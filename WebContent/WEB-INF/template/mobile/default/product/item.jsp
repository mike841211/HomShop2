<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>商品详情 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<script type="text/javascript" src="${base}/resources/common/js/idangerous.swiper.min.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/common/js/idangerous.swiper.css">
<script type="text/javascript">
var product ={
	price : ${product.price},
	stock : ${product.stock},
	sn : "${product.sn}",
	id : "${product.id}"
};

var skus = [
<c:forEach items="${product.tbShopSkus}" var="sku" varStatus="status">
	{id:"${sku.id}",sn:"${sku.sn}",specs:${sku.specificationJson},stock:${sku.stock},price:${sku.price}}<c:if test="${not status.last}">,</c:if>
</c:forEach>
];
</script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/item.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/item.css">
<style type="text/css">
.swiper-container{height:${CacheUtil.getConfig('SHOP_PRODUCT_ITEM_IMAGE_HEIGHT')}px;}
<c:if test="${CacheUtil.getConfig('SHOP_PRODUCT_ITEM_IMAGE_MODE')=='HIDDEN'}">
.swiper-slide img{max-height:none;}
</c:if>
</style>
</head>

<body>
<c:set var="header_title" value="商品详情" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="section-img">
    <div class="mask"></div>
    <div class="swiper-container">
      <div class="swiper-wrapper">
		<c:set var="images" value="${fn:split(product.imageStore,'|')}" />
		<c:set var="images_length" value="${fn:length(images)}" />
        <c:forEach items="${images}" begin="${images_length<2 ? 0 : 1}" var="image">
        <div class="swiper-slide"><img src="${base}/resources/common/images/blank.gif" _src="${image}" alt="" /></div>
        </c:forEach>
      </div>
      <div class="pagination"></div>
    </div>
	<div class="add_favorite btn-sc2"><span></span>收藏</div>
</section>
<section class="section10">
	<p class="detail-title">
		${product.name}
		<c:if test="${not empty product.slogan}"><br><span class="red2">${product.slogan}</span></c:if>
		<br><span class="gray bold">货号：${product.sn}</span>
		<c:if test="${product.isNew=='1'}"><em class="hint hint-n">新</em></c:if>
		<c:if test="${product.isHot=='1'}"><em class="hint hint-h">热</em></c:if>
		<c:if test="${product.isPromotion=='1'}"><em class="hint hint-p">促</em></c:if>
		<c:if test="${product.isRecomend=='1'}"><em class="hint hint-r">荐</em></c:if>
		<c:if test="${product.isFreeShipping=='1'}"><em class="hint hint2 hint-fs">免邮</em></c:if>
	</p>
</section>
<section class="section10">
	<a name="select_specification"></a>
	<div class="info bold">
		<a>销售信息</a>
		<span class="icon-arr"></span>
	</div>
	<div class="detail-price">
		<c:if test="${CacheUtil.getConfig('SHOP_ISSHOWMARKETPRICE')=='1'}"><p><span class="caption">市价：<del>￥<fmt:formatNumber value="${product.marketPrice}" pattern="#,##0.00"/></del></span></p></c:if>
		<p><span class="caption">售价：</span><span class="em13 bold red2" id="J_price">￥<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></p>
		<p><span class="caption">货号：</span><span class="bold" id="J_sku_sn">${product.sn}</span></p>
		<%-- <p class="add_favorite btn-sc"></p> --%>
	</div>
	<div id="J_specification">
	<c:forEach items="${product.tbShopSpecifications}" var="specification">
	<dl class="detail-option" data-value="${specification.id}">
	  <dt class="caption">${specification.name}:</dt>
	  <c:forEach items="${specificationValues[specification.id]}" var="value">
	  <dd><a href="javascript:void(0);" class="" data-value="${value.id}">${value.name}</a></dd>
	  </c:forEach>
	</dl>
	</c:forEach>
	</div>
	<div class="detail-option tbl">
		<span class="text-fl caption tbl-cell">数量：</span>
		<span class="add-del tbl-cell">
			<a class="btn-del" id="J_btnDel"><span></span></a>
			<input type="number" class="input" value="1" id="J_number" min="1" max="999" maxlength="3">
			<a class="btn-add" id="J_btnAdd"><span></span></a>
		</span>
		<span class="text-fl caption tbl-cell">库存：<span class="bold black" id="J_stock">${product.stock}</span></span>
	</div>
</section>
<section class="section10">
	<div class="info bold">
		<a>商品描述</a>
		<span class="icon-arr"></span>
	</div>
	<div class="detail-introduction fixed-img" id="J_introduction">
	<c:choose>
		<c:when test="${CacheUtil.getConfig('SHOP_INTRODUCTION_LAZY')=='1'}"><div id="J_loadIntroduction" style="text-align:center;color:steelblue;">点击加载</div></c:when>
		<c:otherwise>${product.introduction}</c:otherwise>
	</c:choose>
	</div>
