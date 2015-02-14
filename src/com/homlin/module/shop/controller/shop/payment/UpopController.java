package com.homlin.module.shop.controller.shop.payment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.controller.shop.member.BaseMemberController;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.module.shop.model.TbShopOrder.OrderStatus;
import com.homlin.module.shop.model.TbShopOrder.PaymentMethod;
import com.homlin.module.shop.model.TbShopOrder.PaymentStatus;
import com.homlin.module.shop.plugin.payment.unionpay.upop.UpopConfig;
import com.homlin.module.shop.plugin.payment.unionpay.upop.UpopUtil;
import com.homlin.module.shop.service.impl.OrderServiceImpl;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.IPUtil;

//未测试
//@Controller
public class UpopController extends BaseMemberController {

	@Autowired
	OrderServiceImpl orderService;

	private String moneyToFenString(BigDecimal money) {
		return money.movePointRight(2).setScale(0).toString();
	}

	@RequestMapping(value = "/payment/upop", params = { "type=order" })
	public String upop(@RequestParam(value = "id", defaultValue = "") String id,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		TbShopOrder order = orderService.get(id);

		boolean available = false; // 是不是我的订单
		if (order == null) {
			available = false;
		} else {
			if (order.getTbShopMember() == null) {
				available = order.getTuid().equals(getTuid());
			} else {
				available = order.getTbShopMember().getId().equals(getMemberId());
			}
		}
		if (!available) { // XXX 是否检验
			addActionError("没有找到匹配信息");
			return getTemplatePath() + "/error";
		}

		if (order.getPaymentStatus() != PaymentStatus.unpaid) {
			addActionError("该订单已付过款！");
			return PAGE_ERROR;
		}
		if (order.getOrderStatus() == OrderStatus.cancelled) {
			addActionError("该订单已取消！");
			return PAGE_ERROR;
		}
		if (order.getOrderStatus() == OrderStatus.completed) {
			addActionError("该订单已完成！");
			return PAGE_ERROR;
		}
		// if (order.getOrderStatus() == OrderStatus.unconfirmed) {
		// addActionError("该订单未确认金额，请联系客服！");
		// return PAGE_ERROR;
		// }

		final String SHOP_URL = CacheUtil.getConfig(CacheConfigKeys.SHOP_URL);

		// 商户需要组装如下对象的数据
		String[] valueVo = new String[] {
				UpopConfig.version,// 协议版本
				UpopConfig.charset,// 字符编码
				"01",// 交易类型
				"",// 原始交易流水号
				UpopConfig.merCode,// 商户代码
				UpopConfig.merName,// 商户简称
				"",// 收单机构代码（仅收单机构接入需要填写）
				"",// 商户类别（收单机构接入需要填写）
				SHOP_URL + "/member/order/view/" + order.getId() + ".htm",// 商品URL
				UpopConfig.merName + "_订单付款_" + order.getSn(),// 商品名称
				moneyToFenString(order.getTotalAmount()),// 商品单价 单位：分
				"1",// 商品数量
				"0",// 折扣 单位：分
				moneyToFenString(order.getShippingFee()),// 运费 单位：分
				// ("00000000" + order.getSn()).substring(order.getSn().length()),// 订单号（需要商户自己生成）,8-32位，系统只有6位
				order.getSn(),// 订单号（需要商户自己生成）,8-32位
				// new SimpleDateFormat("yyyyMMddHHmmss'-'").format(order.getCreateDate()) + order.getOrderSn(),// 订单号（需要商户自己生成）,8-32位
				// RandomStringUtils.randomNumeric(8) + order.getOrderSn(),// 订单号（需要商户自己生成）,8-32位
				moneyToFenString(order.getUnpaidAmount()),// 交易金额 单位：分
				"156",// 交易币种
				new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),// 交易时间，注意不是订单时间，而是当前生成付款链接时间
				IPUtil.getRemoteIP(request),// 用户IP
				order.getName(),// 用户真实姓名
				"",// 默认支付方式
				"",// 默认银行编号
				"300000",// 交易超时时间
				SHOP_URL + "/payment/upop_return.htm",// 前台回调商户URL
				SHOP_URL + "/payment/upop_notify.do",// 后台回调商户URL
				""// 商户保留域
		};

		// if (UnionpayConfig.debug) {
		// System.err.println("UnionpayConfig.debug=true，这里要修改配置文件");
		// valueVo[24] = "http://qzsjxxkj.xicp.net:51907/payment/unionpay_notify.do";
		// }

		String signType = request.getParameter("sign");
		if (!UpopConfig.signType_SHA1withRSA.equalsIgnoreCase(signType)) {
			signType = UpopConfig.signType;
		}

		/*
		 * 说明：以下代码直接返回跳转到银联在线支付页面字符串
		 */
		String html = UpopUtil.createPayHtml(valueVo, signType);// 跳转到银联页面支付

