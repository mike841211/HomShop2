<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<script type="text/javascript">
$(function($){
	var $J_box = $('#J_box');
	function setHeight(){
		$J_box.css("min-height",$(window).height()-131);
	}
	$(window).on('resize',function(){setHeight();});
	setHeight();
});
</script>
<style type="text/css">
	#J_box {background:#eb5520;}
</style>
</head>

<body>
<c:set var="header_title" value="${CacheUtil.getConfig('SHOP_NAME')}" scope="request" />
<jsp:include page="inc.header.jsp" />
<c:set var="advert" value="${CacheUtil.getAdvert('about')}"/>
<section class="fixed-img" id="J_box">
	${advert.content}
</section>
<jsp:include page="inc.footer.jsp" />
</body>
</html>