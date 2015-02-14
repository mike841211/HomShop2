package com.homlin.module.shop.interceptor;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.homlin.app.interceptor.BaseInterceptor;
import com.homlin.module.AppConstants;

/**
 * 管理员后台登录验证
 * 
 * @author wuduanpiao
 * @date 2013-02-18
 */
public class MemberLoginInterceptor extends BaseInterceptor {

	/**
	 * Controller之前执行
	 * 在业务处理器处理请求之前被调用
	 * 如果返回false
	 * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 
	 * 如果返回true
	 * 执行下一个拦截器,直到所有的拦截器都执行完毕
	 * 再执行被拦截的Controller
	 * 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle()
	 * 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// =====================
		// 验证是否登陆
		// =====================

		boolean isMemberLogin = false;
		try {
			// request.getSession().setAttribute(AppConstants.SESSION_USER_ISADMIN, true);
			// return true;
			isMemberLogin = (Boolean) request.getSession().getAttribute(AppConstants.SESSION_MEMBER_ISLOGIN);
		} catch (Exception e) {
			// Util.trace("管理员未登录");
		}

		if (!isMemberLogin) {
			if (isAjaxRequest(request)) { // AJAX请求
				response.setHeader(AppConstants.RESPONSE_STATUSCODE, AppConstants.RESPONSE_STATUSCODE_NEEDLOGIN);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				OutputStream out = response.getOutputStream();
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
				// PrintWriter pw = response.getWriter();
				pw.println("{\"status\":\"error\",\"code\":\"unlogin\",\"message\":\"未登入或登入超时，请重新登入！\"}");
				pw.flush();
				pw.close();
			} else {
				request.getRequestDispatcher("/member/login.htm").forward(request, response);
				// response.sendRedirect(request.getServletContext().getContextPath() + "/member/login.htm");

				// // String url = request.getRequestURL().toString();
				// String url = request.getRequestURI();
				// String queryString = request.getQueryString();
				// if (StringUtils.isNotEmpty(queryString)) {
				// url = url + "?" + queryString;
				// }
				// response.sendRedirect("/login.htm?from=" + url);
			}
		}

		return isMemberLogin;
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