package com.homlin.module.shop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.homlin.app.interceptor.BaseInterceptor;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;

/**
 * 微信验证
 * 
 * @author wuduanpiao
 * @date 2014-03-10
 */
public class WeixinSignatureInterceptor extends BaseInterceptor {

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
		// 微信验证
		// =====================
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		return WeixinUtil.checkSignature(signature, timestamp, nonce);
		// if (!WeixinUtil.checkSignature(signature, timestamp, nonce)) {
		// return false;
		// }
		//
		// // System.err.println(request.getContentType());
		// // Enumeration<String> en = request.getHeaderNames();// 使用枚举获取key
		// // while (en.hasMoreElements()) {
		// // // en.nextElement();
		// // // System.err.println(en.nextElement());
		// // System.err.println(request.getHeader(en.nextElement()));
		// // }
		// // BufferedReader reader = request.getReader();
		// // String input = null;
		// // String requestBody = "";
		// // while ((input = reader.readLine()) != null) {
		// // requestBody = requestBody + input + "<br>";
		// // }
		// // System.err.println(requestBody);
		// // =====================
		// // StringBuffer sb = new StringBuffer();
		// // InputStream is = request.getInputStream();
		// // InputStreamReader isr = new InputStreamReader(is);
		// // BufferedReader br = new BufferedReader(isr);
		// // String s = "";
		// // while ((s = br.readLine()) != null) {
		// // sb.append(s);
		// // }
		// // String str = sb.toString();
		// // System.err.println("======str=======");
		// // System.err.println(str);
		// // System.err.println("======str=======");
		// // =====================
		//
		// // System.err.println(request.getParameterMap().keySet());
		// String postStr = null;
		// for (String key : request.getParameterMap().keySet()) {
		// if (key.startsWith("<xml>")) {
		// postStr = key;
		// break;
		// }
		// }
		// // String postStr = WeixinUtil.readStreamParameter(request.getInputStream());
		// // System.err.println("======postStr=======");
		// // System.err.println(postStr);
		// // System.err.println(null != postStr);
		// // System.err.println(!postStr.isEmpty());
		// if (null != postStr && !postStr.isEmpty()) {
		// Document document = DocumentHelper.parseText(postStr);
		// Element root = document.getRootElement();
		// request.setAttribute("FromUserName", root.elementText("FromUserName"));
		// request.setAttribute("ToUserName", root.elementText("ToUserName"));
		// request.setAttribute("MsgType", root.elementText("MsgType"));
		// request.setAttribute("XmlDom", root);
		// return true;
		// }
		// return false;
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