package com.homlin.module.shop.plugin.payment.unionpay.upop;

import java.util.Properties;

import com.homlin.utils.PropertyHelper;

/**
 * 名称：支付配置类
 * 功能：配置类
 * 类属性：公共类
 * 版本：1.0
 * 日期：2011-03-11
 * 作者：中国银联UPOP团队
 * 版权：中国银联
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class UpopConfig {

	// 版本号
	public final static String version = "1.0.0";

	// 编码方式
	public final static String charset = "UTF-8";

	// 基础网址（请按相应环境切换）

	/* 测试环境 */
	// private static String UPOP_BASE_URL = "http://58.246.226.99/UpopWeb/api/";

	/* PM环境（准生产环境） */
	private static String UPOP_BASE_URL = "https://www.epay.lxdns.com/UpopWeb/api/";

	/* 生产环境 */
	// private static String UPOP_BASE_URL = "https://unionpaysecure.com/api/";

	// 支付网址
	public static String gateWay = UPOP_BASE_URL + "Pay.action";

	// 后续交易网址
	public static String backStagegateWay = UPOP_BASE_URL + "BSPay.action";

	// 查询网址
	public static String queryUrl = UPOP_BASE_URL + "Query.action";

	// 商户代码
	public static String merCode = "105550149170027";

	// 商户名称
	public static String merName = "商户名称";

	// public final static String merFrontEndUrl = "http://www.wdp.com/payment/unionpay_return.htm";

	// public final static String merBackEndUrl = "http://www.wdp.com/payment/unionpay_notify.do";
	// public final static String merBackEndUrl = "http://qzsjxxkj.xicp.net:51907/payment/unionpay_notify.do";

	// 加密方式
	public final static String signType = "MD5";
	public final static String signType_SHA1withRSA = "SHA1withRSA";

	// 商城密匙，需要和银联商户网站上配置的一样
	public static String securityKey = "88888888";

	// 签名
	public final static String signature = "signature";
	public final static String signMethod = "signMethod";

	// 组装消费请求包
	public final static String[] reqVo = new String[] {
			"version",
			"charset",
			"transType",
			"origQid",
			"merId",
			"merAbbr",
			"acqCode",
			"merCode",
			"commodityUrl",
			"commodityName",
			"commodityUnitPrice",
			"commodityQuantity",
			"commodityDiscount",
			"transferFee",
			"orderNumber",
			"orderAmount",
			"orderCurrency",
			"orderTime",
			"customerIp",
			"customerName",
			"defaultPayType",
			"defaultBankNumber",
			"transTimeout",
			"frontEndUrl",
			"backEndUrl",
			"merReserved"
	};

	public final static String[] notifyVo = new String[] {
			"charset",
			"cupReserved",
			"exchangeDate",
			"exchangeRate",
			"merAbbr",
			"merId",
			"orderAmount",
			"orderCurrency",
			"orderNumber",
			"qid",
			"respCode",
			"respMsg",
			"respTime",
			"settleAmount",
			"settleCurrency",
			"settleDate",
			"traceNumber",
			"traceTime",
			"transType",
			"version"
	};

	public final static String[] queryVo = new String[] {
			"version",
			"charset",
			"transType",
			"merId",
			"orderNumber",
			"orderTime",
			"merReserved"
	};

	// ---------------

	// 是否启用
	public static boolean enabled = false;

	public static void initConfig() {
		Properties p = PropertyHelper.getPropertyFile("config.properties");

		enabled = Boolean.valueOf(p.getProperty("upop.enabled"));
		if (!enabled) {
			return;
		}

		Boolean debug = true;
		try {
			debug = Boolean.valueOf(p.getProperty("upop.debug"));
		} catch (Exception e) {
		}

		if (debug) {

			UPOP_BASE_URL = "http://58.246.226.99/UpopWeb/api/";
			// 支付网址
			gateWay = UPOP_BASE_URL + "Pay.action";
			// 后续交易网址
			backStagegateWay = UPOP_BASE_URL + "BSPay.action";
			// 查询网址
			queryUrl = UPOP_BASE_URL + "Query.action";

			merName = "测试-商户名称";
			merCode = "105550149170027";
			securityKey = "88888888";

		} else {

			UPOP_BASE_URL = "https://unionpaysecure.com/api/";
			// 支付网址
			gateWay = UPOP_BASE_URL + "Pay.action";
			// 后续交易网址
			backStagegateWay = UPOP_BASE_URL + "BSPay.action";
			// 查询网址
			queryUrl = UPOP_BASE_URL + "Query.action";

			merName = p.getProperty("upop.merName");
			if (merName == null) {
				throw new RuntimeException("upop.merName property is not configured in config.properties!!!");
			}

			merCode = p.getProperty("upop.merCode");
			if (merCode == null) {
				throw new RuntimeException("upop.merCode property is not configured in config.properties!!!");
			}

			securityKey = p.getProperty("upop.securityKey");
			if (securityKey == null) {
				throw new RuntimeException("upop.securityKey property is not configured in config.properties!!!");
			}
		}
	}

	static {
		initConfig();
	}

}
