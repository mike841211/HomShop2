package com.homlin.module.shop.plugin.weixin;

import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.util.CacheUtil;

public class WeixinConfig {

	// 凭证
	// public final static String AppId = "wx60df5c56f1697a43";
	public static String AppId = "wxed2b793254af26bf"; // 测试号

	// 密钥
	// public final static String AppSecret = "8c89c5f9e802fbfdc2a3afb144e81e9b";
	public static String AppSecret = "7392734a82bfa1c5cc6f28a5ac51c9eb"; // 测试号

	public static String Token = "";

	public static String access_token = null;

	public static void initConfig() {
		access_token = null;

		AppId = CacheUtil.getConfig(CacheConfigKeys.WEIXIN_APPID);
		if (AppId == null) {
			throw new RuntimeException("WEIXIN_APPID is not configured in database config!!!");
		}

		AppSecret = CacheUtil.getConfig(CacheConfigKeys.WEIXIN_APPSECRET);
		if (AppSecret == null) {
			throw new RuntimeException("WEIXIN_APPSECRET is not configured in database config!!!");
		}

		Token = CacheUtil.getConfig(CacheConfigKeys.WEIXIN_TOKEN);
		if (AppSecret == null) {
			throw new RuntimeException("WEIXIN_TOKEN is not configured in database config!!!");
		}
	}

	static {
		initConfig();
	}

}
