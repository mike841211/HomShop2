package com.homlin.module.shop.interceptor;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.app.interceptor.BaseInterceptor;
import com.homlin.module.AppConstants;

/**
 * 管理员后台注解权限验证
 * 
 * @author wuduanpiao
 * @date 2013-02-18
 */
public class AdminAuthorityInterceptor extends BaseInterceptor {

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// =====================
		// 验证注解权限
		// =====================
		try {
			if ((Boolean) request.getSession().getAttribute(AppConstants.SESSION_ADMIN_ISSUPER)) {
				return true; // 系统管理员
			}
		} catch (Exception e) {
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		CheckAuthority checkAuthority = handlerMethod.getMethodAnnotation(CheckAuthority.class);

		if (null == checkAuthority) {
			// 没有声明权限,放行
			return true;
		}

		boolean hasAuthority = false;

		Set<String> authoritySet = new HashSet<String>(); // SESSION权限集
		try {
			authoritySet = (Set<String>) request.getSession().getAttribute(AppConstants.SESSION_ADMIN_AUTHORITY);
			if (authoritySet == null) {
				return false;
			}
		} catch (Exception e) {
		}
		for (String authority : checkAuthority.value()) {
			if (authoritySet.contains(authority)) {
				hasAuthority = true;
				break;
			}
		}

		if (false == hasAuthority) {
			String message = "没有权限";
			if (isAjaxRequest(request)) { // AJAX请求
				response.setHeader(AppConstants.RESPONSE_STATUSCODE, AppConstants.RESPONSE_STATUSCODE_UNAUTHORIZED);
				response.setCharacterEncoding(AppConstants.CHARSET);
				response.setContentType("text/html;charset=" + AppConstants.CHARSET);
				OutputStream out = response.getOutputStream();
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, AppConstants.CHARSET));
				// PrintWriter pw = response.getWriter();
				pw.println("{\"status\":\"error\",\"message\":\"" + message + "\"}");
				pw.flush();
				pw.close();
			} else {
				request.setAttribute("message", message);
				request.getRequestDispatcher("/common/error.jsp").forward(request, response);
			}
		}
		return hasAuthority;
	}

	/**
	 * 显示视图前执行
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}

	/**
	 * 最后执行，可用于释放资源
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}
}