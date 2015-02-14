package com.homlin.module.shop.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.exception.MessageException;
import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.dao.TbShopCartDao;
import com.homlin.module.shop.dao.TbShopMemberGradeDao;
import com.homlin.module.shop.dao.TbShopOrderDao;
import com.homlin.module.shop.dao.TbShopOrderItemDao;
import com.homlin.module.shop.model.TbShopCart;
import com.homlin.module.shop.model.TbShopCartItem;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberGrade;
import com.homlin.module.shop.model.TbShopMemberScoreLog;
import com.homlin.module.shop.model.TbShopMemberScoreLog.Valtype;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.module.shop.model.TbShopOrder.OrderStatus;
import com.homlin.module.shop.model.TbShopOrder.PaymentMethod;
import com.homlin.module.shop.model.TbShopOrder.PaymentStatus;
import com.homlin.module.shop.model.TbShopOrder.ShippingStatus;
import com.homlin.module.shop.model.TbShopOrderItem;
import com.homlin.module.shop.model.TbShopShippingMethod;
import com.homlin.module.shop.model.TbShopSku;
import com.homlin.module.shop.plugin.payment.wxpay.WxpayConfig;
import com.homlin.module.shop.plugin.payment.wxpay.web.WxPayHelper;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.service.OrderService;
import com.homlin.module.shop.service.ShippingMethodService;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.module.shop.util.SerialNumberUtil;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Service
public class OrderServiceImpl extends BaseServiceImpl<TbShopOrder, String> implements OrderService {

	@Autowired
	public void setBaseDao(TbShopOrderDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopOrderDao orderDao;

	@Autowired
	TbShopOrderItemDao orderItemDao;

	@Autowired
	TbShopCartDao cartDao;

	@Autowired
	ShippingMethodService shippingMethodService;

	@Autowired
	TbShopMemberGradeDao memberGradeDao;

	@Override
	public String getLastOrderSn() {
		return orderDao.getLastOrderSn();
	}

	@Override
	public TbShopOrder create(TbShopOrder order, TbShopCart cart, Map<String, Object> params) throws Exception {
		String datetime = Util.getNowDateTimeString();
		TbShopMember member = cart.getTbShopMember();

		// 会员折扣
		BigDecimal discount = null;
		if (null != member) {
			TbShopMemberGrade grade = member.getTbShopMemberGrade();
			if (grade != null) {
				discount = grade.getDiscount();
			}
		}
		if (discount == null) {
			discount = BigDecimal.ONE;
		}

		Set<TbShopCartItem> cartItems = cart.getTbShopCartItems();
		Integer totalWeight = 0; // 订单总重量
		Integer totalFreeShippingWeight = 0; // 免运费重量
		Integer totalQuantity = 0; // 订单总件数
		BigDecimal totalPrice = BigDecimal.ZERO;
		for (TbShopCartItem tbShopCartItem : cartItems) {
			TbShopOrderItem orderItem = new TbShopOrderItem();
			TbShopSku sku = tbShopCartItem.getTbShopSku();
			orderItem.setCreateDate(datetime);
			orderItem.setModifyDate(datetime);
			orderItem.setTbShopSku(sku);
			orderItem.setProductId(sku.getTbShopProduct().getId()); // 不关联商品，冗余数据方便查询
			orderItem.setSn(sku.getSn());
			orderItem.setCost(sku.getCost());
			orderItem.setIsGift(AppConstants.FALSE);
			orderItem.setName(sku.getTbShopProduct().getName());
			if (StringUtils.isEmpty(sku.getSampleImage())) {
				orderItem.setImagePath(sku.getTbShopProduct().getSampleImage());
			} else {
				orderItem.setImagePath(sku.getSampleImage());
			}

			orderItem.setOriginalPrice(sku.getPrice());
			orderItem.setDiscount(discount);
			orderItem.setPrice(sku.getPrice().multiply(discount)); // 折后价

			orderItem.setQuantity(tbShopCartItem.getQuantity());
			orderItem.setReturnQuantity(0);
			orderItem.setShippedQuantity(0);
			orderItem.setSpecifications(sku.getSpecificationValueName());
			orderItem.setWeight(sku.getWeight());
			orderItem.setTbShopOrder(order);
			order.getTbShopOrderItems().add(orderItem);

			// ---
			Integer weight = orderItem.getWeight() * orderItem.getQuantity();
			totalWeight += weight;
			if (AppConstants.TRUE.equals(sku.getTbShopProduct().getIsFreeShipping())) {
				// 免运费重量
				totalFreeShippingWeight += weight;
			}
			totalQuantity += orderItem.getQuantity();
			totalPrice = totalPrice.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));

			// 冻结库存
			sku.setStock(sku.getStock() - orderItem.getQuantity());
			sku.setBlockedStock(sku.getBlockedStock() + orderItem.getQuantity());
			sku.getTbShopProduct().setStock(sku.getTbShopProduct().getStock() - orderItem.getQuantity());
			sku.getTbShopProduct().setBlockedStock(sku.getTbShopProduct().getBlockedStock() + orderItem.getQuantity());

		}
		order.setTotalWeight(totalWeight);
		order.setTotalQuantity(totalQuantity);
		order.setTotalPrice(totalPrice);