		/*
		 * 说明：以下代码直接返回跳转到银行支付页面字符串 目前:支持工行(ICBC)，农行(ABC)，中行(BOC)，建行(CCB)，招行(CMB)，广发(GDB)，浦发(SPDB)
		 * UnionpayUtil.createPayHtml(valueVo, "CCB", signType)
		 */
		// String html = UnionpayUtil.createPayHtml(valueVo, "CCB", signType);//直接跳转到网银页面支付
		response.setContentType("text/html;charset=" + UpopConfig.charset);
		response.setCharacterEncoding(UpopConfig.charset);
		try {
			response.getWriter().write(html);
		} catch (IOException e) {

		}
		response.setStatus(HttpServletResponse.SC_OK);

		return null;

	}

	@RequestMapping(value = "/payment/upop_return", method = RequestMethod.POST)
	public String upop_return(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// System.err.println(UpopConfig.gateWay);
		try {
			request.setCharacterEncoding(UpopConfig.charset);
		} catch (UnsupportedEncodingException e) {
		}

		String[] resArr = new String[UpopConfig.notifyVo.length];
		for (int i = 0; i < UpopConfig.notifyVo.length; i++) {
			resArr[i] = request.getParameter(UpopConfig.notifyVo[i]);
		}
		String signature = request.getParameter(UpopConfig.signature);
		String signMethod = request.getParameter(UpopConfig.signMethod);

		response.setContentType("text/html;charset=" + UpopConfig.charset);
		response.setCharacterEncoding(UpopConfig.charset);

		try {
			String orderNumber = request.getParameter("orderNumber"); // 商户订单号
			orderNumber = Long.valueOf(orderNumber).toString();
			String qid = request.getParameter("qid"); // 银联在线支付交易号
			Boolean signatureCheck = UpopUtil.checkSign(resArr, signMethod, signature);
			// response.getWriter()
			// // .append("建议前台通知和后台通知用两个类实现，后台通知进行商户的数据库等处理,前台通知实现客户浏览器跳转<br>")
			// .append("签名是否正确：" + signatureCheck)
			// .append("<br>交易是否成功：" + "00".equals(resArr[10]));
			if (signatureCheck) {// 验证成功
				// 请在这里加上商户的业务逻辑程序代码
				if ("00".equals(resArr[10])) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 如果有做过处理，不执行商户的业务程序
					response.getWriter()
							.append("银联在线付款请求成功！")
							.append("<br />订单号：" + orderNumber)
							.append("<br />交易号：" + qid);
				} else {
					// 该页面可做页面美工编辑
					response.getWriter()
							.append("银联在线支付失败 ：败原因:" + resArr[11])
							.append("<br />订单号：" + orderNumber)
							.append("<br />交易号：" + qid);
					logger.error("银联在线支付失败 ：败原因:" + resArr[11] + "\n\r订单号：" + orderNumber + "\n\r交易号：" + qid);
				}
			} else {
				response.getWriter().append("验证失败");
				logger.error("验证失败");
			}
		} catch (IOException e) {
		}

		response.setStatus(HttpServletResponse.SC_OK);
		return null; // XXX 跳转到提示页面
	}

	// 调试时注意域名拦截器
	@RequestMapping(value = "/payment/upop_notify", method = RequestMethod.POST)
	public String upop_notify(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			request.setCharacterEncoding(UpopConfig.charset);
		} catch (UnsupportedEncodingException e) {
		}

		String[] resArr = new String[UpopConfig.notifyVo.length];
		for (int i = 0; i < UpopConfig.notifyVo.length; i++) {
			resArr[i] = request.getParameter(UpopConfig.notifyVo[i]);
		}
		String signature = request.getParameter(UpopConfig.signature);
		String signMethod = request.getParameter(UpopConfig.signMethod);

		response.setContentType("text/html;charset=" + UpopConfig.charset);
		response.setCharacterEncoding(UpopConfig.charset);

		try {
			String orderNumber = request.getParameter("orderNumber"); // 商户订单号
			// orderNumber = Long.valueOf(orderNumber).toString();
			String qid = request.getParameter("qid"); // 银联在线支付交易号
			Boolean signatureCheck = UpopUtil.checkSign(resArr, signMethod, signature);
			// response.getWriter()
			// // .append("建议前台通知和后台通知用两个类实现，后台通知进行商户的数据库等处理,前台通知实现客户浏览器跳转<br>")
			// .append("签名是否正确：" + signatureCheck)
			// .append("<br>交易是否成功：" + "00".equals(resArr[10]));
			if (signatureCheck) {// 验证成功
				// 请在这里加上商户的业务逻辑程序代码
				if ("00".equals(resArr[10])) {
					// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

					TbShopOrder order = orderService.getBySn(orderNumber);
					if (null != order && order.getPaymentStatus() == PaymentStatus.unpaid) {
						order.setPaymentMethod(PaymentMethod.TENPAY);
						order.setPaymentStatus(PaymentStatus.paid);
						order.setPaymentCode(qid);
						orderService.paid(order);
					}

					// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				} else {
					logger.error("银联在线支付失败 ：败原因:" + resArr[11] + "\n\r订单号：" + orderNumber + "\n\r交易号：" + qid);
				}
			} else {
				logger.error("验证失败");
			}
		} catch (IOException e) {
		}

		response.setStatus(HttpServletResponse.SC_OK);

		return null;
	}

}
