package com.homlin.module.shop.plugin.weixin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.plugin.weixin.exception.AccessTokenErrorException;
import com.homlin.module.shop.plugin.weixin.exception.WeixinAPIException;
import com.homlin.utils.JacksonUtil;

public class WeixinUtil {

	public static final String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="; // 创建自定义菜单
	public static final String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="; // 查询自定义菜单
	public static final String GROUPS_GET_URL = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token="; // 查询所有分组
	public static final String GROUPS_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token="; // 创建分组
	public static final String GROUPS_UPDATE_URL = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token="; // 修改分组名
	public static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?lang=zh_CN&access_token="; // 获取用户基本信息
	public static final String GROUPS_GETID_URL = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token="; // 查询用户所在分组
	public static final String GROUPS_MEMBERS_UPDATE_URL = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token="; // 移动用户分组
	public static final String MESSAGE_CUSTOM_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="; // 发送消息
	public static final String USER_GET_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="; // 获取关注者列表

	// 二维码
	public static final String QRCODE_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="; // 创建二维码ticket
	public static final String SHOWQRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="; // 通过ticket换取二维码

	// 微信支付
	public static final String PAY_DELIVERNOTIFY_URL = "https://api.weixin.qq.com/pay/delivernotify?access_token="; // 微信支付发货通知
	public static final String PAYFEEDBACK_UPDATE_URL = "https://api.weixin.qq.com/payfeedback/update?access_token="; // 标记客户的投诉处理状态

	// [[ 返回消息 ]]

	static final Map<Integer, String> ErrorMsg = new HashMap<Integer, String>() {
		{
			put(-1, "系统繁忙");
			put(0, "请求成功");
			put(40001, "获取access_token时AppSecret错误，或者access_token无效");
			put(40002, "不合法的凭证类型");
			put(40003, "不合法的OpenID");
			put(40004, "不合法的媒体文件类型");
			put(40005, "不合法的文件类型");
			put(40006, "不合法的文件大小");
			put(40007, "不合法的媒体文件id");
			put(40008, "不合法的消息类型");
			put(40009, "不合法的图片文件大小");
			put(40010, "不合法的语音文件大小");
			put(40011, "不合法的视频文件大小");
			put(40012, "不合法的缩略图文件大小");
			put(40013, "不合法的APPID");
			put(40014, "不合法的access_token");
			put(40015, "不合法的菜单类型");
			put(40016, "不合法的按钮个数");
			put(40017, "不合法的按钮个数");
			put(40018, "不合法的按钮名字长度");
			put(40019, "不合法的按钮KEY长度");
			put(40020, "不合法的按钮URL长度");
			put(40021, "不合法的菜单版本号");
			put(40022, "不合法的子菜单级数");
			put(40023, "不合法的子菜单按钮个数");
			put(40024, "不合法的子菜单按钮类型");
			put(40025, "不合法的子菜单按钮名字长度");
			put(40026, "不合法的子菜单按钮KEY长度");
			put(40027, "不合法的子菜单按钮URL长度");
			put(40028, "不合法的自定义菜单使用用户");
			put(40029, "不合法的oauth_code");
			put(40030, "不合法的refresh_token");
			put(40031, "不合法的openid列表");
			put(40032, "不合法的openid列表长度");
			put(40033, "不合法的请求字符，不能包含\\uxxxx格式的字符");
			put(40035, "不合法的参数");
			put(40038, "不合法的请求格式");
			put(40039, "不合法的URL长度");
			put(40050, "不合法的分组id");
			put(40051, "分组名字不合法");
			put(41001, "缺少access_token参数");
			put(41002, "缺少appid参数");
			put(41003, "缺少refresh_token参数");
			put(41004, "缺少secret参数");
			put(41005, "缺少多媒体文件数据");
			put(41006, "缺少media_id参数");
			put(41007, "缺少子菜单数据");
			put(41008, "缺少oauth code");
			put(41009, "缺少openid");
			put(42001, "access_token超时");
			put(42002, "refresh_token超时");
			put(42003, "oauth_code超时");
			put(43001, "需要GET请求");
			put(43002, "需要POST请求");
			put(43003, "需要HTTPS请求");
			put(43004, "需要接收者关注");
			put(43005, "需要好友关系");
			put(44001, "多媒体文件为空");
			put(44002, "POST的数据包为空");
			put(44003, "图文消息内容为空");
			put(44004, "文本消息内容为空");
			put(45001, "多媒体文件大小超过限制");
			put(45002, "消息内容超过限制");
			put(45003, "标题字段超过限制");
			put(45004, "描述字段超过限制");
			put(45005, "链接字段超过限制");
			put(45006, "图片链接字段超过限制");
			put(45007, "语音播放时间超过限制");
			put(45008, "图文消息超过限制");
			put(45009, "接口调用超过限制");
			put(45010, "创建菜单个数超过限制");
			put(45015, "回复时间超过限制");
			put(45016, "系统分组，不允许修改");
			put(45017, "分组名字过长");
			put(45018, "分组数量超过上限");
			put(46001, "不存在媒体数据");
			put(46002, "不存在的菜单版本");
			put(46003, "不存在的菜单数据");
			put(46004, "不存在的用户");
			put(47001, "解析JSON/XML内容错误");
			put(48001, "api功能未授权");
			put(49004, "not match signature");
			put(50001, "用户未授权该api");
		}
	};

