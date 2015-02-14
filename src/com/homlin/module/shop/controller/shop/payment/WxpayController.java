package com.homlin.module.shop.controller.shop.payment;

import java.io.BufferedReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.SpringContextHolder;
import com.homlin.module.commom.mail.MailService;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.controller.shop.member.BaseMemberController;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.module.shop.model.TbShopOrder.OrderStatus;
import com.homlin.module.shop.model.TbShopOrder.PaymentMethod;
import com.homlin.module.shop.model.TbShopOrder.PaymentStatus;
import com.homlin.module.shop.model.TbWxPayfeedback;
import com.homlin.module.shop.model.TbWxPayfeedback.FeedBackStatus;
import com.homlin.module.shop.plugin.payment.wxpay.WxpayConfig;
import com.homlin.module.shop.plugin.payment.wxpay.server.RequestHandler;
import com.homlin.module.shop.plugin.payment.wxpay.server.ResponseHandler;
import com.homlin.module.shop.plugin.payment.wxpay.server.client.ClientResponseHandler;
import com.homlin.module.shop.plugin.payment.wxpay.server.client.TenpayHttpClient;
import com.homlin.module.shop.plugin.payment.wxpay.web.WxPayHelper;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.service.WxPayfeedbackService;
import com.homlin.module.shop.service.impl.OrderServiceImpl;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.IPUtil;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.PropertyHelper;
import com.homlin.utils.Util;

/**
 * <pre>
 * 开通流程：
 * 申请后进入公众平台 > 商户功能 > 支付测试
 * 通过测试目录，测试账号<b style="color:red;">成功支付</b>一笔订单，
 * 到管理后台<b style="color:red;">执行发货</b>
 * 返回公众平台商户功能<b style="color:red;">发布</b>全网支付
 * </pre>
 * 
 * Date:2014-5-17 下午03:23:20
 * 
 * @author wuduanpiao
 * 
 */
@Controller
public class WxpayController extends BaseMemberController {

	@Autowired
	OrderServiceImpl orderService;

	@Autowired
	WxPayfeedbackService wxPayfeedbackService;

	private String moneyToFenString(BigDecimal money) {
		return money.movePointRight(2).setScale(0).toString();
	}

	@RequestMapping(value = "/payment/wxpay", params = { "type=order" })
	public String wxpay(@RequestParam(value = "id", defaultValue = "") String id,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		TbShopOrder order = orderService.get(id);

		if (!Util.isWxpayJsApiSupported(request)) {
			addActionError("当前浏览器不支持微信支付，<br>请选择其他支付方式");
			return getTemplatePath() + "/error";
		}

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
			return getTemplatePath() + "/error";
		}
		if (order.getOrderStatus() == OrderStatus.cancelled) {
			addActionError("该订单已取消！");
			return getTemplatePath() + "/error";
		}
		if (order.getOrderStatus() == OrderStatus.completed) {
			addActionError("该订单已完成！");
			return getTemplatePath() + "/error";
		}
		// if (order.getOrderStatus() == OrderStatus.unconfirmed) {
		// addActionError("该订单未确认金额，请联系客服！");
		// return PAGE_ERROR;
		// }

		final String SHOP_URL = CacheUtil.getConfig(CacheConfigKeys.SHOP_URL);

		// 准备jsapi参数
		WxPayHelper wxPayHelper = new WxPayHelper();

		// 先设置基本信息
		wxPayHelper.SetAppId(WxpayConfig.appId);
		wxPayHelper.SetAppKey(WxpayConfig.paySignKey);
		wxPayHelper.SetPartnerKey(WxpayConfig.partnerKey);
		wxPayHelper.SetSignType("sha1");

		// 设置请求package信息
		wxPayHelper.SetParameter("bank_type", "WX");
		wxPayHelper.SetParameter("body", "付款订单_" + order.getSn() + " - " + CacheUtil.getConfig(CacheConfigKeys.SHOP_NAME));
		wxPayHelper.SetParameter("partner", WxpayConfig.partnerId);
		wxPayHelper.SetParameter("out_trade_no", order.getSn());
		wxPayHelper.SetParameter("total_fee", String.valueOf(moneyToFenString(order.getUnpaidAmount())));
		wxPayHelper.SetParameter("fee_type", "1");
		wxPayHelper.SetParameter("notify_url", SHOP_URL + "/payment/wxpay_notify.do");
		wxPayHelper.SetParameter("spbill_create_ip", IPUtil.getRemoteIP(getRequest()));
		wxPayHelper.SetParameter("input_charset", "UTF-8");

		// ==========