</section>
<c:if test="${CacheUtil.getConfig('SHOP_PRODUCT_COMMENT_ENABLED')=='1'}">
<script type="text/javascript" src="${base}/resources/common/js/juicer-min.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/jquery.pager.js"></script>
<script type="text/javascript">var SHOP_PRODUCT_COMMENT_ENABLED=true;product.scoreCount=${product.scoreCount};</script>
<section class="section10">
	<div class="info bold">
		<a>商品评价</a>
		<span class="icon-arr"></span>
	</div>
	<div class="detail-comments fixed-img" id="J_comments">
		<div class="tbl total">
			<div class="score-num"><span class="score"><fmt:formatNumber value="${product.score}" pattern="0.0"/></span>分</div>
			<div class="score-bg"><div class="score-star1"><div class="score-star2" style="width:${product.score*40}px"></div></div></div>
		</div>
		<ul id="J_comments_ul">
			<%-- <li>
				<div class="score-star1 score-star1-s"><div class="score-star2 score-star2-s" style="width:${100*4.3/5}px"></div></div>
				<div class="contents">评价：立即购买立即购买立即买立即购买立即买立即购买立即购买</div>
				<div class="comment-info"><span class="username">ceshi</span>：<span class="datetime">2013-04-22 18:00:05</span></div>
			</li>
			<li>
				<div class="score-star1 score-star1-s"><div class="score-star2 score-star2-s" style="width:${100*4.3/5}px"></div></div>
				<div class="contents">评价：立即购买立即购买立即买立即购买立即买立即购买立即购买</div>
				<div class="comment-info"><span class="username">ceshi</span>：<span class="datetime">2013-04-22 18:00:05</span></div>
				<div class="reply"><span class="arr"></span>
				评价：立即购买立即购买立即买立即购买立即买立即购买立即购买
					<div class="comment-info">客服回复：2013-04-22 18:00:05</div>
				</div>
			</li>
			<li>
				<div class="score-star1 score-star1-s"><div class="score-star2 score-star2-s" style="width:${100*4.3/5}px"></div></div>
				<div class="contents"><span class="empty">没有评价</span></div>
				<div class="comment-info"><span class="username">ceshi</span>：<span class="datetime">2013-04-22 18:00:05</span></div>
			</li> --%>
		</ul>
		<div class="pagination"></div>
	</div>
</section>
<script id="J_comments_tmpl" type="text/template">
	{@if data==null || data.length==0}
	<%-- <li>
		<div class="empty">暂无评价</div>
	</li> --%>
	{@else}
	{@each data as item,index}
	<li>
		<div class="score-star1 score-star1-s"><div class="score-star2 score-star2-s" style="width:\${item.score*20}px"></div></div>
		{@if item.contents==null || item.contents==''}
		<div class="contents"><span class="empty">没有评价</span></div>
		{@else}
		<div class="contents">\${item.contents}</div>
		{@/if}
		<div class="comment-info"><span class="username">{@if item.username==null || item.username==''}匿名{@else}\${item.username}{@/if}</span>：<span class="datetime">\${item.createDate}</span></div>
		{@if item.replyContent==null || item.replyContent==''}
		{@else}
		<div class="reply"><span class="arr"></span>
			\${item.replyContent}
			<div class="comment-info">客服回复：\${item.replyDate}</div>
		</div>
		{@/if}
	</li>
	{@/each}
	{@/if}
</script>
</c:if>
<div class="tbl detail-tbn2">
	<%-- <div class="tbl-cell"><a id="J_gocart" class="btn-buy btn-buy-def" href="${base}/cart/list.htm"><span></span>前往购物车</a></div> --%>
	<div class="tbl-cell"><a id="J_directorder" class="btn-buy btn-buy-def"><span></span>立即购买</a></div>
	<div class="tbl-cell"><a id="J_add_cart" class="btn-cart btn-cart-def"><span></span>加入购物车</a></div>
</div>
<div id="J_btn_tips" style="display:none;">
	<div class="btn_tips_mask"></div>
	<div class="btn_tips">
		<div class="btn_tips_message" id="J_btn_tips_message"></div>
		<div class="btn_tips_btns tbl">
			<div class="btn_tips_btn tbl-cell"><a href="javascript:$('#J_btn_tips').hide();">再逛逛</a></div>
			<div class="btn_tips_btn tbl-cell"><a href="${base}/cart/list.htm">去结算</a></div>
		</div>
	</div>
</div>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>
