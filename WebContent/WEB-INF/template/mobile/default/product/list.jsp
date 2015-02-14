<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
<c:forEach items="${param}" var="p">params["${p.key}"]="${p.value}";</c:forEach>
</script>
<script type="text/javascript" src="${base}/resources/common/js/juicer-min.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.lazyImage.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/list.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/list.css">
<%-- <style type="text/css">
	.filter-box .filter-ul-li:nth-child(1) > a{background:none;text-align:center;}
</style> --%>
</head>

<body>
<c:set var="header_title" value="商品分类" scope="request" />
<c:set var="header_menu" value="2" scope="request" />
<jsp:include page="../inc.header.jsp" />
<jsp:include page="../inc.search.jsp" />
<a name="listTop"></a>
<section class="main">
	<div class="search-lst-navbar">
		<div class="tbl">
			<div class="tbl-cell btn${param.isNew=='1' ? ' active' : ''}" data-prop="isNew">新品</div>
			<div class="tbl-cell btn${param.isHot=='1' ? ' active' : ''}" data-prop="isHot"><span></span>热销</div>
			<div class="tbl-cell btn${param.isPromotion=='1' ? ' active' : ''}" data-prop="isPromotion"><span></span>促销</div>
			<div class="tbl-cell btn${param.isRecomend=='1' ? ' active' : ''}" data-prop="isRecomend"><span></span>推荐</div>
			<div class="tbl-cell btn-filter"><span></span>筛选<b></b></div>
		</div>
    </div>
	<div class="btn-filter list_float"></div>
	<div id="J_filter" class="filter">
		<div class="filter-box-mask"></div>
		<div class="filter-box">
			<div class="filter-box-arrow"></div>
			<div class="filter-box-content">
			<div class="tbl box-shadow">
				<div class="tbl-cell filter-tab on"><span>分类</span></div>
				<div class="tbl-cell filter-tab"><span>属性</span></div>
			</div>
			<c:set var="categories" value="${ElUtil.getSubCategories('', false)}"/>
			<ul class="filter-ul-lst cate-ul on">
				<%-- <li class="filter-ul-li">
					<a href="${base}/product/list.htm">查看所有商品</a>
				</li> --%>
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
			<ul class="filter-ul-lst attr-ul">
				<li class="filter-ul-li">
					<a>品牌</a>
					<div>
						<ul data-prop="bid">
							<li><a data-id=""${empty param.bid ? ' class="on"' : ''}>不限</a></li>
							<c:if test="${not empty param.cid}">
							<c:set var="brands" value="${ElUtil.getBrands(param.cid)}"/>
							<c:forEach items="${brands}" var="brand">
							<li><a data-id="${brand.id}"${param.bid==brand.id ? ' class="on"' : ''}>${brand.name}</a></li>
							</c:forEach>
							</c:if>
						</ul>
					</div>
				</li>
				<li class="filter-ul-li">
					<a>价格</a>
					<div>
						<ul data-prop="price">
							<li><a data-id=""${empty param.price ? ' class="on"' : ''}>不限</a></li>
							<li><a data-id="0,99"${param.price=='0,99' ? ' class="on"' : ''}>0-99</a></li>
							<li><a data-id="100,199"${param.price=='100,199' ? ' class="on"' : ''}>100-199</a></li>
							<li><a data-id="200,299"${param.price=='200,299' ? ' class="on"' : ''}>200-299</a></li>
							<li><a data-id="300,399"${param.price=='300,399' ? ' class="on"' : ''}>300-399</a></li>
							<li><a data-id="400,499"${param.price=='400,499' ? ' class="on"' : ''}>400-499</a></li>
							<li><a data-id="500,0"${param.price=='500,0' ? ' class="on"' : ''}>500以上</a></li>
						</ul>
					</div>
				</li>
			</ul>
			</div>
		</div>
    </div>
	<div class="search-lst">
		<ul class="product-lst">
		</ul>
		<div class="pagination"></div>
    </div>
</section>
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