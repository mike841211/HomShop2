<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>会员中心 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<style type="text/css">
	body {background:#f8f8f8;}
	section {min-height:400px;padding:20px 10px;}
	.dl1 {margin:0px 0 10px;}
	.dl1 dd {position: relative;}
	.unbind,.bind{position:absolute;right:30px;top:1px;}
	.unbind {color:#e00;}
	.bind {color:#aaa;}
</style>
</head>

<body>
<c:set var="header_title" value="账号绑定" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section>
	<div class="dl1">
		<dl>
			<dt>绑定账号</dt>
			<c:if test="${empty bindOauth2s['WEIXIN']}" >
			<c:set var="redirect_uri" value="${CacheUtil.getConfig('SHOP_URL')}/member/bind.htm"/>
			<dd class="icon-wx"><a href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=${CacheUtil.getConfig('WEIXIN_APPID')}&redirect_uri=${ElUtil.encodeURL(redirect_uri)}&response_type=code&scope=snsapi_userinfo&state=weixin#wechat_redirect">微信账号</a><span class="unbind">未绑定</span></dd>
			</c:if>
			<c:if test="${not empty bindOauth2s['WEIXIN']}" >
			<dd class="icon icon-wx"><a>微信账号</a><span class="bind">已绑定</span></dd>
			</c:if>
		</dl>
	</div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>