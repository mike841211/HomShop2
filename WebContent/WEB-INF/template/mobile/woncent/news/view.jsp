<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>${article.title}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${article.metaKeyword}">
<meta http-equiv="description" content="${article.metaDescription}">
<style type="text/css">
	body {background:#e5004f;}
	article{margin:10px;padding: 10px;min-height: 420px;border-radius:3px;background:#fff;}
	article header h1{color: #555;font-size: 1.2em;word-break: normal;word-wrap: break-word;line-height: 1.8em;}
	article header span{color: #8C8C8C;margin: 0;font-size: 1rem;}
/*	article section{margin-top: 10px;}*/
/*	article section .cover{text-align: center;margin-bottom: 10px;}*/
/*	article section .summary{margin-bottom: 10px;border:1px solid #efefef;color:#aaa;padding:15px 10px;}*/
	article section .content{color:#666;min-height: 250px;margin:10px 5px;}
</style>
</head>

<body>
<c:set var="header_title" value="${CacheUtil.getConfig('SHOP_NAME')}" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="fixed-img">
	<img src="${base}/resources/mobile/woncent/images/news_1.png" id="J_title" />
</section>
<article>
	<header>
		<h1>${article.title}</h1>
		<fmt:parseDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss" var="createDate" />
		<span class="post-date"><fmt:formatDate value="${createDate}" pattern="yyyy-MM-dd" /></span>
	</header>
	<section class="fixed-img" id="img-content">
		<c:if test="${not empty article.coverimg}">
		<div class="cover"><img src="${article.coverimg}" /></div>
		</c:if>
		<div class="content">${article.content}</div>
	</section>
</article>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>