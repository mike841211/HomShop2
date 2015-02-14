<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	body {background:#42bcc6;}
	.case ul {padding:10px;overflow: auto;}
	.case li {width:50%;float:left;}
	.case li:nth-child(odd) {clear:left;}
	.case li img {width:100%;}
	.case .item {padding:0px 5px 2px 5px;background:#fff;margin: 2px;}
	.case .item p {text-align:center;color:#666;line-height: 2em;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;}
	.pagination {margin: 10px 12px 20px;background: #FFF;padding: 10px 0;}
</style>
</head>

<body>
<c:set var="header_title" value="${CacheUtil.getConfig('SHOP_NAME')}" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="fixed-img">
	<img src="${base}/resources/mobile/woncent/images/case.png" id="J_title" />
</section>
<section class="case">
	<ul>
		<c:forEach items="${pager.dataList}" var="item">
		<li><div class="item"><a href="${base}/module/case/view/${item.id}.htm"><p>${item.title}</p><img src="${item.coverimg}" /></a></div></li>
		</c:forEach>
	</ul>
	<div class="pagination"></div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>