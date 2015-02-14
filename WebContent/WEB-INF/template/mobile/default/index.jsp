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
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/index.css">
<script type="text/javascript">
$(function($){
	var $bg_img = $('.index-bg img').eq(0);
	var $menus = $('.index-menus').eq(0);
	var $menus_ul = $('.index-menus ul').eq(0);
	function doLayout(){
		var img_height = $bg_img.height();
		$menus.css("height",img_height);
		$menus_ul.css("margin-top",(img_height-$menus_ul.height())*0.4);
		//$menus_ul.css("margin-top",$(window).height()-300);
	}
	$bg_img.on('load',function(){doLayout();});
	$(window).resize(function (){doLayout();});
});
</script>
<c:set var="slides" value="${CacheUtil.getAdverts('INDEX_SLIDE')}"/>
<c:if test="${fn:length(slides)>0}">
<script type="text/javascript" src="${base}/resources/common/js/idangerous.swiper.min.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/common/js/idangerous.swiper.css">
<script type="text/javascript">
$(function($){

	// 图片滑动
	var mySwiper = new Swiper('.swiper-container',{
		pagination: '.pagination',
		autoplay: 5000,
		loop:true,
		grabCursor: true,
		paginationClickable: true
	});
	var $swiperCont = $('.swiper-container');
	if ($swiperCont.size()>0){
		$('.swiper-slide img').eq(0).load(function(){
			var _height = this.height;
			$swiperCont.css("height",_height);
		});
		$(window).on('resize',function(){
			var _height = $('.swiper-slide img').eq(0).height();
			$swiperCont.css("height",_height);
		});
	}
});
</script>
</c:if>
</head>

<body>
<c:set var="header_title" value="${CacheUtil.getConfig('SHOP_NAME')}" scope="request" />
<header>
	<div class="header-bar" id="J_header_bar">
		<a href="javascript:history.back();" class="header-back"><span>返回</span></a>
		<a href="${base}/category/list.htm" class="header-icon icon2"><span>列表</span></a>
		<h2>${header_title}</h2>
		<a href="${base}/order/list.htm" class="header-icon icon3"><span>订单</span></a>
		<a href="${base}/cart/list.htm" class="header-icon icon4"><span>购物车</span></a>
	</div>
</header>
<c:if test="${fn:length(slides)>0}">
<!-- swiper begin -->
<div class="swiper-container">
  <div class="swiper-wrapper">
	<c:forEach items="${slides}" var="image">
	<div class="swiper-slide"><a href="${image.link}"><img src="${image.imgpath}" alt="${image.title}"></a></div>
	</c:forEach>
  </div>
  <div class="pagination"></div>
</div>
<!-- swiper end -->
</c:if>
<div class="index-bg"><img src="${CacheUtil.getConfig('TEMPLATE_MOBILE_INDEX_BGIMG')}" /></div>
<div class="index-menus">
<ul>
	<c:set var="navs" value="${CacheUtil.getNavigation('middle')}"/>
	<c:forEach items="${navs}" var="nav">
	<li><a href="${nav.url}"${nav.isBlankTarget=='1' ? ' target="_blank"' : ''}>${nav.title}</a></li>
	</c:forEach>
</ul>
</div>
<jsp:include page="inc.footer.jsp" />
</body>
</html>