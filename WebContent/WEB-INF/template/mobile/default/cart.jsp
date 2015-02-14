<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>购物车 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/cart.css">
<script type="text/javascript" src="${base}/resources/mobile/default/js/cart.js"></script>
<script type="text/javascript">
	$(function($){
		$('.product-img img').load(function(){
			$(this).closest('.product-img').addClass('loaded');
		});
	});
</script>
</head>

<body>
<c:set var="header_title" value="购物车" scope="request" />
<c:set var="header_menu" value="3" scope="request" />
<jsp:include page="inc.header.jsp" />
<section class="main">
	<div class="search-lst">
		<ul class="product-lst">
		  <c:forEach items="${cart.tbShopCartItems}" var="cartItem">
			<c:set value="${cartItem.tbShopSku}" var="sku"/>
			<c:set value="${sku.tbShopProduct}" var="product"/>
            <li class="product-li">
				<span class="product-img"><a href="${base}/product/item/${product.id}.htm" class="product-a"><img src="${empty sku.sampleImage ? empty product.sampleImage ? noimage : product.sampleImage : sku.sampleImage}" alt="${product.name}"></a></span>
				<span class="product-info">
					<strong class="l2h"><a href="${base}/product/item/${product.id}.htm" class="product-a">${product.name}</a></strong>
					<span class="l2h gray">规格：<span class="red2">${sku.specificationValueName}</span></span>
					<span class="l2h gray">编号：${sku.sn}</span>
					<span class="l2h">单价：<strong class="red2">￥<fmt:formatNumber value="${sku.price}" pattern="#,##0.00"/></strong> <del class="grey f12">￥<fmt:formatNumber value="${sku.marketPrice}" pattern="#,##0.00"/></del></span>
					<span class="l2h">数量：<input type="hidden" id="J_stock${cartItem.id}" value="${sku.stock}">
						 <a href="javascript:void(0)" class="redu J_redu" data-id="${cartItem.id}">-</a>
						 <input type="number" size="4" value="${cartItem.quantity}" id="J_number${cartItem.id}" class="common-input J_number" data-id="${cartItem.id}">
						 <a href="javascript:void(0)" class="add J_add" data-id="${cartItem.id}">+</a>
						 <a href="javascript:void(0)" class="btn J_del" data-id="${cartItem.id}">删除</a>
					</span>
				</span>
            </li>
    	  </c:forEach>
		</ul>
    </div>
	<div class="total">
		<p class="common-border">总数：<span id="J_total_quantity">--</span> <span id="J_clear" style="position: absolute;right: 10px;color:#0A5CC5;text-decoration: underline;">清空购物车</span></p>
		<p>总额：<span class="red2">￥<span id="J_total_price">-.--</span></span>  <span id="J_discount"></span></p>
	</div>
    <%-- <a href="javascript:void(0);" id="submit" class="new-abtn-type">去结算</a> --%>
	<div class="tbl detail-tbn2" style="">
		<div class="tbl-cell"><a class="btn-cart btn-cart-def" href="${base}/category/list.htm"><span></span>继续购物</a></div>
		<%-- <div class="tbl-cell"><a class="btn-clear btn-cart-def" href="${base}/category/list.htm"><span></span>清空购物车</a></div> --%>
		<div class="tbl-cell"><a class="btn-buy btn-buy-def" href="${base}/order/cartorder.htm"><span></span>去结算</a></div>
	</div>
</section>
<jsp:include page="inc.footer.jsp" />
</body>
</html>
