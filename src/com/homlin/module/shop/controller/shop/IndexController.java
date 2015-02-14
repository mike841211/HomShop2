package com.homlin.module.shop.controller.shop;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homlin.module.shop.constants.ClientType;
import com.homlin.module.shop.service.AdvertService;
import com.homlin.module.shop.util.CacheUtil;

@Controller
public class IndexController extends BaseShopController {

	@Autowired
	AdvertService advertService;

	@RequestMapping(value = "/index")
	public String index(Model model) {
		String TEMPLATE_INDEX = "TEMPLATE_MOBILE_INDEX";
		if (getClientType() == ClientType.pc) {
			TEMPLATE_INDEX = "TEMPLATE_PC_INDEX";
		}

		String index = CacheUtil.getConfig(TEMPLATE_INDEX);
		if (StringUtils.isBlank(index)) {
			index = "index";
		}
		return getTemplatePath() + "/" + index;
	}

	@RequestMapping(value = "/member/index")
	public String member_index() {
		return getTemplatePath() + "/member/index";
	}

	// @ResponseBody
	// @RequestMapping(value = "/test")
	// public String test() throws Exception {
	// return test_CreateBizPackage();
	// }
	//
	// @SuppressWarnings("deprecation")
	// private String test_delivernotify() {
	//
	// WeixinConfig.AppId = "wxf13c857d6ac62f3e";
	// WeixinConfig.AppSecret = "0ae657f6a984f70990bb0544fcf6befc";
	// // WeixinConfig.access_token = null;
	//
	// WxpayConfig.appId = "wxf13c857d6ac62f3e";
	// WxpayConfig.appSecret = "0ae657f6a984f70990bb0544fcf6befc";
	// WxpayConfig.partnerId = "1218611701";
	// WxpayConfig.partnerKey = "3c19aaeaf8fc702a9664acf662b48732";
	// WxpayConfig.paySignKey = "ms9E2cjSCsYCDronCeTW693Iq4mga1Br0krn1DsSkfNEBxGDSYjuF15DWTnLq9vYa3ndWc6NAObtz0hR4yQnKR4sWds4pXkFDbaL5ON7MhOMUGr4LJrspUXdrZ6ExH5f";
	//
	// // 通知发货
	//
	// // 准备jsapi参数
	// WxPayHelper wxPayHelper = new WxPayHelper();
	//
	// // 先设置基本信息
	// wxPayHelper.SetAppId(WxpayConfig.appId);
	// // wxPayHelper.SetAppKey(WxpayConfig.appSecret);
	// wxPayHelper.SetAppKey(WxpayConfig.paySignKey);
	// wxPayHelper.SetPartnerKey(WxpayConfig.partnerKey);
	// wxPayHelper.SetSignType("sha1");
	//
	// HashMap<String, String> data = new HashMap<String, String>();
	// data.put("appid", "wxf13c857d6ac62f3e");
	// data.put("openid", "oBZG-uAxgUR_EJTc3Xkwb5JmPYww");
	// data.put("transid", "1218611701201404153177581595");
	// data.put("out_trade_no", "201402011");
	// data.put("deliver_timestamp", String.valueOf(new Date().getTime() / 1000));
	// data.put("deliver_status", "1");
	// try {
	// data.put("deliver_msg", URLEncoder.encode("测试", "UTF-8"));
	// } catch (UnsupportedEncodingException e2) {
	// e2.printStackTrace();
	// }
	// // --
	// try {
	// data.put("app_signature", wxPayHelper.GetBizSign(data));
	// } catch (SDKRuntimeException e1) {
	// e1.printStackTrace();
	// }
	// data.put("sign_method", "sha1");
	//
	// try {
	// WeixinUtil.delivernotify(JacksonUtil.toJsonString(data));
	// } catch (Exception e) {
	// logger.error("微信发货通知发送失败，通知服务器失败！\n" + e.getMessage());
	// return ("微信发货通知发送失败，通知服务器失败！\n" + e.getMessage());
	// }
	//
	// return new Date().toLocaleString();
	// }
	//
	// private String test_CreateBizPackage() throws Exception {
	//
	// WeixinConfig.AppId = "wxf13c857d6ac62f3e";
	// WeixinConfig.AppSecret = "0ae657f6a984f70990bb0544fcf6befc";
	// // WeixinConfig.access_token = null;
	//
	// WxpayConfig.appId = "wxf13c857d6ac62f3e";
	// WxpayConfig.appSecret = "0ae657f6a984f70990bb0544fcf6befc";
	// WxpayConfig.partnerId = "1218611701";
	// WxpayConfig.partnerKey = "3c19aaeaf8fc702a9664acf662b48732";
	// WxpayConfig.paySignKey = "ms9E2cjSCsYCDronCeTW693Iq4mga1Br0krn1DsSkfNEBxGDSYjuF15DWTnLq9vYa3ndWc6NAObtz0hR4yQnKR4sWds4pXkFDbaL5ON7MhOMUGr4LJrspUXdrZ6ExH5f";
	//
	// // 通知发货
	//
	// final String SHOP_URL = "http://m.gudusm.net";
	// final String SHOP_NAME = "圣炎官方商城";
	//
	// // 准备jsapi参数
	// WxPayHelper wxPayHelper = new WxPayHelper();
	//
	// // 先设置基本信息
	// wxPayHelper.SetAppId(WxpayConfig.appId);
	// wxPayHelper.SetAppKey(WxpayConfig.paySignKey);
	// wxPayHelper.SetPartnerKey(WxpayConfig.partnerKey);
	// wxPayHelper.SetSignType("sha1");
	//
	// // 设置请求package信息
	// wxPayHelper.SetParameter("bank_type", "WX");
	// // wxPayHelper.SetParameter("body", CacheUtil.getConfig(CacheConfigKeys.SHOP_NAME) + "_订单付款_201402123");// MD5签名编码改成UTF-8应该就可以了！
	// wxPayHelper.SetParameter("body", "商品订单付款_201402003 - " + SHOP_NAME);// MD5签名编码改成UTF-8应该就可以了！
	// wxPayHelper.SetParameter("partner", WxpayConfig.partnerId);
	// wxPayHelper.SetParameter("out_trade_no", "201402003");
	// wxPayHelper.SetParameter("total_fee", "1");
	// wxPayHelper.SetParameter("fee_type", "1");
	// wxPayHelper.SetParameter("notify_url", SHOP_URL + "/payment/wxpay_notify.do");
	// wxPayHelper.SetParameter("spbill_create_ip", IPUtil.getRemoteIP(getRequest()));
	// wxPayHelper.SetParameter("input_charset", "UTF-8");
	//
	// // ==========
	//
	// // System.out.println("生成app支付package:");
	// // System.out.println(wxPayHelper.CreateAppPackage("test"));
	// // System.out.println("生成jsapi支付package:");
	// String BrandWCPayRequestPackage = wxPayHelper.CreateBizPackage();
	// // System.out.println(BrandWCPayRequestPackage);
	// // payments.put("微信支付", "WXPAY");
	// // request.setAttribute("BrandWCPayRequestPackage", BrandWCPayRequestPackage);
	// // model.addAttribute("BrandWCPayRequestPackage", BrandWCPayRequestPackage);
	// Util.copyToClipboard(BrandWCPayRequestPackage);
	//
	// return BrandWCPayRequestPackage;
	// }

}
