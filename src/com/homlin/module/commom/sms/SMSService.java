package com.homlin.module.commom.sms;

import java.util.Map;

/**
 * 短信服务
 * 
 * @author Administrator
 * 
 */
public interface SMSService {

	int sendSMS(String mobile, String content) throws Exception;

	int sendSMS(String[] mobiles, String content) throws Exception;

	/**
	 * 测试服务配置
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	int testConfig(Map<String, Object> config) throws Exception;

	/**
	 * 返回值信文本息
	 * 
	 * @param result
	 * @return
	 * @throws Exception
	 */
	String getSendResultText(int result) throws Exception;
}
