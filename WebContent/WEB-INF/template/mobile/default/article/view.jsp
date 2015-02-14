<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>${article.title}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${article.metaKeyword}">
<meta http-equiv="description" content="${article.metaDescription}">
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/article.css">
<script type="text/javascript">
	$(function($){
		$('.subcate-a').click(function(){
			var subcate = $(this).next();
			if (subcate.hasClass('active')){
				subcate.removeClass('active');
			}else{
				$('.active').removeClass('active');
				subcate.addClass('active');
			}
		});
	});
</script>
</head>

<body>
<c:set var="header_title" value="新闻资讯" scope="request" />
<jsp:include page="../inc.header.jsp" />
<article>
	<header>
		<h1>${article.title}</h1>
		<fmt:parseDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss" var="postDate" />
		<span class="post-date"><fmt:formatDate value="${postDate}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
	</header>
	<section class="fixed-img" id="img-content">
		<div class="content">${article.content}</div>
	</section>
	<c:set var="articles" value="${ElUtil.getNextArticles(article.id,5)}"/>
	<c:if test="${fn:length(articles)>0}">
	<section class="related-list">
		<dl class="">
			<dt><h3>相关推荐</h3></dt>
			<c:forEach items="${articles}" var="item">
			<dd><a href="${base}/article/view/${item.id}.htm">${item.title}</a></dd>
			</c:forEach>
		</dl>
	</section>
	</c:if>
</article>
<div class="tbl fixed_bottom">
	<div class="tbl-cell p33">
	<c:set var="categories" value="${ElUtil.getSubArticleCategories('',false)}"/>
	<c:choose>
		<c:when test="${fn:length(categories)==0}">
		<a href="${base}/article/list.htm">全部资讯</a>
		</c:when>
		<c:otherwise>
		<a class="subcate-a">全部资讯</a>
		<div class="subcate">
			<ul>
				<c:forEach items="${categories}" var="category" varStatus="status">
				<li><a href="${base}/article/list.htm?cid=${category.id}">${category.name}</a></li>
				</c:forEach>
			</ul>
			<span class="arr"></span>
		</div>
		</c:otherwise>
	</c:choose>
	</div>
	<div class="tbl-cell p34">
	<c:set var="categories" value="${ElUtil.getSubArticleCategories(article.tbShopArticleCategory.id,true)}"/>
	<c:choose>
		<c:when test="${fn:length(categories)==0}">
		<a href="${base}/article/list.htm?cid=${article.tbShopArticleCategory.id}">${article.tbShopArticleCategory.name}</a>
		</c:when>
		<c:otherwise>
		<a class="subcate-a">相关资讯</a>
		<div class="subcate">
			<ul>
				<c:forEach items="${categories}" var="category" varStatus="status">
				<li><a href="${base}/article/list.htm?cid=${category.id}">${category.name}</a></li>
				</c:forEach>
			</ul>
			<span class="arr"></span>
		</div>
		</c:otherwise>
	</c:choose>
	</div>
	<div class="tbl-cell p33"><a href="javascript:history.back();">返回</a></div>
</div>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>