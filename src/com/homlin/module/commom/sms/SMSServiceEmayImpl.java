package com.homlin.module.commom.sms;

import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.emay.sdk.common.SDKErrCode;

/**
 * SMSServiceImpl实现类：亿美软通
 * 
 * @author Administrator
 * 
 */
@Service
public class SMSServiceEmayImpl implements SMSService {

	ResourceBundle bundle = PropertyResourceBundle.getBundle("smsconfig");

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public int sendSMS(String mobile, String content) throws Exception {
		return sendSMS(new String[] { mobile }, content);
	}

	/**
	 * <pre>
	 * 参数名称 说明
	 * mobiles 手机号码(群发为字符串数组推荐最多为200个手机号码或以内)
	 * content 短信内容(最多500个汉字或1000个纯英文，emay服务器程序能够自动分割；
	 * 亿美有多个通道为客户提供服务，所以分割原则采用最短字数的通道为分割短信长度的规则，请客户应用程序不要自己分割短信以免造成混乱)
	 * smsPriority 优先级(级别从1到5的正整数，数字越大优先级越高，越先被发送)
	 * </pre>
	 */
	@Override
	public int sendSMS(String[] mobiles, String content) throws Exception {
		int result = 0;
		if ("true".equalsIgnoreCase(bundle.getString("debug"))) {
			// 调试：短信发送，不发送
			System.err.println("模拟发送短信:" + StringUtils.join(mobiles, ", "));
			System.err.println(content);
		} else {
			result = EmaySMSClient.getClient().sendSMS(mobiles, content, "", 5);// 带扩展码;
			if (result != 0) {
				logger.error("短信发送失败：" + result);
			}
		}
		return result;
	}

	@Override
	public int testConfig(Map<String, Object> config) throws Exception {
		// TODO 测试配置
		return 0;
	}

	@Override
	public String getSendResultText(int result) throws Exception {
		String message = "";
		if (result == -1) {
			message = "发送信息失败（短信内容长度越界）";
		} else if (result == 0) {
			message = "发送成功";
		} else if (result == 17) {
			message = "发送信息失败（未激活序列号或序列号和KEY值不对，或账户没有余额等）";
		} else if (result == 101) {
			message = "客户端网络故障";
		} else if (result == SDKErrCode.SEND_TIME_OUT) {
			message = "由于客户端网络问题导致信息发送超时，该信息是否成功下发无法确定";
		} else if (result == 305) {
			message = "服务器端返回错误，错误的返回值（返回值不是数字字符串）";
		} else if (result == 307) {
			message = "目标电话号码不符合规则，电话号码必须是以0、1开头";
		} else if (result == 997) {
			message = "平台返回找不到超时的短信，该信息是否成功无法确定";
		} else if (result == -1107) {
			message = "服务序列号未激活";
		} else {
			message = "出现其他错误";
		}
		return message;
	}

}