		// System.out.println("生成app支付package:");
		// System.out.println(wxPayHelper.CreateAppPackage("test"));
		// System.out.println("生成jsapi支付package:");
		String BrandWCPayRequestPackage = wxPayHelper.CreateBizPackage();
		// System.out.println(BrandWCPayRequestPackage);
		// payments.put("微信支付", "WXPAY");
		request.setAttribute("BrandWCPayRequestPackage", BrandWCPayRequestPackage);
		// model.addAttribute("BrandWCPayRequestPackage", BrandWCPayRequestPackage);
		// Util.copyToClipboard(BrandWCPayRequestPackage);

		return getTemplatePath() + "/order/pay_wx_jsapi";
	}

	private String getPostData(HttpServletRequest request) {
		String xmlString = "";
		try {
			BufferedReader in = request.getReader();
			String line;
			while ((line = in.readLine()) != null) {
				xmlString += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlString;
	}

	private Map<String, String> getNotifyData(HttpServletRequest request) {

		// transport_fee : 0 //物流费用，单位分，默认 0。 如 果 有 值 ， 必 须 保 证transport_fee + product_fee = total_fee
		// trade_state : 0 //交易状态 支付结果： 0—成功 其他保留
		// trade_mode : 1 //交易模式:1-即时到账
		// sign_type : MD5
		// input_charset : UTF-8
		// fee_type : 1 //现金支付币种,目前只支持人民币,默认值是 1-人民币
		// out_trade_no : 075049654 //商户系统的订单号，与请求一致
		// transaction_id : 1218611701 20140410 3224950564 //交易号，28 位长的数值，其中前 10 位为商户号，之后 8 位为 订 单 产 生 的 日 期 ， 如20090415，最后 10 位是流水号。
		// discount : 0
		// bank_billno : 201404101555443 //银行订单号
		// sign : 58331EF00B323E0A11D3D2F21AB6AC0D
		// total_fee : 1 //支付金额，单位为分，如果discount 有值，通知的 total_fee + discount = 请求的 total_fee
		// time_end : 20140410182658 // 支 付 完 成 时 间
		// partner : 1218611701
		// notify_id : Svr6U7pe0ITgveQMNiGXtWO1-6UkvMu6K7iYHz_9JTInjWuJuMQYD7aH5ab3Wr_hyWJFnP0RI2RXR-5jWdVAvKRaIEzyTf5p //支付结果通知 id，对于某些特定商户，只返回通知 id，要求商户据此查询交易结果
		// bank_type : 3006
		// product_fee : 1
		// pay_info : //支付结果信息 支付结果信息，支付成功时为空

		Map<String, String> paymentData = new HashMap<String, String>();
		Set<String> keys = request.getParameterMap().keySet();
		for (String key : keys) {
			String value = request.getParameter(key);
			if (null == value) {
				value = "";
			}
			paymentData.put(key, value);
		}
		// String xmlString = "<xml>"
		// + "<OpenId><![CDATA[oBZG-uAxgUR_EJTc3Xkwb5JmPYww]]></OpenId>"
		// + "<AppId><![CDATA[wxf13c857d6ac62f3e]]></AppId>"
		// + "<IsSubscribe>1</IsSubscribe>"
		// + "<TimeStamp>1397212780</TimeStamp>"
		// + "<NonceStr><![CDATA[mwrpLdlhSyObn2nV]]></NonceStr>"
		// + "<AppSignature><![CDATA[c3ccd1520757de07b3af9c4bfab277065420e8ae]]></AppSignature>"
		// + "<SignMethod><![CDATA[sha1]]></SignMethod>"
		// + "</xml>";
		String xmlString = getPostData(request);
		// System.out.println("XML:" + xmlString);
		try {
			Document doc = DocumentHelper.parseText(xmlString);
			Element root = doc.getRootElement(); // 获取根节点
			// String OpenId = root.elementTextTrim("OpenId");
			// String IsSubscribe = root.elementTextTrim("IsSubscribe");

			@SuppressWarnings("unchecked")
			List<Element> elements = root.elements();
			for (Element element : elements) {
				paymentData.put(element.getName(), element.getTextTrim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paymentData;
	}

	@RequestMapping(value = "/payment/wxpay_notify", method = RequestMethod.POST)
	public String wxpay_notify(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 创建支付应答对象
		ResponseHandler resHandler = new ResponseHandler(request, response);
		resHandler.setKey(WxpayConfig.partnerKey);

		// traceRequest();
		// System.out.println("后台回调返回参数:" + resHandler.getAllParameters());

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
			queryReq.setKey(WxpayConfig.partnerKey);
			queryReq.setGateUrl("https://gw.tenpay.com/gateway/verifynotifyid.xml");
			queryReq.setParameter("partner", WxpayConfig.partnerId);
			queryReq.setParameter("notify_id", notify_id);

			// 通信对象
			httpClient.setTimeOut(5);
			// 设置请求内容
			httpClient.setReqContent(queryReq.getRequestURL());
			// System.out.println("验证ID请求字符串:" + queryReq.getRequestURL());

			// 后台调用
			if (httpClient.call()) {
				// 设置结果参数
				queryRes.setContent(httpClient.getResContent());
				// System.out.println("验证ID返回字符串:" + httpClient.getResContent());
				queryRes.setKey(WxpayConfig.partnerKey);

				// 获取id验证返回状态码，0表示此通知id是财付通发起
				String retcode = queryRes.getParameter("retcode");

				// 支付结果
				String trade_state = resHandler.getParameter("trade_state");
				// 交易模式，1即时到账，2中介担保
				String trade_mode = resHandler.getParameter("trade_mode");

				// 商户订单号
				String out_trade_no = resHandler.getParameter("out_trade_no");
				// 财付通订单号
				String transaction_id = resHandler.getParameter("transaction_id");
				// 金额,以分为单位
				// String total_fee = resHandler.getParameter("total_fee");
				// 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
				// String discount = resHandler.getParameter("discount");

				// 支付结果信息，支付成功时为空
				String pay_info = queryRes.getParameter("pay_info");
				if (StringUtils.isNotEmpty(pay_info)) {
					logger.error(out_trade_no + "\n" + pay_info);
				}

				// 判断签名及结果
				if (queryRes.isTenpaySign() && "0".equals(retcode) && "0".equals(trade_state) && "1".equals(trade_mode)) {
					// System.out.println("id验证成功");

					// ------------------------------
					// 即时到账处理业务开始
					// ------------------------------

					// 处理数据库逻辑
					// 注意交易单不要重复处理
					// 注意判断返回金额

					TbShopOrder order = orderService.getBySn(out_trade_no);
					if (null != order && order.getPaymentStatus() == PaymentStatus.unpaid) {

						Map<String, String> paymentData = getNotifyData(request);
						// String OpenId = root.elementTextTrim("OpenId");
						// String IsSubscribe = root.elementTextTrim("IsSubscribe");
						order.setPaymentData(JacksonUtil.toJsonString(paymentData));
						// ---
						order.setPaymentMethod(PaymentMethod.WXPAY);
						order.setPaymentStatus(PaymentStatus.paid);
						order.setPaymentCode(transaction_id);
						orderService.paid(order);

						logger.error(out_trade_no + " 微信支付即时到账支付成功,付款单号：" + transaction_id);
						// 如果已订阅用户，微信消息提醒提醒openid
						if ("1".equals(paymentData.get("IsSubscribe"))) {
							String message = "{\"touser\":\"%1$s\",\"msgtype\":\"text\",\"text\":{\"content\":\"" +
									"/:share恭喜，支付成功！%n" +
									"订单号：%2$s%n" +
									"支付金额：%3$,.2f 元%n" +
									"支付单号：%4$s%n" +
									"\"}}";
							message = String.format(message, paymentData.get("OpenId"), out_trade_no, Long.valueOf(paymentData
									.get("total_fee")) / 100.00, transaction_id);
							WeixinUtil.sendCustomMessage(message);
						}

						// 给财付通系统发送成功信息，财付通系统收到此结果后不再进行后续通知
						resHandler.sendToCFT("success");
					}
					// ------------------------------
					// 即时到账处理业务完毕
					// ------------------------------
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

	// 原生支付回调查询订单信息
	@ResponseBody
	@RequestMapping(value = "/payment/wxpay_native_getpackage", method = RequestMethod.POST)
	public String wxpay_native_getpackage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 未使用
		return null;
	}

	// 微信维权通知URL
	@ResponseBody
	@RequestMapping(value = "/payment/wxpay_payfeedback", method = RequestMethod.POST)
	public String wxpay_payfeedback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String xmlString = "<xml><OpenId><![CDATA[oDF3iY9P32sK_5GgYiRkjsCo45bk]]></OpenId><AppId><![CDATA[wxf8b4f85f3a794e77]]></AppId>" +
		// "<TimeStamp>1393400471</TimeStamp><MsgType><![CDATA[request]]></MsgType><FeedBackId>7197417460812502768</FeedBackId>" +
		// "<TransId><![CDATA[1900000109201402143240185685]]></TransId><Reason><![CDATA[质量问题]]></Reason>" +
		// "<Solution><![CDATA[换货]]></Solution><ExtInfo><![CDATA[备注 12435321321]]></ExtInfo>" +
		// "<AppSignature><![CDATA[d60293982cc7c97a5a9d3383af761db763c07c86]]></AppSignature><SignMethod><![CDATA[sha1]]></SignMethod>" +
		// "<PicInfo><item><PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz/49ogibiahRNtOk37iaztwmdgFbyFS9FUrqfodiaUAmxr4hOP34C6R4nGgebMalKuY3H35riaZ5vtzJh25tp7vBUwWxw/0]]></PicUrl></item><item><PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz/49ogibiahRNtOk37iaztwmdgFbyFS9FUrqfn3y72eHKRSAwVz1PyIcUSjBrDzXAibTiaAdrTGb4eBFbib9ibFaSeic3OIg/0]]></PicUrl></item><item><PicUrl><![CDATA[]]></PicUrl></item><item><PicUrl><![CDATA[]]></PicUrl></item><item><PicUrl><![CDATA[]]></PicUrl></item></PicInfo></xml>";
		String xmlString = getPostData(request);
		// System.out.println("XML:" + xmlString);
		Document doc = DocumentHelper.parseText(xmlString);
		Element root = doc.getRootElement(); // 获取根节点

		String MsgType = root.elementTextTrim("MsgType");

		if ("request".equals(MsgType)) {
			// request 用户投诉
			String FeedBackId = root.elementTextTrim("FeedBackId");
			TbWxPayfeedback payfeedback = wxPayfeedbackService.get(FeedBackId);
			if (payfeedback == null) {
				payfeedback = new TbWxPayfeedback();
			}
			payfeedback = new TbWxPayfeedback();
			payfeedback.setStatus(FeedBackStatus.unconfirmed);
			payfeedback.setAppId(root.elementTextTrim("AppId"));
			payfeedback.setExtInfo(root.elementTextTrim("ExtInfo"));
			payfeedback.setFeedBackId(root.elementTextTrim("FeedBackId"));
			payfeedback.setOpenId(root.elementTextTrim("OpenId"));
			payfeedback.setReason(root.elementTextTrim("Reason"));
			payfeedback.setSolution(root.elementTextTrim("Solution"));
			payfeedback.setTimeStamp(Integer.valueOf(root.elementTextTrim("TimeStamp")));
			payfeedback.setTransId(root.elementTextTrim("TransId"));
			payfeedback.setRawXmlData(xmlString);
			Element PicInfo = root.element("PicInfo");
			if (PicInfo != null) {
				@SuppressWarnings("unchecked")
				List<Element> items = PicInfo.elements();
				for (int i = 0; i < items.size(); i++) {
					String name = "picUrl" + i;
					String value = items.get(i).elementTextTrim("PicUrl");
					try {
						BeanUtils.setProperty(payfeedback, name, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				wxPayfeedbackService.merge(payfeedback);
			} catch (Exception e) {
				logger.error("微信维权通知 request 保存失败" + e.getMessage());
				logger.error(xmlString);
			}
		} else if ("confirm".equals(MsgType)) {
			// confirm 用户确认消除投诉
			String FeedBackId = root.elementTextTrim("FeedBackId");
			TbWxPayfeedback payfeedback = wxPayfeedbackService.get(FeedBackId);
			if (null != payfeedback) {
				payfeedback.setStatus(FeedBackStatus.confirmed);
				payfeedback.setTimeStamp(Integer.valueOf(root.elementTextTrim("TimeStamp")));
				try {
					wxPayfeedbackService.update(payfeedback);
				} catch (Exception e) {
					logger.error("微信维权通知 confirm 保存失败" + e.getMessage());
					logger.error(xmlString);
				}
			}
		} else if ("reject".equals(MsgType)) {
			// TODO reject 用户拒绝消除投诉
		}

		return "success";
	}

	// 微信告警通知URL
	@ResponseBody
	@RequestMapping(value = "/payment/wxpay_warning", method = RequestMethod.POST)
	public String wxpay_warning(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String xmlString = getPostData(request);
		logger.error("wxpay_warning");
		logger.error(xmlString);
		try {
			String subject = "微信告警通知";
			String content = request.getServerName() + "微信告警通知：XML：" + xmlString;
			String sentto = PropertyHelper.getProperty("config.properties", "system.admin_email");
			SpringContextHolder.getBean(MailService.class).send(subject, content, sentto);
		} catch (Exception e) {
		}
		return "success";
	}

}
