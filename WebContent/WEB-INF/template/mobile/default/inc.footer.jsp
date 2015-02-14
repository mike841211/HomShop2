<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="tips" id="J_tips"></div>
<c:if test="${not empty CacheUtil.getConfig('SHOP_QQ')}">
<div class="qqChat"><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${CacheUtil.getConfig('SHOP_QQ')}&site=qq&menu=yes"></a></div>
</c:if>
<footer class="footer">
	<div class="footer-login">
		<c:if test="${true==sessionScope.SESSION_MEMBER_ISLOGIN}" var="isMemberLogin">
		<a href="${base}/member/logout.htm">退出</a><span class="bar2">|</span><a href="${base}/member/index.htm">${sessionScope.SESSION_MEMBER_USERNAME}</a>
		</c:if>
		<c:if test="${!isMemberLogin}">
		<a href="${base}/member/register.htm">免费注册</a><span class="bar2">|</span><a href="${base}/member/login.htm">会员登录</a>
		</c:if>
		<span class="footer-backtop"><a href="#">回到顶部</a></span>
	</div>
	<div class="footer-copyright">${CacheUtil.getConfig('SHOP_COPYRIGHT')}</div>
</footer>