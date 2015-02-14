package com.homlin.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;

import com.homlin.utils.Util;

/**
 * 网站前台页面请求拦截，初始化公共数据
 * 
 * @author Administrator
 * 
 */
public class ProcessInterceptor extends BaseInterceptor {

	private StopWatch stopWatch;

	/**
	 * Controller之前执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		stopWatch = new StopWatch(handler.toString());
		stopWatch.start();

		System.out.println();
		System.out.println("\tFrom:" + request.getRemoteAddr());
		String uri = request.getRequestURI();
		System.out.println("\t[" + Util.getDateTimeNow() + "] " + request.getMethod() + "：" + uri + "?" + request.getQueryString());

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		System.out.println("\t----------------------------------------------------------------------------------------------------");
		System.out.println("\t" + handlerMethod.getMethod().getDeclaringClass().getName() + "\t->" + handlerMethod.getMethod().getName());
		System.out.println("\t----------------------------------------------------------------------------------------------------");
		System.out.println("\t" + handler.toString());
		// System.out.println("\t" + request.getHeader("User-Agent"));
		System.out.println();
		System.out.println();

		return true;
	}

	/**
	 * 最后执行，可用于释放资源
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		try {
			stopWatch.stop();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("===============" + stopWatch.getTotalTimeMillis() + "毫秒================");
		// System.out.println("\nShopInteceptor: afterCompletion");
	}
}