<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>${page.title} - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<style type="text/css">
	article{
		margin:0 auto;
		border-right: 1px solid #ccc;
		border-left: 1px solid #ccc;
		padding: 10px;
		min-height:400px;
	}
	article header{
		text-align: center;
		font-size: 20px;
		padding-bottom: 10px;
		border-bottom: 1px dotted #ccc;
	}
	article section{
		margin-top: 10px;
	}
	article section .cover{
		text-align: center;
		margin-bottom: 10px;
	}
	article footer{
	}
</style>
</head>

<body>
<c:set var="header_title" value="${page.title}" scope="request" />
<jsp:include page="inc.header.jsp" />
<article>
	<header>${page.title}</header>
	<section class="fixed-img">
		<div class="content">${page.content}</div>
	</section>
</article>
<jsp:include page="inc.footer.jsp" />
</body>
</html>