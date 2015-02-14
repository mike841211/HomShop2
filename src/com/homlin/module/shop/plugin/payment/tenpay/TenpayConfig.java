package com.homlin.module.shop.plugin.payment.tenpay;

import java.util.Properties;

import com.homlin.utils.PropertyHelper;

public class TenpayConfig {

	// 商户号：1900000113
	// 商户名称：自助商户测试帐户
	// 密钥：e82573dc7e6136ba414f2e2affbe39fa

	// 商户号
	public static String partner = "_1900000113";

	// 密钥
	public static String key = "_e82573dc7e6136ba414f2e2affbe39fa";

	// 是否启用
	public static boolean enabled = false;

	public static void initConfig() {
		Properties p = PropertyHelper.getPropertyFile("config.properties");

		enabled = Boolean.valueOf(p.getProperty("tenpay.enabled"));
		if (!enabled) {
			return;
		}

		partner = p.getProperty("tenpay.partner");
		if (partner == null) {
			throw new RuntimeException("tenpay.partner property is not configured in config.properties!!!");
		}

		key = p.getProperty("tenpay.key");
		if (key == null) {
			throw new RuntimeException("tenpay.key property is not configured in config.properties!!!");
		}
	}

	static {
		initConfig();
	}

}
