package com.homlin.module.shop.plugin.weixin;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.homlin.utils.JacksonUtil;

public class WeixinMpTool {

	private final static Log log = LogFactory.getLog(WeixinMpTool.class);

	public final static String LOGIN_URL = "http://mp.weixin.qq.com/cgi-bin/login?lang=zh_CN";
	public final static String VERIFY_CODE = "http://mp.weixin.qq.com/cgi-bin/verifycode?";

	public final static String USER_AGENT_H = "User-Agent";
	public final static String REFERER_H = "Referer";
	public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36";
	public final static String UTF_8 = "UTF-8";

	private HttpClient client = new HttpClient();
	private String username;
	private String pwd;
	private String imgcode;
	private boolean isLogin = false;
	private String token;
	private int loginErrCode;
	private String loginErrMsg;

	public WeixinMpTool(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
		this.imgcode = "";
	}

	public WeixinMpTool(String username, String pwd, String imgcode) {
		this.username = username;
		this.pwd = pwd;
		this.imgcode = imgcode;
	}

	public HttpClient getClient() {
		return client;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public int getLoginErrCode() {
		return loginErrCode;
	}

	public String getLoginErrMsg() {
		return loginErrMsg;
	}

	public String getToken() {
		return token;
	}

	public boolean login() {
		System.setProperty("jsse.enableSNIExtension", "false");
		try {
			PostMethod post = new PostMethod(LOGIN_URL);
			post.setRequestHeader("Referer", "https://mp.weixin.qq.com/");
			post.setRequestHeader(USER_AGENT_H, USER_AGENT);
			NameValuePair[] params = new NameValuePair[] {
					new NameValuePair("username", this.username),
					new NameValuePair("pwd", DigestUtils.md5Hex(this.pwd.getBytes())), new NameValuePair("f", "json"),
					new NameValuePair("imgcode", this.imgcode) };
			post.setQueryString(params);
			int status = client.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				String ret = post.getResponseBodyAsString();
				// System.err.println(ret);
				JsonNode jsonNode = JacksonUtil.toJsonNode(ret);
				JsonNode base_resp = jsonNode.get("base_resp");
				loginErrCode = base_resp.get("ret").asInt();
				if (loginErrCode == 0) {
					String redirect_url = jsonNode.get("redirect_url").asText();
					redirect_url = redirect_url.substring(redirect_url.indexOf("?"));
					String[] ps = redirect_url.split("&");
					for (String string : ps) {
						if (StringUtils.startsWithIgnoreCase(string, "token=")) {
							token = string.substring(6);
						}
					}
					isLogin = true;
				} else if (loginErrCode == -8) {
					loginErrMsg = base_resp.get("err_msg").asText();
					isLogin = false;
				} else {
					loginErrMsg = base_resp.toString();
					isLogin = false;
				}
				return isLogin;

				// [[ errCode ]]
				// int errCode = retcode.getErrCode();
				// this.loginErrCode = errCode;
				// switch (errCode) {
				//
				// case -1:
				// this.loginErrMsg = "系统错误";
				// return false;
				// case -2:
				// this.loginErrMsg = "帐号或密码错误";
				// return false;
				// case -3:
				// this.loginErrMsg = "密码错误";
				// return false;
				// case -4:
				// this.loginErrMsg = "不存在该帐户";
				// return false;
				// case -5:
				// this.loginErrMsg = "访问受限";
				// return false;
				// case -6:
				// this.loginErrMsg = "需要输入验证码";
				// return false;
				// case -7:
				// this.loginErrMsg = "此帐号已绑定私人微信号，不可用于公众平台登录";
				// return false;
				// case -8:
				// this.loginErrMsg = "邮箱已存在";
				// return false;
				// case -32:
				// this.loginErrMsg = "验证码输入错误";
				// return false;
				// case -200:
				// this.loginErrMsg = "因频繁提交虚假资料，该帐号被拒绝登录";
				// return false;
				// case -94:
				// this.loginErrMsg = "请使用邮箱登陆";
				// return false;
				// case 10:
				// this.loginErrMsg = "该公众会议号已经过期，无法再登录使用";
				// return false;
				// case 65201:
				// case 65202:
				// this.loginErrMsg = "成功登陆，正在跳转...";
				// return true;
				// case 0:
				// this.loginErrMsg = "成功登陆，正在跳转...";
				// return true;
				// default:
				// this.loginErrMsg = "未知的返回";
				// return false;
				// }
				// ]]
			} else {
				System.err.println("mp login status:" + status);
			}
		} catch (Exception e) {
			String info = "【登录失败】【发生异常：" + e.getMessage() + "】";
			System.err.println(info);
			log.debug(info);
			return false;
		}
		return false;
	}

	/**
	 * 获取验证码
	 * 
	 * @throws HttpException
	 * @throws IOException
	 */
	public InputStream getVerifyCode() throws HttpException, IOException {
		GetMethod get = new GetMethod(VERIFY_CODE);
		get.setRequestHeader(USER_AGENT_H, USER_AGENT);
		// get.setRequestHeader("Cookie", this.cookiestr);
		// NameValuePair[] params = new NameValuePair[] {
		// new NameValuePair("username", this.loginUser),
		// new NameValuePair("r", "1365318662649") };
		// get.setQueryString(params);
		int status = client.executeMethod(get);
		if (status == HttpStatus.SC_OK) {
			return get.getResponseBodyAsStream();
		}
		return null;
	}

}
