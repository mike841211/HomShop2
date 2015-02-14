<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>商品列表 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<script type="text/javascript" src="${base}/resources/mobile/default/js/jquery.pager.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/shop.js"></script>
<script type="text/javascript">
var params = {};
<c:forEach items="${param}" var="p">
params["${p.key}"]="${p.value}";</c:forEach>
</script>
<script type="text/javascript" src="${base}/resources/common/js/juicer-min.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.lazyImage.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/idangerous.swiper.min.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/common/js/idangerous.swiper.css">
<script type="text/javascript" src="${base}/resources/mobile/default/js/swiperlist.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/list.css">
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/swiperlist.css">
</head>

<body>
<c:set var="header_title" value="商品分类" scope="request" />
<jsp:include page="../../default/inc.header.jsp" />
<jsp:include page="../inc.search.jsp" />
<div class="loader">Loading...</div>
<div class="btn-filter list_float"></div>
<div id="J_filter" class="filter">
	<div class="filter-box-mask"></div>
	<div class="filter-box">
		<div class="filter-box-arrow"></div>
		<div class="filter-box-content">
		<div class="tbl box-shadow">
			<div class="tbl-cell filter-tab on"><span>分类</span></div>
		</div>
		<c:set var="categories" value="${ElUtil.getSubCategories('', false)}"/>
		<ul class="filter-ul-lst cate-ul on">
			<c:forEach items="${categories}" var="category" varStatus="status">
			<li class="filter-ul-li">
				<a>${category.name}</a>
				<div>
					<ul data-prop="cid">
						<li><a data-id="${category.id}"${param.cid==category.id ? ' class="on"' : ''}>全部</a></li>
						<c:set var="subCategories" value="${ElUtil.getSubCategories(category.id,false)}"/>
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
<a name="listTop"></a>
<div class="tabs-outter">
  <div class="tabs">
	  <c:set var="categories" value="${ElUtil.getSubCategories(param.cid,true)}"/>
	  <c:forEach items="${categories}" var="category" varStatus="status" begin="0">
	  <a id="tab_${category.id}" data-cid="${category.id}"<c:if test="${status.first}"> class="active"</c:if>>${category.name}</a>
	  </c:forEach>
	  <%-- <a class="btn-filter btn-filter-a" id="J_abtn_filter">更多</a> --%>
  </div>
</div>
<div class="swiper-container">
	<div class="swiper-wrapper">
	<c:forEach items="${categories}">
	  <div class="swiper-slide">
		<div class="search-lst">
			<ul class="product-lst">
			</ul>
			<div class="pagination"></div>
		</div>
	  </div>
	</c:forEach>
	</div>
</div>
<script id="J_li" type="text/template">
	{@if data==null || data.length==0}
		<%-- <li class="product-li product-li-empty">暂无数据</li> --%>
	{@else}
		{@each data as item,index}
		<li class="product-li">
			<a href="${base}/product/item/\${item.id}.htm" class="product-a">
				<span class="product-img lazyImageBg">
					<img class="lazyImage" src="${base}/resources/common/images/blank.gif" _src="{@if item.sampleImage!=null && item.sampleImage!=''}\${item.sampleImage}{@else}${noimage}{@/if}" alt="\${item.name}">
					{@if item.isFreeShipping=='1'}<span class="hint hint2 hint-lb hint-fs">免邮</span>{@/if}
				</span>
				<span class="product-info">
					<strong class="l2h">\${item.name}</strong>
					<span class="l2h slogan"><span class="red2 elps">\${item.slogan}</span></span>
					<span><strong class="red2">￥\${item.price|formatMoney}</strong> <c:if test="${CacheUtil.getConfig('SHOP_ISSHOWMARKETPRICE')=='1'}"><del class="gray f12">￥\${item.marketPrice|formatMoney}</del></c:if>
					{@if item.isNew=='1'}<em class="hint hint-n">新</em>{@/if}
					{@if item.isHot=='1'}<em class="hint hint-h">热</em>{@/if}
					{@if item.isPromotion=='1'}<em class="hint hint-p">促</em>{@/if}
					{@if item.isRecomend=='1'}<em class="hint hint-r">荐</em>{@/if}
					</span>
				</span>
			</a>
		</li>
		{@/each}
	{@/if}
</script>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>