		// Payment
		order.setPaymentMethod(null);
		order.setPaymentFee(BigDecimal.ZERO);
		order.setPaymentCode(null);
		order.setPaymentStatus(PaymentStatus.unpaid);

		// Shipping
		// [[ 计算运费 ]]
		String shippingMethodId = params.get("shippingMethodId").toString();
		BigDecimal shippingFee = shippingMethodService.getShippingFee(shippingMethodId, totalWeight - totalFreeShippingWeight,
			order.getAreaCode());
		TbShopShippingMethod shippingMethod = shippingMethodService.get(shippingMethodId);
		order.setShippingMethod(shippingMethod.getName());
		order.setShippingFee(shippingFee);
		order.setShippingCode(null);
		order.setShippingStatus(ShippingStatus.unshipped);
		// ]] 计算运费 ]]

		order.setAdjustAmount(BigDecimal.ZERO);
		// 订单合计金额 = 商品总金额 + 卖家金额调整 + 运费 (+ 支付费用)
		order.setTotalAmount(order.getTotalPrice().add(order.getAdjustAmount()).add(order.getShippingFee())); // .add(order.getPaymentFee())
		order.setPaidAmount(BigDecimal.ZERO); // 已付款

		// TODO 优惠券抵扣金额合计
		order.setCouponAmount(BigDecimal.ZERO);

