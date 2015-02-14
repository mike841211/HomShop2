<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>客户案例 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<script type="text/javascript" src="${base}/resources/mobile/default/js/jquery.pager.js"></script>
<script type="text/javascript">
var params = {};
<c:forEach items="${param}" var="p">params["${p.key}"]="${p.value}";</c:forEach>
$(function($){
	PageClick = function(pageclickednumber) {params['page'] = pageclickednumber;location.href = location.pathname+"?"+$.param(params);}
	$(".pagination").pager({ pagenumber: ${pager.pageIndex}, pagecount: ${pager.pageCount}, buttonClickCallback: PageClick });
});
</script>
<style type="text/css">
	body {background:#e5004f;}
	.news ul {padding:10px;overflow: auto;}
	.news li {margin-bottom:10px;}
	.news li img {width:100%;}
	.news .item {padding:5px 10px;background:#fff;border-radius: 3px;}
	.news .item .title {color:#333;line-height: 2em;}
	.news .item .date {color:#999;line-height: 1.8em;font-size: 1.2rem;}
	.news .item .summary {color:#999;line-height: 1.4em;margin-top: 5px;}
	.news .item .more {color:#ababab;line-height: 2.5em;border-top: 1px solid #EFEFEF;margin-top: 10px;text-align: right;}
	.news .item .more a {color:#ababab;}
	.pagination {margin: 10px 12px 20px;background: #FFF;padding: 10px 0;border-radius: 3px;}
</style>
</head>

<body>
<c:set var="header_title" value="${CacheUtil.getConfig('SHOP_NAME')}" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="fixed-img">
	<img src="${base}/resources/mobile/woncent/images/news_2.png" id="J_title" />
</section>
<section class="news">
	<ul>
		<c:forEach items="${pager.dataList}" var="item">
		<li><div class="item">
			<div class="title">${item.title}</div>
			<fmt:parseDate value="${item.createDate}" pattern="yyyy-MM-dd HH:mm:ss" var="createDate" />
			<div class="date"><fmt:formatDate value="${createDate}" pattern="yyyy-MM-dd" /></div>
			<c:if test="${not empty item.coverimg}">
			<div class="cover"><img src="${item.coverimg}" /></div>
			</c:if>
			<c:if test="${not empty item.summary}">
			<div class="summary">${item.summary}</div>
			</c:if>
			<div class="more"><a href="${base}/module/news/view/${item.id}.htm">阅读全文 >></a></div>
		</div></li>
		</c:forEach>
	</ul>
	<div class="pagination"></div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>