package com.homlin.module.shop.plugin.weixin;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信回复关键字映射
 * 
 * Date:2014-4-28 下午04:17:49
 * 
 * @author Administrator
 * 
 */
public class KeywordMappingUtil {

	private static final Map<String, String> REPLY_MAPPINGS = new HashMap<String, String>() {
		{
			put("Q:帮助", "help");
			put("Q:HELP", "help");

			put("Q:新品", "query_product_new");
			put("Q:热销", "query_product_hot");
			put("Q:促销", "query_product_promotion");
			put("Q:推荐", "query_product_recomend");

			put("Q:积分", "query_member_score");
			put("Q:余额", "query_member_deposit");

			put("客服", "transfer_customer_service");
			put("小二", "transfer_customer_service");
		}
	};

	public static String get(String key) {
		return REPLY_MAPPINGS.get(key);
	}

}
