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
<c:set var="slides" value="${CacheUtil.getAdverts(param.data)}"/>
<c:if test="${fn:length(slides)>0}">
<script type="text/javascript" src="${base}/resources/common/js/idangerous.swiper.min.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/common/js/idangerous.swiper.css">
<script type="text/javascript">
$(function($){
	// 图片滑动
	var mySwiper = new Swiper('.swiper-container',{
		//pagination: '.pagination',
		//autoplay: 3000,
		//loop:true,
		grabCursor: true,
		paginationClickable: true
	});
	var $swiperCont = $('.swiper-container');
	if ($swiperCont.size()>0){
		var $J_title = $('#J_title');
		var $J_swiper_slide_img_0 = $('.swiper-slide img').eq(0);
		function setHeight(){
			var _height = $J_swiper_slide_img_0.height();
			var _height2 = $(window).height()-$J_title.height()-131;
			if(_height<_height2){_height=_height2;}
			$swiperCont.css("height",_height);
		}
		$J_swiper_slide_img_0.load(function(){setHeight();});
		$(window).on('resize',function(){setHeight();});
	}
});
</script>
</c:if>
<style type="text/css">
	.swiper-container {background:#ffc600;}
	.swiper-slide img{width: 100%;}
	/* .swiper-slide img {max-width:100%;} */
</style>
</head>

<body>
<c:set var="header_title" value="${CacheUtil.getConfig('SHOP_NAME')}" scope="request" />
<jsp:include page="inc.header.jsp" />
<section class="fixed-img">
	<img src="${base}/resources/mobile/woncent/images/${param.data}.png" id="J_title" />
</section>
<c:if test="${fn:length(slides)>0}">
<!-- swiper begin -->
<div class="swiper-container">
  <div class="swiper-wrapper">
	<c:forEach items="${slides}" var="image">
	<div class="swiper-slide"><img src="${image.imgpath}" alt="${image.title}"></div>
	</c:forEach>
  </div>
  <div class="pagination"></div>
</div>
<!-- swiper end -->
</c:if>
<jsp:include page="inc.footer.jsp" />
</body>
</html>