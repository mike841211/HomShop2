package com.homlin.module.shop.plugin.payment.wxpay.web;

public class Example {
	public static void main(String args[]) {
		// String appId = " wxf8b4f85f3a794e77";
		// String appSecret = " 4333d426b8d01a3fe64d53f36892df";
		// String paySignKey = "2Wozy2aksie1puXUBpWD8oZxiD1DfQuEaiC7KcRATv1Ino3mdopKaPGQQ7TtkNySuAmCaDCrw4xhPY5qKTBl7Fzm0RgR3c0WaVYIXZARsxzHV2x7iwPPzOz94dnwPWSn";
		// String partnerId = " 1900000109";
		// String partnerKey = " 8934e7d15453e97507ef794cf7b0519d";
		try {
			WxPayHelper wxPayHelper = new WxPayHelper();
			// 先设置基本信息
			wxPayHelper.SetAppId("wxf8b4f85f3a794e77");
			wxPayHelper
					.SetAppKey("2Wozy2aksie1puXUBpWD8oZxiD1DfQuEaiC7KcRATv1Ino3mdopKaPGQQ7TtkNySuAmCaDCrw4xhPY5qKTBl7Fzm0RgR3c0WaVYIXZARsxzHV2x7iwPPzOz94dnwPWSn");
			wxPayHelper.SetParameter("partner", "1900000109");
			wxPayHelper.SetPartnerKey("8934e7d15453e97507ef794cf7b0519d");
			wxPayHelper.SetSignType("sha1");
			// 设置请求package信息
			wxPayHelper.SetParameter("bank_type", "WX");
			wxPayHelper.SetParameter("body", "test");
			wxPayHelper.SetParameter("out_trade_no", CommonUtil.CreateNoncestr());
			wxPayHelper.SetParameter("total_fee", "1");
			wxPayHelper.SetParameter("fee_type", "1");
			wxPayHelper.SetParameter("notify_url", "htttp://www.baidu.com");
			wxPayHelper.SetParameter("spbill_create_ip", "127.0.0.1");
			wxPayHelper.SetParameter("input_charset", "GBK");

			// System.out.println("生成app支付package:");
			// System.out.println(wxPayHelper.CreateAppPackage("test"));
			System.out.println("生成jsapi支付package:");
			System.out.println(wxPayHelper.CreateBizPackage());
			// System.out.println("生成原生支付url:");
			// System.out.println(wxPayHelper.CreateNativeUrl("abc"));
			// System.out.println("生成原生支付package:");
			// System.out.println(wxPayHelper.CreateNativePackage("0", "ok"));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
