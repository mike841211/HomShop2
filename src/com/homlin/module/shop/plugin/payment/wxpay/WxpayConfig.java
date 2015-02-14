package com.homlin.module.shop.plugin.payment.wxpay;

import java.util.Properties;

import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.PropertyHelper;

public class WxpayConfig {

	// 公众号身份标识
	public static String appId = "";

	// 公众平台 API(参考文档 API 接口部分)的权限获取所需密钥 Key，在使用所有公众平台 API 时，都需要先用它去换取 access_token，然后再进行调用。
	public static String appSecret = "";

	// 公众号支付请求中用于加密的密钥 Key，可验证商户唯一身份，PaySignKey 对应于支付场景中的 appKey 值。
	public static String paySignKey = "";

	// 财付通商户身份标识。
	public static String partnerId = "";

	// 财付通商户权限密钥 Key
	public static String partnerKey = "";

	// 是否启用
	public static boolean enabled = false;

	public static void initConfig() {
		Properties p = PropertyHelper.getPropertyFile("config.properties");

		enabled = Boolean.valueOf(p.getProperty("wxpay.enabled"));
		if (!enabled) {
			return;
		}

		// --

		appId = CacheUtil.getConfig(CacheConfigKeys.WEIXIN_APPID);
		if (appId == null) {
			throw new RuntimeException("WEIXIN_APPID is not configured in database config!!!");
		}

		appSecret = CacheUtil.getConfig(CacheConfigKeys.WEIXIN_APPSECRET);
		if (appSecret == null) {
			throw new RuntimeException("WEIXIN_APPSECRET is not configured in database config!!!");
		}

		// appId = p.getProperty("wxpay.appId");
		// if (appId == null) {
		// throw new RuntimeException("wxpay.appId property is not configured in config.properties!!!");
		// }
		//
		// appSecret = p.getProperty("wxpay.appSecret");
		// if (appSecret == null) {
		// throw new RuntimeException("wxpay.appSecret property is not configured in config.properties!!!");
		// }

		// --

		partnerId = p.getProperty("wxpay.partnerId");
		if (partnerId == null) {
			throw new RuntimeException("wxpay.partnerId property is not configured in config.properties!!!");
		}

		partnerKey = p.getProperty("wxpay.partnerKey");
		if (partnerKey == null) {
			throw new RuntimeException("wxpay.partnerKey property is not configured in config.properties!!!");
		}

		paySignKey = p.getProperty("wxpay.paySignKey");
		if (paySignKey == null) {
			throw new RuntimeException("wxpay.paySignKey property is not configured in config.properties!!!");
		}
	}

	static {
		initConfig();
	}

}
