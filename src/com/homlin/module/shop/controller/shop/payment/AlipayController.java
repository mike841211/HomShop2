package com.homlin.module.shop.controller.shop.payment;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.controller.shop.member.BaseMemberController;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.module.shop.model.TbShopOrder.OrderStatus;
import com.homlin.module.shop.model.TbShopOrder.PaymentMethod;
import com.homlin.module.shop.model.TbShopOrder.PaymentStatus;
import com.homlin.module.shop.plugin.payment.alipay.AlipayConfig;
import com.homlin.module.shop.plugin.payment.alipay.util.AlipayNotify;
import com.homlin.module.shop.plugin.payment.alipay.util.AlipaySubmit;
import com.homlin.module.shop.service.impl.OrderServiceImpl;
import com.homlin.module.shop.util.CacheUtil;

@Controller
public class AlipayController extends BaseMemberController {

	@Autowired
	OrderServiceImpl orderService;

	@RequestMapping(value = "/payment/alipay", params = { "type=order", "way=mobile" })
	public String alipay(@RequestParam(value = "id", defaultValue = "") String id,
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

		// **************************
		// 支付宝网关地址
		String ALIPAY_GATEWAY_NEW = "http://wappaygw.alipay.com/service/rest.htm?";

		// //////////////////////////////////调用授权接口alipay.wap.trade.create.direct获取授权码token//////////////////////////////////////

		// 返回格式
		String format = "xml";
		// 必填，不需要修改

		// 返回格式
		String v = "2.0";
		// 必填，不需要修改

		// 请求号
		// String req_id = UtilDate.getOrderNum();
		String req_id = order.getSn();
		// 必填，须保证每次请求都是唯一

		// req_data详细信息

		// 服务器异步通知页面路径
		String notify_url = SHOP_URL + "/payment/alipay_notify.do";
		// 需http://格式的完整路径，不能加?id=123这类自定义参数

		// 页面跳转同步通知页面路径
		String call_back_url = SHOP_URL + "/payment/alipay_return_" + order.getId() + ".htm";
		// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

		// 操作中断返回地址
		String merchant_url = SHOP_URL;
		// 用户付款中途退出返回商户的地址。需http://格式的完整路径，不允许加?id=123这类自定义参数

		// 卖家支付宝帐户
		String seller_email = AlipayConfig.seller_email;
		// 必填

		// 商户订单号
		String out_trade_no = order.getSn();
		// 商户网站订单系统中唯一订单号，必填

		// 订单名称
		String subject = "付款订单_" + order.getSn() + " - " + CacheUtil.getConfig(CacheConfigKeys.SHOP_NAME);
		// 必填

		// 付款金额
		String total_fee = order.getUnpaidAmount().setScale(2).toString();
		// 必填

		// 请求业务参数详细
		String req_dataToken = "<direct_trade_create_req><notify_url>" + notify_url + "</notify_url><call_back_url>" + call_back_url + "</call_back_url><seller_account_name>" + seller_email + "</seller_account_name><out_trade_no>" + out_trade_no + "</out_trade_no><subject>" + subject + "</subject><total_fee>" + total_fee + "</total_fee><merchant_url>" + merchant_url + "</merchant_url></direct_trade_create_req>";
		// 必填

		// ////////////////////////////////////////////////////////////////////////////////

		// 把请求参数打包成数组
		Map<String, String> sParaTempToken = new HashMap<String, String>();
		sParaTempToken.put("service", "alipay.wap.trade.create.direct");
		sParaTempToken.put("partner", AlipayConfig.partner);
		sParaTempToken.put("_input_charset", AlipayConfig.input_charset);
		sParaTempToken.put("sec_id", AlipayConfig.sign_type);
		sParaTempToken.put("format", format);
		sParaTempToken.put("v", v);
		sParaTempToken.put("req_id", req_id);
		sParaTempToken.put("req_data", req_dataToken);

		// 建立请求
		String sHtmlTextToken = AlipaySubmit.buildRequest(ALIPAY_GATEWAY_NEW, "", "", sParaTempToken);
		// URLDECODE返回的信息
		sHtmlTextToken = URLDecoder.decode(sHtmlTextToken, AlipayConfig.input_charset);
		// 获取token
		String request_token = AlipaySubmit.getRequestToken(sHtmlTextToken);
		// out.println(request_token);

		// //////////////////////////////////根据授权码token调用交易接口alipay.wap.auth.authAndExecute//////////////////////////////////////

		// 业务详细
		String req_data = "<auth_and_execute_req><request_token>" + request_token + "</request_token></auth_and_execute_req>";
		// 必填

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("sec_id", AlipayConfig.sign_type);
		sParaTemp.put("format", format);
		sParaTemp.put("v", v);
		sParaTemp.put("req_data", req_data);

		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(ALIPAY_GATEWAY_NEW, sParaTemp, "get", "确认");
		// out.println(sHtmlText);

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.println(sHtmlText);
		pw.flush();
		pw.close();
		return null;

	}

