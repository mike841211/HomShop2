<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/base.css">
<script type="text/javascript" src="${base}/resources/common/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript">
var base = '${base}';
var isMemberLogin = ${true==sessionScope.SESSION_MEMBER_ISLOGIN};
$(function() {
	$("#J_header_menus_key").click(function(){
		$("#J_header_menus").toggle();
	});
});
<c:if test="${CacheUtil.getConfig('WEIXIN_HIDETOOLBAR')=='1'}">document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {WeixinJSBridge.call('hideToolbar');});</c:if>
</script>