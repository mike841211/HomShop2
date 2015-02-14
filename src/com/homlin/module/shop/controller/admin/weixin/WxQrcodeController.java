package com.homlin.module.shop.controller.admin.weixin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.shop.controller.admin.BaseAdminController;
import com.homlin.module.shop.plugin.weixin.WeixinMpTool;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;

@Controller
@RequestMapping(value = "/admin/weixin/qrcode")
public class WxQrcodeController extends BaseAdminController {

	@ResponseBody
	@RequestMapping(value = "/getBaseQrcode")
	public String getBaseQrcode() throws Exception {
		try {
			String data = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 1}}}"; // 永久二维码请求
			// String data = "{\"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}"; //临时二维码请求
			return WeixinUtil.createQrcode(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("读取二维码ticket失败：" + e.getMessage());
		}
	}

	@RequestMapping(value = "/list")
	public String list() throws Exception {
		return "/admin/weixin/qrcode/list";
	}

	// =====================

	// [[ https://mp.weixin.qq.com/ ]]

	// 注意单例模式 wx 是共用的
	WeixinMpTool wx;

	@ResponseBody
	@RequestMapping(value = "/mp_login")
	public String mp_login(String username, String pwd, @RequestParam(defaultValue = "") String imgcode) {
		wx = new WeixinMpTool(username, pwd, imgcode);
		wx.login();
		if (!wx.isLogin()) {
			return ajaxJsonErrorMessage(String.valueOf(wx.getLoginErrCode()), wx.getLoginErrMsg());
		}
		return ajaxJsonSuccessMessage();
	}

	@RequestMapping(value = "/getVerifyCode")
	public void getVerifyCode(HttpServletResponse response) throws Exception {
		InputStream inStream = wx.getVerifyCode();
		OutputStream out = response.getOutputStream();

		response.reset();
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		// response.setContentType("image/jpeg");

		// 循环取出流中的数据
		byte[] b = new byte[4096];
		int len;
		try {
			while ((len = inStream.read(b)) > 0) {
				out.write(b, 0, len);
			}
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.flush();
		out.close();

	}

	@RequestMapping(value = "/getQrcode")
	public synchronized void getQrcode(HttpServletResponse response, @RequestParam(defaultValue = "1") String style,
			@RequestParam(defaultValue = "215") int pixsize,
			@RequestParam(defaultValue = "") String action) throws Exception {

		System.setProperty("jsse.enableSNIExtension", "false");

		String imgurl = ("https://mp.weixin.qq.com/misc/getqrcode?token=" + wx.getToken());
		imgurl += "&style=" + style;
		imgurl += "&pixsize=" + pixsize;
		GetMethod get = new GetMethod(imgurl);
		int status = wx.getClient().executeMethod(get);
		// System.err.println(status);
		if (status != HttpStatus.SC_OK) {
			return;
		}

		InputStream inStream = new BufferedInputStream(get.getResponseBodyAsStream());

		response.reset();
		// response.setHeader("Cache-Control", "no-store");
		// response.setHeader("Pragma", "no-cache");
		// response.setDateHeader("Expires", 0);
		// response.setContentType("image/jpeg");
		if (action.equals("download")) {
			response.addHeader("Content-Disposition", "attachment; filename=\"qrcode_" + style + "_" + pixsize + "\"");
		}

		OutputStream out = response.getOutputStream();
		// 循环取出流中的数据
		byte[] b = new byte[4096];
		int len;
		try {
			while ((len = inStream.read(b)) > 0) {
				out.write(b, 0, len);
			}
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.flush();
		out.close();

	}

	// ]]

}