	@RequestMapping(value = "/payment/alipay_return_{id}")
	public String alipay_return(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable String id)
			throws Exception {
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			params.put(name, StringUtils.join(values, ","));
		}

		// 计算得出通知验证结果
		boolean verify_result = AlipayNotify.verifyReturn(params);

		model.addAttribute("paymentType", PaymentMethod.ALIPAY);
		model.addAttribute("id", id);
		if (verify_result) {// 验证成功

			// // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// String out_trade_no = params.get("out_trade_no"); // 商户订单号
			// String trade_no = params.get("trade_no"); // 支付宝交易号
			String result = params.get("result"); // 交易状态

			// 请在这里加上商户的业务逻辑程序代码
			if (result.equals("success")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序
				// pw.println("支付宝付款请求成功！");
				// pw.println("<br />订单号：" + out_trade_no);
				// pw.println("<br />支付宝交易号：" + trade_no);
			}

			// 该页面可做页面美工编辑
			// trace("验证成功<br />");
		} else {
			// // 该页面可做页面美工编辑
			// response.setContentType("text/html;charset=UTF-8");
			// PrintWriter pw = response.getWriter();
			// pw.println("验证失败");
			// pw.flush();
			// pw.close();
			// // trace("验证失败");
			model.addAttribute("error", true);
			logger.error("alipay notify verify fail");
		}

		return getTemplatePath() + "/order/pay_return";
	}

	@RequestMapping(value = "/payment/alipay_notify", method = RequestMethod.POST)
	public String alipay_notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			params.put(name, StringUtils.join(values, ","));
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//

		// 解密（如果是RSA签名需要解密，如果是MD5签名则下面一行清注释掉）
		// Map<String, String> decrypt_params = AlipayNotify.decrypt(params); // RSA签名
		Map<String, String> decrypt_params = params; // MD5签名

		// XML解析notify_data数据
		Document doc_notify_data = DocumentHelper.parseText(decrypt_params.get("notify_data"));

		// 商户订单号
		String out_trade_no = doc_notify_data.selectSingleNode("//notify/out_trade_no").getText();

		// 支付宝交易号
		String trade_no = doc_notify_data.selectSingleNode("//notify/trade_no").getText();

		// 交易状态
		String trade_status = doc_notify_data.selectSingleNode("//notify/trade_status").getText();

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if (AlipayNotify.verifyNotify(params)) {// 验证成功
			PrintWriter pw = response.getWriter();
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			TbShopOrder order = orderService.getBySn(out_trade_no);
			if (trade_status.equals("TRADE_FINISHED")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 该种交易状态只在两种情况下出现
				// 1、开通了普通即时到账，买家付款成功后。
				// 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。

				if (null != order && order.getPaymentStatus() == PaymentStatus.unpaid) {
					order.setPaymentData(decrypt_params.get("notify_data"));
					// ---
					order.setPaymentMethod(PaymentMethod.ALIPAY);
					order.setPaymentStatus(PaymentStatus.paid);
					order.setPaymentCode(trade_no);
					orderService.paid(order);

					logger.error(out_trade_no + " 支付宝即时到账支付成功1,付款单号：" + trade_no);
				}
				// 给系统发送成功信息，系统收到此结果后不再进行后续通知
				pw.println("success"); // 请不要修改或删除

			} else if (trade_status.equals("TRADE_SUCCESS")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。

				if (null != order && order.getPaymentStatus() == PaymentStatus.unpaid) {
					order.setPaymentData(decrypt_params.get("notify_data"));
					// ---
					order.setPaymentMethod(PaymentMethod.ALIPAY);
					order.setPaymentStatus(PaymentStatus.paid);
					order.setPaymentCode(trade_no);
					orderService.paid(order);

					logger.error(out_trade_no + " 支付宝即时到账支付成功2,付款单号：" + trade_no);
				}
				// 给系统发送成功信息，系统收到此结果后不再进行后续通知
				pw.println("success"); // 请不要修改或删除

			}

			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			// ////////////////////////////////////////////////////////////////////////////////////////
			pw.flush();
			pw.close();
		} else {// 验证失败
			logger.error(params.toString());
			logger.error("alipay notify verify fail");
		}
		return null;
	}

}
