package com.homlin.module.shop.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.homlin.app.SpringContextHolder;
import com.homlin.app.interceptor.BaseInterceptor;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.controller.admin.AdminLoginController;
import com.homlin.utils.Util;

/**
 * 管理员后台登录验证
 * 
 * @author wuduanpiao
 * @date 2013-02-18
 */
public class AdminLoginInterceptor extends BaseInterceptor {

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

		boolean isAdminLogin = false;
		try {
			// request.getSession().setAttribute(AppConstants.SESSION_USER_ISADMIN, true);
			// return true;
			isAdminLogin = (Boolean) request.getSession().getAttribute(AppConstants.SESSION_ADMIN_ISLOGIN);
		} catch (Exception e) {
			// Util.trace("管理员未登录");
		}

		// 尝试调试登录
		if (!isAdminLogin) {
			String queryString = request.getQueryString() + ":admin";
			// queryString = "DEBUG:admin"; // debug
			boolean debug = queryString.startsWith("DEBUG:");
			debug = debug && "true".equalsIgnoreCase(request.getServletContext().getInitParameter("debug"));
			if (debug == true) {
				String username = queryString.split(":")[1];
				try {
					Util.trace("\n\n\n\n================================================");
					Util.trace("调试状态，自动登录：" + username);
					Util.trace(SpringContextHolder.getBean(AdminLoginController.class).debug_login(username));
					Util.trace("================================================\n\n\n\n");

					isAdminLogin = (Boolean) request.getSession().getAttribute(AppConstants.SESSION_ADMIN_ISLOGIN);
				} catch (Exception e) {
					Util.trace("调试登录失败");
					// e.printStackTrace();
				}
			}
		}

		if (!isAdminLogin) {
			if (isAjaxRequest(request)) { // AJAX请求
				response.setHeader(AppConstants.RESPONSE_STATUSCODE, AppConstants.RESPONSE_STATUSCODE_NEEDLOGIN);
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter pw = response.getWriter();
				pw.println("{\"message\":\"未登入或登入超时，请重新登入！\"}");
				pw.flush();
				pw.close();
			} else {
				request.getRequestDispatcher("/admin/login.htm").forward(request, response);
			}
		}

		return isAdminLogin;
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