<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>系统提示 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:include page="inc.required.jsp" />
<style type="text/css">
.main {
	min-height: 400px;
	margin: 20px 10px;
	text-align:center;
}
.main a {line-height: 3em;}
.cry {margin-top: 50px;}
</style>
</head>

<body>
<c:set var="header_title" value="系统提示" scope="request" />
<jsp:include page="inc.header.jsp" />
<section class="main">
	<div class="cry"><img src="${base}/resources/mobile/default/images/cry.jpg" border="0" alt="error"></div>
	<a href="javascript:history.back();">返回</a>
	<p>${message}</p>
	<p>${exception}</p>
	<p>${exception.message}</p>
</section>
<jsp:include page="inc.footer.jsp" />
</body>
</html>