<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<title>${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<script type="text/javascript" src="${base}/resources/common/js/idangerous.swiper.min.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/common/js/idangerous.swiper.css">
<script type="text/javascript">
$(function($){

	// 图片滑动
	var mySwiper = new Swiper('.swiper-container',{
		pagination: '.pagination',
		//autoplay: 3000,
		loop:true,
		grabCursor: true,
		paginationClickable: true
	});
});
</script>
<style type="text/css">
	body{background:#efefef}
	section,form{background:#fff;}
	.srch-box {border: 1px solid #ccc;border-radius: 3px;}
	.index-layout {width:320px;margin:0 auto;}
	.index-module{margin-bottom:5px;padding: 5px 5px;border-bottom:1px solid #ccc;border-radius:3px;}
	.index-module .caption {font-weight:bold;position:relative;line-height:1.8em;height:1.8em;border-bottom:1px solid #ccc;margin-bottom:10px;}
	.index-module .caption .icon-arr{top:7px;}
	.section-logo{text-align:center;line-height: 0;/* height:50px; */overflow:hidden;}
	.index-menu{margin-bottom:5px;border-radius:0 0 5px 5px;padding:12px 0 10px;border-bottom:1px solid #ccc;}
	.index-menu .tbl-cell{text-align:center;}
	/* .index-menu .menu{width:20%;} */
	.index-menu .menu a{display:block;}

	/* idangerous.swiper */
	.swiper-container{height:120px;width:100%;text-align:center;z-index:9992}
	.pagination{position:absolute;left:0;text-align:center;bottom:5px;width:100%;margin:0;}
	.swiper-pagination-switch{display:inline-block;width:10px;height:10px;border-radius:10px;background:#999;box-shadow:0 1px 2px #555 inset;margin:0 3px;cursor:pointer}
	.swiper-active-switch{background:#6f3}
	.swiper-slide img{width:320px}
</style>
</head>

<body>
<div class="index-layout">
<c:set var="slides" value="${CacheUtil.getAdverts('INDEX_SLIDE')}"/>
<c:if test="${fn:length(slides)>0}">
<section class="section-img">
    <div class="swiper-container">
      <div class="swiper-wrapper">
        <c:forEach items="${slides}" var="image">
        <div class="swiper-slide"><a href="${image.link}"><img src="${image.imgpath}" alt="${image.title}"></a></div>
        </c:forEach>
      </div>
      <div class="pagination"></div>
    </div>
</section>
</c:if>
<section class="section-logo"><img src="${base}/resources/mobile/${cfg_template}/images/logo.png" alt="微商城LOGO"></section>
<jsp:include page="inc.search.jsp" />
<section class="index-menu tbl">
	<c:set var="navs" value="${CacheUtil.getNavigation('middle')}"/>
	<c:forEach items="${navs}" var="nav">
	<div class="tbl-cell menu"><a href="${nav.url}"${nav.isBlankTarget=='1' ? ' target="_blank"' : ''}>${nav.title}</a></div>
	</c:forEach>
</section>
<c:set var="modules" value="${CacheUtil.getAdverts('INDEX_MODULE')}"/>
<c:forEach items="${modules}" var="module">
<section class="index-module fixed-img">
	<c:if test="${not empty module.title}"><div class="caption"><c:if test="${not empty module.link}" var="hasLink"><a class="block" href="${module.link}">${module.title}<span class="icon-arr"></span></a></c:if><c:if test="${not hasLink}">${module.title}</c:if></div></c:if>
	<div class="content">${module.content}</div>
</section>
</c:forEach>
</div>
<jsp:include page="inc.footer.jsp" />
</body>
</html>