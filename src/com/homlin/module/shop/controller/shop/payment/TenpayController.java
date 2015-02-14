package com.homlin.module.shop.controller.shop.payment;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.controller.shop.member.BaseMemberController;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.module.shop.model.TbShopOrder.OrderStatus;
import com.homlin.module.shop.model.TbShopOrder.PaymentMethod;
import com.homlin.module.shop.model.TbShopOrder.PaymentStatus;
import com.homlin.module.shop.plugin.payment.tenpay.RequestHandler;
import com.homlin.module.shop.plugin.payment.tenpay.ResponseHandler;
import com.homlin.module.shop.plugin.payment.tenpay.TenpayConfig;
import com.homlin.module.shop.plugin.payment.tenpay.client.ClientResponseHandler;
import com.homlin.module.shop.plugin.payment.tenpay.client.TenpayHttpClient;
import com.homlin.module.shop.plugin.payment.tenpay.util.TenpayUtil;
import com.homlin.module.shop.service.impl.OrderServiceImpl;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.Util;

//@Controller
public class TenpayController extends BaseMemberController {

	@Autowired
	OrderServiceImpl orderService;

	private String moneyToFenString(BigDecimal money) {
		return money.movePointRight(2).setScale(0).toString();
	}

	@RequestMapping(value = "/payment/tenpay", params = { "type=order" })
	public String tenpay(@RequestParam(value = "id", defaultValue = "") String id,
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

		// ================
		request.setCharacterEncoding("GBK");

		// 创建支付请求对象
		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init();
		// 设置密钥
		reqHandler.setKey(TenpayConfig.key);
		// 设置支付网关
		reqHandler.setGateUrl("https://gw.tenpay.com/gateway/pay.htm");
		// reqHandler.setGateUrl("https://wap.tenpay.com/cgi-bin/wappayv2.0/wappay_init.htm");
		// System.err.println("partner" + TenpayConfig.partner);
		// System.err.println("key" + TenpayConfig.key);

		// -----------------------------
		// 设置支付参数
		// -----------------------------
		reqHandler.setParameter("partner", TenpayConfig.partner); // 商户号
		reqHandler.setParameter("out_trade_no", order.getSn()); // 商家订单号
		reqHandler.setParameter("total_fee", String.valueOf(moneyToFenString(order.getUnpaidAmount()))); // 商品金额,以分为单位
		reqHandler.setParameter("return_url", SHOP_URL + "/payment/tenpay_return.htm"); // 交易完成后跳转的URL
		reqHandler.setParameter("notify_url", SHOP_URL + "/payment/tenpay_notify.do"); // 接收财付通通知的URL
		reqHandler.setParameter("body", CacheUtil.getConfig(CacheConfigKeys.SHOP_NAME) + "_订单付款_" + order.getSn()); // 商品描述
		reqHandler.setParameter("bank_type", "DEFAULT"); // 银行类型(中介担保时此参数无效)
		reqHandler.setParameter("spbill_create_ip", request.getRemoteAddr()); // 用户的公网ip，不是商户服务器IP
		reqHandler.setParameter("fee_type", "1"); // 币种，1人民币
		reqHandler.setParameter("subject", "订单付款_" + order.getSn()); // 商品名称(中介交易时必填)

		// 系统可选参数
		reqHandler.setParameter("sign_type", "MD5"); // 签名类型,默认：MD5
		reqHandler.setParameter("service_version", "1.0"); // 版本号，默认为1.0
		reqHandler.setParameter("input_charset", "GBK"); // 字符编码
		reqHandler.setParameter("sign_key_index", "1"); // 密钥序号

		// 业务可选参数
		reqHandler.setParameter("attach", ""); // 附加数据，原样返回
		reqHandler.setParameter("product_fee", ""); // 商品费用，必须保证transport_fee + product_fee=total_fee
		reqHandler.setParameter("transport_fee", "0"); // 物流费用，必须保证transport_fee + product_fee=total_fee
		String currTime = Util.getDateTimeString(Util.stringToDate(order.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		reqHandler.setParameter("time_start", currTime); // 订单生成时间，格式为yyyymmddhhmmss
		reqHandler.setParameter("time_expire", ""); // 订单失效时间，格式为yyyymmddhhmmss
		reqHandler.setParameter("buyer_id", ""); // 买方财付通账号
		reqHandler.setParameter("goods_tag", ""); // 商品标记
		reqHandler.setParameter("trade_mode", "1"); // 交易模式，1即时到账(默认)，2中介担保，3后台选择（买家进支付中心列表选择）
		reqHandler.setParameter("transport_desc", ""); // 物流说明
		reqHandler.setParameter("trans_type", "1"); // 交易类型，1实物交易，2虚拟交易
		reqHandler.setParameter("agentid", ""); // 平台ID
		reqHandler.setParameter("agent_type", ""); // 代理模式，0无代理(默认)，1表示卡易售模式，2表示网店模式
		reqHandler.setParameter("seller_id", ""); // 卖家商户号，为空则等同于partner

		// 请求的url
		String requestUrl = reqHandler.getRequestURL();

		// 获取debug信息,建议把请求和debug信息写入日志，方便定位问题
		String debuginfo = reqHandler.getDebugInfo();
		// System.out.println("requestUrl:  " + requestUrl);
		// System.out.println("sign_String:  " + debuginfo);
		logger.error("【财付通付款日志】" + requestUrl);
		logger.error("【财付通付款日志】" + debuginfo);

		// ================

		response.sendRedirect(requestUrl);

		return null;

	}

	@RequestMapping(value = "/payment/tenpay_return", method = RequestMethod.POST)
	public String tenpay_return(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

		// ---------------------------------------------------------
		// 财付通支付应答（处理回调）示例，商户按照此文档进行开发即可
		// ---------------------------------------------------------

		// 创建支付应答对象
		ResponseHandler resHandler = new ResponseHandler(request, response);
		resHandler.setKey(TenpayConfig.key);

		System.out.println("前台回调返回参数:" + resHandler.getAllParameters());

		// 判断签名
		if (resHandler.isTenpaySign()) {

			// 通知id
			// String notify_id = resHandler.getParameter("notify_id");
			// 商户订单号
			String out_trade_no = resHandler.getParameter("out_trade_no");
			// 财付通订单号
			String transaction_id = resHandler.getParameter("transaction_id");
			// 金额,以分为单位
			// String total_fee = resHandler.getParameter("total_fee");
			// 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
			// String discount = resHandler.getParameter("discount");
			// 支付结果
			String trade_state = resHandler.getParameter("trade_state");
			// 交易模式，1即时到账，2中介担保
			String trade_mode = resHandler.getParameter("trade_mode");

			if ("1".equals(trade_mode)) { // 即时到账
				if ("0".equals(trade_state)) {
					// ------------------------------
					// 即时到账处理业务开始
					// ------------------------------

					// 注意交易单不要重复处理
					// 注意判断返回金额

					// ------------------------------
					// 即时到账处理业务完毕
					// ------------------------------

					response.getWriter()
							.append("财付通即时到帐付款成功！")
							.append("<br />订单号：" + out_trade_no)
							.append("<br />交易号：" + transaction_id);
					// System.out.println("即时到帐付款成功");
				} else {
					response.getWriter()
							.append("财付通即时到帐付款失败！");
					logger.error("即时到帐付款失败");
				}
			} else if ("2".equals(trade_mode)) { // 中介担保
				if ("0".equals(trade_state)) {
					// ------------------------------
					// 中介担保处理业务开始
					// ------------------------------

					// 注意交易单不要重复处理
					// 注意判断返回金额

					// ------------------------------
					// 中介担保处理业务完毕
					// ------------------------------

					response.getWriter()
							.append("财付通中介担保付款成功！")
							.append("<br />订单号：" + out_trade_no)
							.append("<br />交易号：" + transaction_id);
					// System.out.println("中介担保付款成功");
				} else {
					response.getWriter()
							.append("财付通中介担保付款失败！trade_state=" + trade_state);
					logger.error("财付通中介担保付款失败！trade_state=" + trade_state);
				}
			}
		} else {
			response.getWriter().append("认证签名失败");
			logger.error("认证签名失败");
		}

		// 获取debug信息,建议把debug信息写入日志，方便定位问题
		String debuginfo = resHandler.getDebugInfo();
		System.out.println("debuginfo:" + debuginfo);
		// out.print("sign_String:  " + debuginfo + "<br><br>");

		return null; // XXX 跳转到提示页面
	}

	@RequestMapping(value = "/payment/tenpay_notify", method = RequestMethod.POST)
	public String tenpay_notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ---------------------------------------------------------
		// 财付通支付通知（后台通知）示例，商户按照此文档进行开发即可
		// ---------------------------------------------------------

		// 创建支付应答对象
		ResponseHandler resHandler = new ResponseHandler(request, response);
		resHandler.setKey(TenpayConfig.key);

		System.out.println("后台回调返回参数:" + resHandler.getAllParameters());

		// 判断签名
		if (resHandler.isTenpaySign()) {

			// 通知id
			String notify_id = resHandler.getParameter("notify_id");

			// 创建请求对象
			RequestHandler queryReq = new RequestHandler(null, null);
			// 通信对象
			TenpayHttpClient httpClient = new TenpayHttpClient();
			// 应答对象
			ClientResponseHandler queryRes = new ClientResponseHandler();

			// 通过通知ID查询，确保通知来至财付通
			queryReq.init();
			queryReq.setKey(TenpayConfig.key);
			queryReq.setGateUrl("https://gw.tenpay.com/gateway/simpleverifynotifyid.xml");
			queryReq.setParameter("partner", TenpayConfig.partner);
			queryReq.setParameter("notify_id", notify_id);

			// 通信对象
			httpClient.setTimeOut(5);
			// 设置请求内容
			httpClient.setReqContent(queryReq.getRequestURL());
			System.out.println("验证ID请求字符串:" + queryReq.getRequestURL());

			// 后台调用
			if (httpClient.call()) {
				// 设置结果参数
				queryRes.setContent(httpClient.getResContent());
				System.out.println("验证ID返回字符串:" + httpClient.getResContent());
				queryRes.setKey(TenpayConfig.key);

				// 获取id验证返回状态码，0表示此通知id是财付通发起
				String retcode = queryRes.getParameter("retcode");

				// 商户订单号
				String out_trade_no = resHandler.getParameter("out_trade_no");
				// 财付通订单号
				String transaction_id = resHandler.getParameter("transaction_id");
				// 金额,以分为单位
				// String total_fee = resHandler.getParameter("total_fee");
				// 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
				// String discount = resHandler.getParameter("discount");
				// 支付结果
				String trade_state = resHandler.getParameter("trade_state");
				// 交易模式，1即时到账，2中介担保
				String trade_mode = resHandler.getParameter("trade_mode");

				// 判断签名及结果
				if (queryRes.isTenpaySign() && "0".equals(retcode)) {
					System.out.println("id验证成功");

					if ("1".equals(trade_mode)) { // 即时到账
						if ("0".equals(trade_state)) {
							// ------------------------------
							// 即时到账处理业务开始
							// ------------------------------

							// 处理数据库逻辑
							// 注意交易单不要重复处理
							// 注意判断返回金额

							TbShopOrder order = orderService.getBySn(out_trade_no);
							if (null != order && order.getPaymentStatus() == PaymentStatus.unpaid) {
								order.setPaymentMethod(PaymentMethod.TENPAY);
								order.setPaymentStatus(PaymentStatus.paid);
								order.setPaymentCode(transaction_id);
								orderService.paid(order);

								System.out.println("即时到账支付成功");
								// 给财付通系统发送成功信息，财付通系统收到此结果后不再进行后续通知
								resHandler.sendToCFT("success");
							}
							// ------------------------------
							// 即时到账处理业务完毕
							// ------------------------------

						} else {
							System.out.println("即时到账支付失败");
							resHandler.sendToCFT("fail");
						}
					} else if ("2".equals(trade_mode)) { // 中介担保
						// ------------------------------
						// 中介担保处理业务开始
						// ------------------------------

						// 处理数据库逻辑
						// 注意交易单不要重复处理
						// 注意判断返回金额

						int iStatus = TenpayUtil.toInt(trade_state);
						switch (iStatus) {
							case 0: // 付款成功

								break;
							case 1: // 交易创建

								break;
							case 2: // 收获地址填写完毕

								break;
							case 4: // 卖家发货成功

								break;
							case 5: // 买家收货确认，交易成功

								break;
							case 6: // 交易关闭，未完成超时关闭

								break;
							case 7: // 修改交易价格成功

								break;
							case 8: // 买家发起退款

								break;
							case 9: // 退款成功

								break;
							case 10: // 退款关闭

								break;
							default:
						}

						// ------------------------------
						// 中介担保处理业务完毕
						// ------------------------------

						System.out.println("trade_state = " + trade_state);
						// 给财付通系统发送成功信息，财付通系统收到此结果后不再进行后续通知
						resHandler.sendToCFT("success");
					}
				} else {
					// 错误时，返回结果未签名，记录retcode、retmsg看失败详情。
					System.out.println("查询验证签名失败或id验证失败" + ",retcode:" + queryRes.getParameter("retcode"));
					logger.error("查询验证签名失败或id验证失败" + ",retcode:" + queryRes.getParameter("retcode"));
				}
			} else {
				logger.error("财付通后台调用通信失败");
				System.out.println("后台调用通信失败");
				System.out.println(httpClient.getResponseCode());
				System.out.println(httpClient.getErrInfo());
				// 有可能因为网络原因，请求已经处理，但未收到应答。
			}
		} else {
			System.out.println("通知签名验证失败");
			logger.error("财付通通知签名验证失败");
		}

		return null;
	}

}
