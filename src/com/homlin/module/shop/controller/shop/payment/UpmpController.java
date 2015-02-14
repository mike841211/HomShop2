package com.homlin.module.shop.controller.shop.payment;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sun.misc.BASE64Encoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.controller.shop.member.BaseMemberController;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.module.shop.model.TbShopOrder.OrderStatus;
import com.homlin.module.shop.model.TbShopOrder.PaymentMethod;
import com.homlin.module.shop.model.TbShopOrder.PaymentStatus;
import com.homlin.module.shop.plugin.payment.unionpay.upmp.conf.UpmpConfig;
import com.homlin.module.shop.plugin.payment.unionpay.upmp.service.UpmpService;
import com.homlin.module.shop.service.impl.OrderServiceImpl;
import com.homlin.module.shop.util.CacheUtil;

@Controller
public class UpmpController extends BaseMemberController {

	private BASE64Encoder base64Encoder = new BASE64Encoder();

	@Autowired
	private OrderServiceImpl orderService;

	private String moneyToFenString(BigDecimal money) {
		return money.movePointRight(2).setScale(0).toString();
	}

	@RequestMapping(value = "/payment/upmp", params = { "type=order" })
	public String upmp(@RequestParam(value = "id", defaultValue = "") String id,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

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

		// ===============================
		// final String SHOP_URL = CacheUtil.getConfig(CacheConfigKeys.SHOP_URL);
		final String SHOP_NAME = CacheUtil.getConfig(CacheConfigKeys.SHOP_NAME);

		// 请求要素
		Map<String, String> req = new HashMap<String, String>();
		req.put("version", UpmpConfig.VERSION);// 版本号
		req.put("charset", UpmpConfig.CHARSET);// 字符编码
		req.put("transType", "01");// 交易类型
		req.put("merId", UpmpConfig.MER_ID);// 商户代码
		req.put("backEndUrl", UpmpConfig.MER_BACK_END_URL);// 通知URL
		req.put("frontEndUrl", UpmpConfig.MER_FRONT_END_URL);// 前台通知URL(可选)
		req.put("orderDescription", SHOP_NAME + "_订单付款_" + order.getSn());// 订单描述(可选)
		req.put("orderTime", order.getCreateDate().replaceAll("-|\\s|:", ""));// 交易开始日期时间yyyyMMddHHmmss
		req.put("orderTimeout", "");// 订单超时时间yyyyMMddHHmmss(可选)
		req.put("orderNumber", order.getSn());// 订单号(商户根据自己需要生成订单号)
		req.put("orderAmount", moneyToFenString(order.getUnpaidAmount()));// 订单金额
		req.put("orderCurrency", "156");// 交易币种(可选)
		req.put("reqReserved", "");// 请求方保留域(可选，用于透传商户信息)

		// // 保留域填充方法
		// Map<String, String> merReservedMap = new HashMap<String, String>();
		// merReservedMap.put("test", "test");
		// req.put("merReserved", UpmpService.buildReserved(merReservedMap));// 商户保留域(可选)

		Map<String, String> resp = new HashMap<String, String>();
		boolean validResp = UpmpService.trade(req, resp);

		// 商户的业务逻辑
		if (validResp) {
			// 服务器应答签名验证成功
			String paydata = "tn=" + resp.get("tn");
			paydata += ", resultURL=" + URLEncoder.encode(UpmpConfig.MER_FRONT_END_URL + "?id=" + id + "&result=", UpmpConfig.CHARSET);
			paydata += ", usetestmode=" + UpmpConfig.debug; // 是否测试环境
			paydata = base64Encoder.encodeBuffer(paydata.getBytes());
			paydata = URLEncoder.encode(paydata, UpmpConfig.CHARSET);
			request.setAttribute("paydata", paydata);
		} else {
			// 服务器应答签名验证失败
			logger.error(resp.toString());
		}

		return getTemplatePath("/order/pay_upmp");
	}

	@RequestMapping(value = "/payment/upmp_return")
	public String upmp_return(String id, String result, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("id", id);
		request.setAttribute("paymentType", PaymentMethod.UNIONPAY);
		if ("0".equals(result)) { // 支付成功
		} else if ("1".equals(result)) { // 支付失败
			request.setAttribute("error", true);
			request.setAttribute("errorMessage", "支付失败");
		} else if ("-1".equals(result)) { // 支付取消
			request.setAttribute("error", true);
			request.setAttribute("errorMessage", "支付取消");
		}
		return getTemplatePath() + "/order/pay_return";
	}

	// 调试时注意域名拦截器
	@RequestMapping(value = "/payment/upmp_notify", method = RequestMethod.POST)
	public void upmp_notify(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

		// String _reportString = "";
		// 获取银联POST过来异步通知信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
			// _reportString += "&" + name + "=" + valueStr;
		}
		// System.out.println(_reportString.substring(1));
		// System.out.println(new ObjectMapper().writeValueAsString(params));

		if (UpmpService.verifySignature(params)) {// 服务器签名验证成功
			// 获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
			String transStatus = request.getParameter("transStatus");// 交易状态
			// 请在这里加上商户的业务逻辑程序代码
			if ("00".equals(transStatus)) {
				// 交易处理成功
				String orderNumber = request.getParameter("orderNumber"); // 商户订单号
				String qn = request.getParameter("qn"); // 银联在线支付交易号(查询流水号)

				TbShopOrder order = orderService.getBySn(orderNumber);
				if (null != order && order.getPaymentStatus() == PaymentStatus.unpaid) {
					order.setPaymentMethod(PaymentMethod.UNIONPAY);
					order.setPaymentStatus(PaymentStatus.paid);
					order.setPaymentCode(qn);
					try {
						orderService.paid(order);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("UNIONPAY支付成功，但是更新订单状态失败:" + new ObjectMapper().writeValueAsString(params));
					}
				} else {
					logger.error("交易处理失败，没有匹配订单:" + new ObjectMapper().writeValueAsString(params));
				}
			} else {
				logger.error("交易处理失败:" + new ObjectMapper().writeValueAsString(params));
			}

			// response.setStatus(HttpServletResponse.SC_OK);
		} else { // 服务器签名验证失败
			logger.error("验证失败:" + new ObjectMapper().writeValueAsString(params));
		}
	}

}
