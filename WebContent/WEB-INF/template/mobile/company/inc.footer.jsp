<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="tips" id="J_tips"></div>
<footer class="footer">
	<div class="footer-login">
		<span class="footer-backtop"><a href="#">回到顶部</a></span>
	</div>
	<div class="footer-copyright">${CacheUtil.getConfig('SHOP_COPYRIGHT')}</div>
</footer>