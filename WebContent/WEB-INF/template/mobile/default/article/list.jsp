<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>资讯列表 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<script type="text/javascript" src="${base}/resources/mobile/default/js/jquery.pager.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/shop.js"></script>
<script type="text/javascript">
var params = {};
<c:forEach items="${param}" var="p">params["${p.key}"]="${p.value}";</c:forEach>
</script>
<script type="text/javascript" src="${base}/resources/common/js/juicer-min.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/article.list.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/list.css">
<style type="text/css">
.article-lst .article-li {padding: 5px 0;border-bottom: 1px solid #E8E5E5;display: block;overflow: hidden;}
.article-lst .article-li .article-a {display: block;overflow: hidden;clear: both;padding: 0;position: relative;padding-right:20px;margin-right:10px;background: url(${base}/resources/mobile/default/css/arrowr.png) 100% center no-repeat;}
.article-lst .article-li .article-a span{display: inline-block;width: 7px;height: 7px;margin: 0 5px 2px 0;border-radius: 7px;background-color: #E4393C;}
</style>
</head>

<body>
<c:set var="header_title" value="新闻资讯" scope="request" />
<jsp:include page="../inc.header.jsp" />
<a name="listTop"></a>
<section class="main">
	<div class="btn-filter list_float"></div>
	<div id="J_filter" class="filter">
		<div class="filter-box-mask"></div>
		<div class="filter-box">
			<div class="filter-box-arrow"></div>
			<div class="filter-box-content">
			<div class="tbl box-shadow">
				<div class="tbl-cell filter-tab on"><span>资讯分类</span></div>
			</div>
			<c:set var="categories" value="${ElUtil.getSubArticleCategories('',false)}"/>
			<ul class="filter-ul-lst cate-ul on">
				<c:forEach items="${categories}" var="category" varStatus="status">
				<li class="filter-ul-li">
					<a>${category.name}</a>
					<div>
						<ul data-prop="cid">
							<li><a data-id="${category.id}"${param.cid==category.id ? ' class="on"' : ''}>全部</a></li>
							<c:set var="subCategories" value="${ElUtil.getSubArticleCategories(category.id,false)}"/>
							<c:forEach items="${subCategories}" var="subCategory">
							<li><a data-id="${subCategory.id}"${param.cid==subCategory.id ? ' class="on"' : ''}>${subCategory.name}</a></li>
							</c:forEach>
						</ul>
					</div>
				</li>
				</c:forEach>
			</ul>
			</div>
		</div>
    </div>
	<div class="search-lst">
		<ul class="article-lst">
		</ul>
		<div class="pagination"></div>
    </div>
</section>
<script id="J_li" type="text/template">
	{@if data==null || data.length==0}
		<%-- <li class="article-li article-li-empty">暂无数据</li> --%>
	{@else}
		{@each data as item,index}
		<li class="article-li">
			<a href="${base}/article/view/\${item.id}.htm" class="article-a">
				<strong class="l2h"><span></span>\${item.title}</strong>
			</a>
		</li>
		{@/each}
	{@/if}
</script>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>