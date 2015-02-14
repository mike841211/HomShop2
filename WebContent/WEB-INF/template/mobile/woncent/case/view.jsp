<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>${article.title}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${article.metaKeyword}">
<meta http-equiv="description" content="${article.metaDescription}">
<style type="text/css">
	body {background:#42bcc6;}
	article{margin:0 auto;padding: 10px;min-height: 420px;}
	article header h1{color: #555;font-size: 1.2em;word-break: normal;word-wrap: break-word;
	background: #FFF;margin: 10px 0;line-height: 1.8em;padding: 1px 10px 0;}
/*	article header span{color: #8C8C8C;font-size: 11px;margin: 0;}*/
/*	article section{margin-top: 10px;}*/
/*	article section .cover{text-align: center;margin-bottom: 10px;}*/
/*	article section .summary{margin-bottom: 10px;border:1px solid #efefef;color:#aaa;padding:15px 10px;}*/
/*	article section .content{color:#666;min-height: 250px;}*/
/*	article footer{}*/
</style>
</head>

<body>
<c:set var="header_title" value="${CacheUtil.getConfig('SHOP_NAME')}" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="fixed-img">
	<img src="${base}/resources/mobile/woncent/images/case.png" id="J_title" />
</section>
<article>
	<header>
		<h1>${article.title}</h1>
	</header>
	<section class="fixed-img" id="img-content">
		<div class="content">${article.content}</div>
	</section>
</article>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>