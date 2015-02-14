package com.homlin.module.shop.controller.shop;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.homlin.app.controller.BaseController;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.constants.ClientType;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.CookieUtil;

public class BaseShopController extends BaseController {

	// 判断客户端类型
	public ClientType getClientType() {
		// System.err.println(getRequest().getHeader("User-Agent"));
		// return getRequest().getHeader("User-Agent").indexOf(" Mobile ") > -1 ? ClientType.mobile : ClientType.pc;
		return ClientType.mobile;
	}

	public String getTemplatePath(String path) {
		ClientType clientType = getClientType();
		String template;
		if (ClientType.mobile == clientType) {
			template = CacheUtil.getConfig(CacheConfigKeys.TEMPLATE_MOBILE);
		} else {
			template = CacheUtil.getConfig(CacheConfigKeys.TEMPLATE_PC);
		}
		if (StringUtils.isBlank(template)) {
			template = "default";
		}
		getRequest().setAttribute("cfg_template", template);
		return "/" + clientType.name() + "/" + template + path;
	}

	public String getTemplatePath() {
		return getTemplatePath("");
	}

	public String getTuid(HttpServletResponse response) {
		// TODO 加密
		String tuid = CookieUtil.getCookie(getRequest(), "TUID");
		if (StringUtils.isEmpty(tuid) && null != response) {
			tuid = UUID.randomUUID().toString();
			CookieUtil.addCookie(response, "TUID", tuid, Integer.MAX_VALUE);
		}
		return tuid;
	}

	public String getTuid() {
		return getTuid(null);
	}

	@Override
	public String errorPage(String message) {
		addActionError(message);
		return getTemplatePath("/error");
	}

}