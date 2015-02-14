package com.homlin.app.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Util;
import com.homlin.utils.PropertyEditor.CustomDateEditor;

public class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@InitBinder
	protected void ininBinder(WebDataBinder binder) {
		// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// dateFormat.setLenient(false);
		// // dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// // dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// // dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

		String[] parsePatterns = new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS" };
		binder.registerCustomEditor(Date.class, new CustomDateEditor(parsePatterns, true));
		// binder.registerCustomEditor(Timestamp.class, new TimestampEditor(parsePatterns, true));
		// binder.registerCustomEditor(Timestamp.class, new CustomDateEditor(dateFormat, true));
	}

	protected void trace(Object... objects) {
		Util.trace(objects);
	}

	protected void traceAsJson(Object... objects) {
		Util.traceAsJson(objects);
	}

	protected void traceRequest() {
		trace(getRequest().getParameterMap().keySet());
		Map<String, String[]> aMap = getRequest().getParameterMap();
		for (String key : aMap.keySet()) {
			// trace(key + " : " + aMap.get(key)[0]);
			String[] values = (String[]) aMap.get(key);
			if (values.length > 1) {
				trace(key + " (多项): " + Arrays.asList(aMap.get(key)));
			} else {
				trace(key + ":\t" + aMap.get(key)[0]);
			}
		}
	}

	protected void traceCookies() {
		Cookie[] cookies = getRequest().getCookies();
		for (Cookie cookie : cookies) {
			System.out.println(cookie.getName() + ":\t" + cookie.getValue());
		}
	}

	protected void traceHeader() {
		Enumeration<String> enumeration = getRequest().getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();
			System.out.println(name + ":\t" + getRequest().getHeader(name));
		}
	}
	// ===================

	@Autowired
	private HttpServletRequest request;

	// 获取Request
	public HttpServletRequest getRequest() {
		return this.request;
	}

	public HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 读取SESSION值
	 * 
	 * @param key
	 * @return
	 */
	public Object getSession(String key) {
		HttpSession session = getSession();
		if (session != null) {
			return session.getAttribute(key);
		} else {
			return null;
		}
	}

	public void setSession(String key, Object value) {
		HttpSession session = getSession();
		session.setAttribute(key, value);
	}

	public boolean isAjaxRequest() {
		String xhr = getRequest().getHeader("X-Requested-With");
		return StringUtils.equalsIgnoreCase(xhr, "XMLHttpRequest");
	}

	public void addActionError(String message) {
		getRequest().setAttribute(MESSAGE, message);
	}

	public String errorPage(String message) {
		addActionError(message);
		return PAGE_ERROR;
	}

	// 设置页面不缓存
	public void setResponseNoCache(HttpServletResponse response) {
		response.setHeader("progma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
	}

	// ===================
	// [[ AJAX ]]
	// ===================

	public static final String PAGE_ERROR = "../../../common/error";

	public static final String CODE = "code";
	public static final String STATUS = "status";
	public static final String WARN = "warn";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String MESSAGE = "message";
	public static final String RESULT = "result";

	public static final String MESSAGE_RESOURCE_NOT_FOUND = "您请求的资源没有找到！";

	public String ajax(String content, String type, HttpServletResponse response) {
		try {
			response.setContentType(type + ";charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// AJAX输出，返回null
	public String ajax(String content, String type) {
		return content;
		// try {
		// HttpServletResponse response = getResponse();
		// response.setContentType(type + ";charset=UTF-8");
		// response.setHeader("Pragma", "No-cache");
		// response.setHeader("Cache-Control", "no-cache");
		// response.setDateHeader("Expires", 0);
		// response.getWriter().write(content);
		// response.getWriter().flush();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return null;
	}

	// AJAX输出文本，返回null
	public String ajaxText(String text) {
		return ajax(text, "text/plain");
	}

	// AJAX输出HTML，返回null
	public String ajaxHtml(String html) {
		return ajax(html, "text/html");
	}

	// AJAX输出XML，返回null
	public String ajaxXml(String xml) {
		return ajax(xml, "text/xml");
	}

	// 根据字符串输出JSON，返回null
	public String ajaxJson(String jsonString) {
		return ajax(jsonString, "text/html");
	}

	// 根据Map输出JSON，返回null
	public String ajaxJson(Object object) {
		return ajax(JacksonUtil.toJsonString(object), "text/html");
	}

	// 输出JSON警告消息，返回null
	public String ajaxJsonWarnMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, WARN);
		jsonMap.put(MESSAGE, message);
		return ajaxJson(jsonMap);
	}

	// 输出JSON成功消息，返回null
	public String ajaxJsonSuccessMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put(MESSAGE, message);
		return ajaxJson(jsonMap);
	}

	// 输出JSON成功消息，返回null，data为其它返回数据
	public String ajaxJsonSuccessMessage(String message, Object result) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put(MESSAGE, message);
		jsonMap.put(RESULT, result);
		return ajaxJson(jsonMap);
	}

	public String ajaxJsonSuccessMessage() {
		return ajaxJsonSuccessMessage("");
	}

	// 输出JSON错误消息，返回null
	public String ajaxJsonErrorMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, ERROR);
		jsonMap.put(MESSAGE, message);
		return ajaxJson(jsonMap);
	}

	// 输出JSON错误消息，带错误代码
	public String ajaxJsonErrorMessage(String code, String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, ERROR);
		jsonMap.put(CODE, code);
		jsonMap.put(MESSAGE, message);
		return ajaxJson(jsonMap);
	}

	// 未登入信息
	public String ajaxJsonErrorUnloginMessage(String message) {
		return ajaxJsonErrorMessage("unlogin", message);
	}

	// 未登入信息
	public String ajaxJsonErrorCaptchaMessage(String message) {
		return ajaxJsonErrorMessage("captcha", message);
	}

	// ===================
	// ]] AJAX ]]
	// ===================

	/**
	 * 获取全部查询参数，多个值用逗号连接
	 */
	protected Map<String, String> getRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			if (StringUtils.isNotEmpty(request.getParameter(name))) {
				String[] values = (String[]) requestParams.get(name);
				params.put(name, StringUtils.join(values, ","));
			}
		}
		return params;
	}

	/**
	 * 获取指定查询参数，只取第一个匹配值
	 */
	protected Map<String, Object> getQueryParams(String... keys) {
		Map<String, Object> params = new HashMap<String, Object>();
		for (String key : keys) {
			String value = request.getParameter(key);
			if (StringUtils.isNotEmpty(value)) {
				params.put(key, value);
			}
		}
		return params;
	}

	/**
	 * 获取全部查询参数，只取第一个匹配值
	 */
	protected Map<String, Object> getAllQueryParams() {
		Set<String> keys = request.getParameterMap().keySet();
		Map<String, Object> params = new HashMap<String, Object>();
		for (String key : keys) {
			String value = request.getParameter(key);
			if (StringUtils.isNotEmpty(value)) {
				params.put(key, value);
			}
		}
		return params;
	}

}
