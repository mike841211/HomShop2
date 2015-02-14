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
<c:set var="slides" value="${CacheUtil.getAdverts('INDEX_SLIDE')}"/>
<c:set var="news" value="${CacheUtil.getAdverts('index_news')}"/>
<c:if test="${fn:length(slides)>0 || fn:length(news)>0}">
<script type="text/javascript" src="${base}/resources/common/js/idangerous.swiper.min.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/common/js/idangerous.swiper.css">
<script type="text/javascript">
$(function($){
	// 图片滑动
	var swiper1 = new Swiper('.swiper1',{
		pagination: '.swiper1 .pagination',
		autoplay: 3000,
		loop:true,
		grabCursor: true,
		paginationClickable: true
	});
	var $swiperCont = $('.swiper1');
	if ($swiperCont.size()>0){
		$('.swiper-slide img',$swiperCont).eq(0).load(function(){
			var _height = this.height;
			$swiperCont.css("height",_height);
		});
		$(window).on('resize',function(){
			var _height = $('.swiper-slide img',$swiperCont).eq(0).height();
			$swiperCont.css("height",_height);
		});
	}

	var swiper2 = new Swiper('.swiper2',{
		pagination: '.swiper2 .pagination',
		autoplay: 3000,
		loop:true,
		grabCursor: true,
		paginationClickable: true
	});
	var $swiperCont2 = $('.swiper2');
	if ($swiperCont2.size()>0){
		$('.swiper-slide img',$swiperCont2).eq(0).load(function(){
			var _height = this.height;
			$swiperCont2.css("height",_height);
		});
		$(window).on('resize',function(){
			var _height = $('.swiper-slide img',$swiperCont2).eq(0).height();
			$swiperCont2.css("height",_height);
		});
		$('.arrow-left').on('click', function(e){
			e.preventDefault()
			swiper2.swipePrev()
		})
		$('.arrow-right').on('click', function(e){
			e.preventDefault()
			swiper2.swipeNext()
		})
	}
});
</script>
</c:if>
</head>

<body>
<c:set var="header_title" value="${CacheUtil.getConfig('SHOP_NAME')}" scope="request" />
<jsp:include page="inc.header.jsp" />
<c:if test="${fn:length(slides)>0}">
<!-- swiper begin -->
<div class="swiper-container swiper1">
  <div class="swiper-wrapper">
	<c:forEach items="${slides}" var="image">
	<div class="swiper-slide"><a href="${image.link}"><img src="${image.imgpath}" alt="${image.title}"></a></div>
	</c:forEach>
  </div>
  <div class="pagination"></div>
</div>
<!-- swiper end -->
</c:if>
<!-- nav -->
<section class="fixed-img m-nav">
	<div class="tbl">
		<div class="tbl-cell"><a href="${base}/module/about.htm"><img src="${base}/resources/mobile/woncent/images/w.png" /></a></div>
		<div class="tbl-cell"><a href="${base}/module/open.htm"><img src="${base}/resources/mobile/woncent/images/o.png" /></a></div>
		<div class="tbl-cell"><a href="${base}/module/news/list.htm"><img src="${base}/resources/mobile/woncent/images/n.png" /></a></div>
		<div class="tbl-cell"><a href="${base}/module/case/list.htm"><img src="${base}/resources/mobile/woncent/images/c.png" /></a></div>
		<div class="tbl-cell"><a href="${base}/module/expericent.htm?data=expericent_1"><img src="${base}/resources/mobile/woncent/images/e.png" /></a></div>
		<div class="tbl-cell"><a href="${base}/module/development_history.htm"><img src="${base}/resources/mobile/woncent/images/n2.png" /></a></div>
		<div class="tbl-cell"><a href="${base}/module/team.htm"><img src="${base}/resources/mobile/woncent/images/t.png" /></a></div>
	</div>
</section>
<section class="fixed-img m-nav2">
	<div class="tbl">
		<div class="tbl-cell"><a><img src="${base}/resources/mobile/woncent/images/cp1.png" /></a></div>
		<div class="tbl-cell"><a><img src="${base}/resources/mobile/woncent/images/cp2.png" /></a></div>
		<div class="tbl-cell"><a><img src="${base}/resources/mobile/woncent/images/cp3.png" /></a></div>
	</div>
</section>
<!-- // nav -->
<c:set var="cases" value="${ElUtil.getArticlesByCateCode('case',6)}"/>
<section class="fixed-img m-case">
	<div class="relative">
		<img src="${base}/resources/mobile/woncent/images/index_case_title.jpg" />
		<a href="${base}/module/case/list.htm" class="a-tab"></a>
	</div>
	<div class="tbl">
		<c:forEach items="${cases}" var="item" varStatus="status">
		<div class="tbl-cell"><a href="${base}/module/case/view/${item.id}.htm"><img src="${item.coverimg}" alt="${item.title}" /></a></div>
	<c:if test="${not status.last && status.count%3==0}">
	</div>
	<div class="tbl">
	</c:if>
		</c:forEach>
		<c:set var="_length" value="${fn:length(cases)%3}"/>
		<c:if test="${_length>0}">
		<c:forEach begin="1" end="${3-_length}">
		<div class="tbl-cell"></div>
		</c:forEach>
		</c:if>
	</div>
</section>
<%-- <section class="fixed-img m-news relative">
	<img src="/resources/mobile/woncent/images/index_new_title.jpg" />
	<div class="tbl">
		<div class="tbl-cell"><a><img src="/resources/mobile/woncent/images/1_01.jpg" /></a></div>
		<div class="tbl-cell">
			<a>中共中央政治局常委、国务院总理温家宝视察环球鞋网、淘鞋网</a>
			<br><span>2012-04-25</span>
			<p>月底，本土运动品牌2013年财报集中出炉，面对成绩单，各家可谓一言难尽、五味杂陈。总体来看，在熬过了去年的行业寒冬之后，虽然众品牌业绩仍呈下滑势态，但速度已开始放缓。</p>
		</div>
	</div>
    <!-- <a class="arrow-left" href="###"></a>
    <a class="arrow-right" href="###"></a> -->
</section> --%>
<c:if test="${fn:length(news)>0}">
<section class="fixed-img m-news relative">
	<img src="/resources/mobile/woncent/images/index_new_title.jpg" />
	<!-- swiper begin -->
	<div class="swiper-container swiper2">
	  <div class="swiper-wrapper">
		<c:forEach items="${news}" var="item">
		<div class="swiper-slide"><a href="${item.link}"><img src="${item.imgpath}" alt="${item.title}"></a></div>
		</c:forEach>
	  </div>
	  <div class="pagination"></div>
	</div>
	<!-- swiper end -->
    <a class="arrow-left" href="###"></a>
    <a class="arrow-right" href="###"></a>
</section>
</c:if>
<c:set var="modules" value="${CacheUtil.getAdverts('INDEX_MODULE')}"/>
<c:forEach items="${modules}" var="module">
<c:if test="${not empty module.content}">
<section class="fixed-img">
	${module.content}
</section>
</c:if>
</c:forEach>
<jsp:include page="inc.footer.jsp" />
</body>
</html>