<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>我的收藏 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<script type="text/javascript" src="${base}/resources/mobile/default/js/jquery.pager.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/shop.js"></script>
<script type="text/javascript">
$(function($){
	$(".pagination").pager({pagenumber: ${pager.pageIndex},pagecount: ${pager.pageCount},buttonClickCallback: Shop.pageChange});

	// 取消收藏
	$(".product-img img").click(function(){
		$(this).siblings('.remove').animate({height: '100%'}, 500);
	});
	$(".product-img b").click(function(){
		$(this).closest('.remove').animate({height: '3px'}, 500);
	});
	$(".product-img em").click(function(){
		if (!confirm("确定取消收藏吗？")){
			return;
		}
		var $this = $(this);
		var id = $this.data("id");
		$.ajax({
			url: base+"/member/favorite/delete.do",
			type: "POST",
			dataType: "json",
			data: {id:id},
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					$this.closest('li').remove();
				}else if (json.code=="unlogin"){
					location.href = base+"/member/login.htm"
				}else{
					alert(json.message);
				}
			}
		});
	});

	$(function($){
		$('.product-img img').load(function(){
			$(this).closest('.product-img').addClass('loaded');
		});
	});

});
</script>
<style type="text/css">
	.product-img .remove {position: absolute;bottom: 0;left: 0;width:100%;height:3px;background:#333;color: #FFF;opacity:0.618;text-align:center;}
	.product-img .remove em{display:block;margin-top: 30px;}
	.product-img .remove b{display:block;background: url(${base}/resources/mobile/${cfg_template}/css/icon.png) 5px -1562px no-repeat;width: 100px;height: 28px;margin: 10px auto 0;}
</style>
</head>

<body>
<c:set var="header_title" value="我的收藏" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="main">
	<div class="search-lst">
		<ul class="product-lst">
		  <c:forEach items="${pager.dataList}" var="item">
            <li class="product-li">
                    <span class="product-img">
                        <img src="${empty item.sampleImage ? noimage : item.sampleImage}" alt="${item.name}"/>
                        <c:if test="${item.isFreeShipping=='1'}"><span class="hint hint2 hint-lb hint-fs">免邮</span></c:if>
                        <span class="remove"><em data-id="${item.id}">取消收藏</em><b></b></span>
                    </span>
                    <a href="${base}/product/item/${item.id}.htm">
                    <span class="product-info">
                        <strong class="l2h">${item.name}</strong>
                        <span class="l2h slogan"><span class="red2 elps">${item.slogan}</span></span>
                        <span><strong class="red2">￥<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></strong> <del class="gray f12">￥<fmt:formatNumber value="${item.marketPrice}" pattern="#,##0.00"/></del></span>
                        <c:if test="${item.isNew=='1'}"><em class="hint hint-n">新</em></c:if>
                        <c:if test="${item.isHot=='1'}"><em class="hint hint-h">热</em></c:if>
                        <c:if test="${item.isPromotion=='1'}"><em class="hint hint-p">促</em></c:if>
                        <c:if test="${item.isRecomend=='1'}"><em class="hint hint-r">荐</em></c:if>
                    </span>
                    </a>
            </li>
    	  </c:forEach>
		</ul>
    </div>
	<div class="pagination"></div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>