	public static String getErrorMsg(Integer errcode) {
		String message = ErrorMsg.get(errcode);
		// if (null == message) {
		// message = "错误代码：" + errcode;
		// // return errcode.toString();
		// }
		return message;
	}

	// ]] 返回消息 ]]

	// [[ 微信验证方法 ]]

	/**
	 * Function:微信验证方法
	 * 
	 * @param signature
	 *            微信加密签名
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机数
	 * @param echostr
	 *            随机字符串
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {

		// 将获取到的参数放入数组
		String[] ArrTmp = { WeixinConfig.Token, timestamp, nonce };
		// 按微信提供的方法，对数据内容进行排序
		Arrays.sort(ArrTmp);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ArrTmp.length; i++) {
			sb.append(ArrTmp[i]);
		}
		// 对排序后的字符串进行SHA-1加密
		String pwd = SHAEncrypt(sb.toString());
		if (pwd.equals(signature)) {
			return true;
		} else {
			System.err.println("微信平台签名消息验证失败！");
		}
		return false;
	}

	/**
	 * 用SHA-1算法加密字符串并返回16进制串
	 * 
	 * @param strSrc
	 * @return
	 */
	private static String SHAEncrypt(String strSrc) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(bt);
			strDes = bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("SHAEncrypt错误");
			return null;
		}
		return strDes;
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	// ]] 微信验证方法 ]]

	// [[ execute ]]

	// 从输入流读取post参数
	public static String readStreamParameter(ServletInputStream in) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}

	public static String doGet(String urlString) {
		try {
			URL url = new URL(urlString);
			InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String json = br.readLine();
			br.close();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doPost(String url, String data) {

		// 发送http请求
		try {
			// // 强制设置可信任证书
			// Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
			// Protocol.registerProtocol("https", myhttps);

			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod(url);
			RequestEntity requestEntity = new StringRequestEntity(data, null, "UTF-8");
			post.setRequestEntity(requestEntity);
			// post.setRequestBody(data);
			// post.getParams().setContentCharset("UTF-8");

			client.executeMethod(post);
			String respStr = post.getResponseBodyAsString();
			return respStr;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String execute(String url, String data) throws WeixinAPIException {
		String json = null;
		if (data == null) {
			json = doGet(url);
		} else {
			json = doPost(url, data);
		}
		// System.out.println(json);
		if (json != null) {
			JsonNode response = JacksonUtil.toJsonNode(json);
			if (null != response.get("errcode")) {
				int errcode = response.get("errcode").asInt();
				if (errcode != 0) {
					if (40001 == errcode || 42001 == errcode || 40014 == errcode) { // || 41001 == errcode
						WeixinConfig.access_token = null;
						throw new AccessTokenErrorException(errcode, getErrorMsg(errcode));
					}
					System.err.println(json);
					String errmsg = getErrorMsg(errcode);
					if (null == errmsg) {
						errmsg = json;
					}
					throw new WeixinAPIException(errcode, errmsg);
				}
			}
		}
		return json;
	}

	public static String execute(String url) throws WeixinAPIException {
		return execute(url, null);
	}

	// ]]

	public static String getAccessToken() throws WeixinAPIException {
		if (WeixinConfig.access_token == null) {
			String urlString = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
						"&appid=" + WeixinConfig.AppId +
						"&secret=" + WeixinConfig.AppSecret;
			String json = doGet(urlString);
			System.out.println(WeixinConfig.AppId + " : " + json);
			if (json != null) {
				JsonNode response = JacksonUtil.toJsonNode(json);
				if (null == response.get("access_token")) {
					int errcode = response.get("errcode").asInt();
					throw new WeixinAPIException(errcode, getErrorMsg(errcode));
				}
				WeixinConfig.access_token = response.get("access_token").toString();
			}
		}
		return WeixinConfig.access_token;
	}

	public static String createMenu(String data) throws WeixinAPIException {
		try {
			return execute(MENU_CREATE_URL + getAccessToken(), data);
		} catch (AccessTokenErrorException e) {
			return execute(MENU_CREATE_URL + getAccessToken(), data); // 重试一次
		}
	}

	public static String getMenu() throws WeixinAPIException {
		try {
			return execute(MENU_GET_URL + getAccessToken());
		} catch (AccessTokenErrorException e) {
			return execute(MENU_GET_URL + getAccessToken());
		}
	}

	public static String getGroups() throws WeixinAPIException {
		try {
			return execute(GROUPS_GET_URL + getAccessToken());
		} catch (AccessTokenErrorException e) {
			return execute(GROUPS_GET_URL + getAccessToken());
		}
	}

	public static String createGroups(String data) throws WeixinAPIException {
		try {
			return execute(GROUPS_CREATE_URL + getAccessToken(), data);
		} catch (AccessTokenErrorException e) {
			return execute(GROUPS_CREATE_URL + getAccessToken(), data);
		}
	}

	public static String updateGroups(String data) throws WeixinAPIException {
		try {
			return execute(GROUPS_UPDATE_URL + getAccessToken(), data);
		} catch (AccessTokenErrorException e) {
			return execute(GROUPS_UPDATE_URL + getAccessToken(), data);
		}
	}

	public static String getUserInfo(String openid) throws WeixinAPIException {
		try {
			return execute(USER_INFO_URL + getAccessToken() + "&openid=" + openid);
		} catch (AccessTokenErrorException e) {
			return execute(USER_INFO_URL + getAccessToken() + "&openid=" + openid);
		}
	}

	public static String getUserGroupId(String data) throws WeixinAPIException {
		try {
			return execute(GROUPS_GETID_URL + getAccessToken(), data);
		} catch (AccessTokenErrorException e) {
			return execute(GROUPS_GETID_URL + getAccessToken(), data);
		}
	}

	public static String updateUserGroups(String data) throws WeixinAPIException {
		try {
			return execute(GROUPS_MEMBERS_UPDATE_URL + getAccessToken(), data);
		} catch (AccessTokenErrorException e) {
			return execute(GROUPS_MEMBERS_UPDATE_URL + getAccessToken(), data);
		}
	}

	/**
	 * 仅当用户最后一次主动发消息给公众号的48小时内可不限制发送次数，超过48小时不可发送
	 */
	public static String sendCustomMessage(String data) throws WeixinAPIException {
		try {
			return execute(MESSAGE_CUSTOM_SEND_URL + getAccessToken(), data);
		} catch (AccessTokenErrorException e) {
			return execute(MESSAGE_CUSTOM_SEND_URL + getAccessToken(), data);
		}
	}

	public static String getUsers(String next_openid) throws WeixinAPIException {
		try {
			return execute(USER_GET_URL + getAccessToken() + "&next_openid=" + next_openid);
		} catch (AccessTokenErrorException e) {
			return execute(USER_GET_URL + getAccessToken() + "&next_openid=" + next_openid);
		}
	}

	// 微信支付发货通知
	public static String delivernotify(String data) throws WeixinAPIException {
		try {
			return execute(PAY_DELIVERNOTIFY_URL + getAccessToken(), data);
		} catch (AccessTokenErrorException e) {
			return execute(PAY_DELIVERNOTIFY_URL + getAccessToken(), data);
		}
	}

	// 标记客户的投诉处理状态
	public static String updatePayfeedback(String feedbackid, String openid) throws WeixinAPIException {
		try {
			return execute(PAYFEEDBACK_UPDATE_URL + getAccessToken() + "&openid=" + openid + "&feedbackid=" + feedbackid);
		} catch (AccessTokenErrorException e) {
			return execute(PAYFEEDBACK_UPDATE_URL + getAccessToken() + "&openid=" + openid + "&feedbackid=" + feedbackid);
		}
	}

	public static String createQrcode(String data) throws WeixinAPIException {
		try {
			return execute(QRCODE_CREATE_URL + getAccessToken(), data);
		} catch (AccessTokenErrorException e) {
			return execute(QRCODE_CREATE_URL + getAccessToken(), data);
		}
	}

	// [[ 授权登入 ]]

	// 构建授权跳转链接
	public static String buildOauth2Link(String appid, String state, String redirect_uri, String scope) {
		StringBuilder sb = new StringBuilder();
		sb.append("https://open.weixin.qq.com/connect/oauth2/authorize");
		sb.append("?appid=" + appid);
		try {
			sb.append("&redirect_uri=" + URLEncoder.encode(redirect_uri, AppConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sb.append("&response_type=code");
		sb.append("&scope=" + scope); // snsapi_base、snsapi_userinfo
		sb.append("&state=" + state);
		sb.append("#wechat_redirect");
		return sb.toString();
	}

	public static String getSnsAccessToken(String code) throws WeixinAPIException {
		String urlString = "https://api.weixin.qq.com/sns/oauth2/access_token" +
						"?appid=" + WeixinConfig.AppId +
						"&secret=" + WeixinConfig.AppSecret +
						"&code=" + code +
						"&grant_type=authorization_code";
		return execute(urlString);
	}

	public static String refreshSnsAccessToken(String refresh_token) throws WeixinAPIException {
		String urlString = "https://api.weixin.qq.com/sns/oauth2/refresh_token" +
				"?appid=" + WeixinConfig.AppId +
				"&grant_type=refresh_token" +
				"&refresh_token=" + refresh_token;
		return execute(urlString);
	}

	public String getSnsUserinfo(String access_token, String openid) throws WeixinAPIException {
		String urlString = "https://api.weixin.qq.com/sns/userinfo" +
				"?access_token=" + access_token +
				"&openid=" + openid +
				"&lang=zh_CN";
		try {
			return execute(urlString);
		} catch (AccessTokenErrorException e) {
			return execute(urlString);
		}
	}

	// ]]

}