		// [[ 积分抵扣 ]]
		// 积分记录
		TbShopMemberScoreLog scoreLog = null;
		if (null != member && null != order.getUseScoreAmount() && BigDecimal.ZERO.compareTo(order.getUseScoreAmount()) == -1
				&& AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.SHOP_ORDER_USESCORE))) {

			// 判断积分超出限额
			if (BigDecimal.ZERO.compareTo(order.getUnpaidAmount()) == 1) {
				throw new MessageException("使用积分超出限额！");
			}

			// 每抵扣1分钱消耗积分数
			BigDecimal multiple = BigDecimal.ONE;
			try {
				String SHOP_SCORE_ORDERCREDIT_MULTIPLE = CacheUtil.getConfig(CacheConfigKeys.SHOP_SCORE_ORDERCREDIT_MULTIPLE);
				multiple = new BigDecimal(SHOP_SCORE_ORDERCREDIT_MULTIPLE);
			} catch (Exception e) {
				logger.error("订单消费抵扣积分倍数转换错误，默认每抵扣1分钱消耗1积分！" + e.getMessage());
			}
			// 重新计算消耗积分
			BigDecimal useScore = order.getUseScoreAmount().movePointRight(2).multiply(multiple).setScale(0, BigDecimal.ROUND_FLOOR);

			// 判断积分是否足够
			if (useScore.compareTo(member.getScore()) == 1) {
				throw new MessageException("可用积分不足！");
			}

			order.setUseScore(useScore);
			// 扣除使用积分，下单后在修改价格已扣积分不再变
			member.setScore(member.getScore().subtract(useScore));
			member.setModifyDate(datetime);

			// 积分记录
			scoreLog = new TbShopMemberScoreLog();
			scoreLog.setCreateDate(datetime);
			scoreLog.setModifyDate(datetime);
			scoreLog.setTbShopMember(member);
			scoreLog.setVal(useScore.negate());
			scoreLog.setValtype(Valtype.consumption);
			// scoreLog.setOperator("");

			// order.sn后面生成，等生成后再保存scoreLog
			// scoreLog.setRemark("消费抵扣积分，订单号：" + order.getSn());
			// member.getTbShopMemberScoreLogs().add(scoreLog);
		} else {
			order.setUseScoreAmount(BigDecimal.ZERO);
			order.setUseScore(BigDecimal.ZERO);
		}
		// ]]

		// [[ 获得积分 ]]
		// 每消费一元获得积分数
		BigDecimal multiple = BigDecimal.valueOf(1);
		try {
			// 配置：订单消费获得积分倍数，1分钱=N积分
			String SHOP_SCORE_ORDERGAIN_MULTIPLE = CacheUtil.getConfig(CacheConfigKeys.SHOP_SCORE_ORDERGAIN_MULTIPLE);
			multiple = new BigDecimal(SHOP_SCORE_ORDERGAIN_MULTIPLE);
		} catch (Exception e) {
			logger.error("订单消费获得积分倍数转换错误，默认每消费一元获得1积分！" + e.getMessage());
		}
		// 可获积分金额（实付） = 商品总金额 + 卖家金额调整 - 优惠券抵扣金额合计 - 积分抵扣金额合计
		// BigDecimal gainScoreAmount = order.getTotalPrice().add(order.getAdjustAmount())
		// .subtract(order.getCouponAmount()).subtract(order.getUseScoreAmount());
		BigDecimal gainScoreAmount = order.getGainScoreAmount();
		if (BigDecimal.ZERO.compareTo(gainScoreAmount) == 1) {
			gainScoreAmount = BigDecimal.ZERO;
		}
		// order.setGainScore(gainScoreAmount.multiply(multiple).setScale(0, BigDecimal.ROUND_FLOOR));
		order.setGainScore(gainScoreAmount.setScale(0, BigDecimal.ROUND_FLOOR).multiply(multiple));
		order.setGainScoreMutiple(multiple);
		// ]]

		if (order.getUnpaidAmount().compareTo(BigDecimal.ZERO) == 0) {
			order.setPaymentStatus(PaymentStatus.paid);
			if (order.getTotalAmount().subtract(order.getCouponAmount()).compareTo(order.getUseScoreAmount()) == 0) { // 全额积分抵扣
				order.setPaymentMethod(PaymentMethod.JFPAY);
			}
		}
		order.setCreateDate(datetime);
		order.setModifyDate(datetime);
		if (null != member) {
			order.setTbShopMember(member);
			order.setMemberUsername(member.getUsername());
		} else {
			order.setTuid(params.get("tuid").toString());
		}

		if (AppConstants.TRUE.equals(order.getFreightCollect())) { // 运费到付 > 订单已处理 > 可直接付款
			order.setOrderStatus(OrderStatus.confirmed);
		} else {
			order.setOrderStatus(OrderStatus.unconfirmed);
		}

		order.setSn(SerialNumberUtil.buildOrderSn());
		// 积分变更记录
		if (scoreLog != null) {
			scoreLog.setRemark("消费抵扣积分，订单号：" + order.getSn());
			member.getTbShopMemberScoreLogs().add(scoreLog);
		}
		orderDao.save(order);
		if (null != cart.getId()) { // 通过购物车购买
			if (null != cart.getTbShopMember()) { // 会员登入购买
				member.setTbShopCart(null); // 解除关联后删除
			}
			cartDao.delete(cart);
		}
		return order;
	}

	@Override
	public Pager getPagedList(Pager pager) {
		return orderDao.getPagedList(pager);
	}

	@Override
	public void confirmed(TbShopOrder order) throws Exception {
		order.setOrderStatus(OrderStatus.confirmed);
		order.setModifyDate(Util.getNowDateTimeString());
		orderDao.update(order);
	}

	@Override
	public void cancelled(TbShopOrder order) throws Exception {
		String datatime = Util.getNowDateTimeString();
		// 恢复冻结库存
		Set<TbShopOrderItem> orderItems = order.getTbShopOrderItems();
		for (TbShopOrderItem orderItem : orderItems) {
			TbShopSku sku = orderItem.getTbShopSku();
			if (sku != null) {
				sku.setStock(sku.getStock() + orderItem.getQuantity());
				sku.setBlockedStock(sku.getBlockedStock() - orderItem.getQuantity());
				sku.setModifyDate(datatime);
				sku.getTbShopProduct().setStock(sku.getTbShopProduct().getStock() + orderItem.getQuantity());
				sku.getTbShopProduct().setBlockedStock(sku.getTbShopProduct().getBlockedStock() - orderItem.getQuantity());
				sku.getTbShopProduct().setModifyDate(datatime);
			}
			orderItem.setModifyDate(datatime);
		}

		// 返还积分，其他优惠券忽略
		BigDecimal useScore = order.getUseScore(); // 下单时预扣的积分
		if (BigDecimal.ZERO.compareTo(useScore) == -1) {
			TbShopMember member = order.getTbShopMember();
			// 会员积分信息更新
			if (member != null) {
				member.setScore(member.getScore().add(useScore));

				// 积分记录
				TbShopMemberScoreLog scoreLog = new TbShopMemberScoreLog();
				scoreLog.setCreateDate(datatime);
				scoreLog.setModifyDate(datatime);
				scoreLog.setTbShopMember(member);
				scoreLog.setVal(useScore);
				scoreLog.setValtype(Valtype.consumption);
				// scoreLog.setOperator("");
				scoreLog.setRemark("订单取消返还预扣积分，订单号：" + order.getSn());
				member.getTbShopMemberScoreLogs().add(scoreLog);

				member.setModifyDate(datatime);
			}
		}

		// 修改状态
		order.setOrderStatus(OrderStatus.cancelled);
		order.setModifyDate(datatime);
		orderDao.update(order);
	}

	@Override
	public void paid(TbShopOrder order) throws Exception {
		order.setOrderStatus(OrderStatus.confirmed); // 自助购物直接付款，可能不需要再次确认，自动设为已确认
		order.setPaymentStatus(PaymentStatus.paid);
		order.setModifyDate(Util.getNowDateTimeString());
		orderDao.update(order);
	}

	@SuppressWarnings("unchecked")
	private void wx_delivernotify(TbShopOrder order) throws Exception {
		String paymentDataString = order.getPaymentData();
		if (StringUtils.isNotBlank(paymentDataString)) {
			Map<String, String> paymentData = new HashMap<String, String>();
			try {
				paymentData = JacksonUtil.toObject(paymentDataString, Map.class);
			} catch (Exception e) {
			}
			if (paymentData.size() > 0) {
				String AppId = paymentData.get("AppId");
				String OpenId = paymentData.get("OpenId");
				String IsSubscribe = paymentData.get("IsSubscribe");
				String transaction_id = paymentData.get("transaction_id");

				// 通知发货

				// 准备jsapi参数
				WxPayHelper wxPayHelper = new WxPayHelper();

				// 先设置基本信息
				wxPayHelper.SetAppId(WxpayConfig.appId);
				wxPayHelper.SetAppKey(WxpayConfig.paySignKey);
				wxPayHelper.SetPartnerKey(WxpayConfig.partnerKey);
				wxPayHelper.SetSignType("sha1");

				HashMap<String, String> data = new HashMap<String, String>();
				data.put("appid", AppId);
				data.put("openid", OpenId);
				data.put("transid", transaction_id);
				data.put("out_trade_no", order.getSn());
				data.put("deliver_timestamp", String.valueOf(new Date().getTime() / 1000)); // Linux 时间戳 10位数字 不是13位
				data.put("deliver_status", "1");
				data.put("deliver_msg", "ok");
				data.put("app_signature", wxPayHelper.GetBizSign(data));
				data.put("sign_method", "sha1");

				try {
					WeixinUtil.delivernotify(JacksonUtil.toJsonString(data));
				} catch (Exception e) {
					logger.error("微信发货通知发送失败，通知服务器失败！\n" + e.getMessage());
				}

				// 微信消息通知发货
				if ("1".equals(IsSubscribe)) {
					try {
						String message = "{\"touser\":\"%1$s\",\"msgtype\":\"text\",\"text\":{\"content\":\"" +
								"恭喜，您的订单已发货！%n" +
								"订单号：%2$s%n" +
								"物流公司：%3$s%n" +
								"物流单号：%4$s%n" +
								"请注意查收！/:share" +
								"\"}}";
						message = String.format(message, OpenId, order.getSn(), order.getShippingCompany(), order.getShippingCode());
						WeixinUtil.sendCustomMessage(message);
					} catch (Exception e) {
						logger.error("微信发货通知发送失败，微信消息失败！\n" + e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public void shipped(TbShopOrder order) throws Exception {
		String datatime = Util.getNowDateTimeString();
		// 扣冻结库存，销量累加
		Set<TbShopOrderItem> items = order.getTbShopOrderItems();
		for (TbShopOrderItem item : items) {
			TbShopSku sku = item.getTbShopSku();
			if (sku != null) {
				sku.setSales(sku.getSales() + item.getQuantity());
				sku.setBlockedStock(sku.getBlockedStock() - item.getQuantity());
				sku.setModifyDate(datatime);
				sku.getTbShopProduct().setSales(sku.getTbShopProduct().getSales() + item.getQuantity());
				sku.getTbShopProduct().setBlockedStock(sku.getTbShopProduct().getBlockedStock() - item.getQuantity());
				sku.getTbShopProduct().setModifyDate(datatime);
			}
			item.setShippedQuantity(item.getQuantity());
			item.setModifyDate(datatime);
		}

		// 修改状态
		order.setOrderStatus(OrderStatus.confirmed); // 预留。可能是货到付款，不需要再次确认，自动设为已确认
		order.setShippingStatus(ShippingStatus.shipped);
		order.setModifyDate(datatime);
		orderDao.update(order);

		if (order.getPaymentMethod() == PaymentMethod.WXPAY) {
			// 发货通知,WXPAY必须通知
			try {
				wx_delivernotify(order);
			} catch (Exception e) {
				logger.error("微信发货通知发送失败！");
				logger.error("e.printStackTrace()");
				e.printStackTrace();
			}
		} else if (order.getPaymentMethod() == PaymentMethod.ALIPAY) {
			// TODO 支付宝担保交易需通知
		}
	}

	@Override
	public void completed(TbShopOrder order) throws Exception {
		String datatime = Util.getNowDateTimeString();
		// 会员积分信息更新
		TbShopMember member = order.getTbShopMember();
		if (member != null) {
			// 消费获得积分
			BigDecimal score = order.getGainScore();
			if (BigDecimal.ZERO.compareTo(score) == -1) {
				member.setScore(member.getScore().add(score));
				member.setScoreAddup(member.getScoreAddup().add(score));

				// 会员等级变更
				TbShopMemberGrade grade = member.getTbShopMemberGrade();
				if (grade == null || !AppConstants.TRUE.equals(grade.getIsSpecial())) {
					TbShopMemberGrade newGrade = memberGradeDao.findByScore(member.getScoreAddup());
					if (newGrade != null) {
						member.setTbShopMemberGrade(newGrade);
					}
				}

				// 积分记录
				TbShopMemberScoreLog scoreLog = new TbShopMemberScoreLog();
				scoreLog.setCreateDate(datatime);
				scoreLog.setModifyDate(datatime);
				scoreLog.setTbShopMember(member);
				scoreLog.setVal(score);
				scoreLog.setValtype(Valtype.consumption);
				// scoreLog.setOperator("");
				scoreLog.setRemark("消费获得积分，订单号：" + order.getSn());
				member.getTbShopMemberScoreLogs().add(scoreLog);

				member.setModifyDate(datatime);
			}
		}

		// 修改状态
		order.setOrderStatus(OrderStatus.completed);
		order.setModifyDate(datatime);
		orderDao.update(order);
	}

	public TbShopOrder getBySn(String sn) {
		return orderDao.findOneByProperty("sn", sn);
	}

	@Override
	public void evictPaymentExpiredOrders(Date expiredDate) {
		logger.info("启动定期清理未付款订单释放冻结库存" + Util.getNowDateTimeString());
		List<TbShopOrder> orders = orderDao.getPaymentExpiredOrders(expiredDate);
		for (TbShopOrder order : orders) {
			try {
				cancelled(order);
			} catch (Exception e) {
				logger.error("OrderJob.evictPaymentExpiredOrders 失败 - 定期清理未付款订单释放冻结库存:" + order.getSn());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void autoCompletedOrders(Date expiredDate) {
		logger.info("启动超期订单自动完成" + Util.getNowDateTimeString());
		List<TbShopOrder> orders = orderDao.getCompletedExpiredOrders(expiredDate);
		for (TbShopOrder order : orders) {
			try {
				completed(order);
			} catch (Exception e) {
				logger.error("OrderJob.autoCompletedOrders 失败 - 超期订单自动完成:" + order.getSn());
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<TbShopOrder> getExportDataList(Map<String, Object> params) {
		return orderDao.getExportDataList(params);
	}

	@Override
	public TbShopOrderItem getTbShopOrderItem(String item_id) {
		return orderItemDao.get(item_id);
	}

}
