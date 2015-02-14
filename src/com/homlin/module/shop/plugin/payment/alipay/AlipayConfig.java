package com.homlin.module.shop.plugin.payment.alipay;

import java.util.Properties;

import com.homlin.utils.PropertyHelper;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {

	public static String seller_email = "";
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "";
	
	// 交易安全检验码，由数字和字母组成的32位字符串
	// 如果签名方式设置为“MD5”时，请设置该参数
	public static String key = "";
	
    // 商户的私钥
    // 如果签名方式设置为“0001”时，请设置该参数
	public static String private_key = "";

    // 支付宝的公钥
    // 如果签名方式设置为“0001”时，请设置该参数
	public static String ali_public_key = "";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持  utf-8
	public static String input_charset = "UTF-8";
	
	// 签名方式，选择项：0001(RSA)、MD5
	public static String sign_type = "MD5";
	// 无线的产品中，签名方式为rsa时，sign_type需赋值为0001而不是RSA

	// 是否启用
	public static boolean enabled = false;

	public static void initConfig() {
		Properties p = PropertyHelper.getPropertyFile("config.properties");

		enabled = Boolean.valueOf(p.getProperty("alipay.enabled"));
		if (!enabled) {
			return;
		}

		seller_email = p.getProperty("alipay.seller_email");
		if (seller_email == null) {
			throw new RuntimeException("alipay.seller_email property is not configured in config.properties!!!");
		}

		partner = p.getProperty("alipay.partner");
		if (partner == null) {
			throw new RuntimeException("alipay.partner property is not configured in config.properties!!!");
		}

		key = p.getProperty("alipay.key");
		if (key == null) {
			throw new RuntimeException("alipay.key property is not configured in config.properties!!!");
		}
	}

	static {
		initConfig();
	}

}
