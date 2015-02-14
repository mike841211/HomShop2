package com.homlin.app.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 自定义拦截器
 * 
 * @author wuduanpiao
 * @date 2013-02-18
 */
public class BaseInterceptor extends HandlerInterceptorAdapter {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected boolean isAjaxRequest(HttpServletRequest request) {
		String xhr = request.getHeader("X-Requested-With");
		return StringUtils.equalsIgnoreCase(xhr, "XMLHttpRequest");
	}

}