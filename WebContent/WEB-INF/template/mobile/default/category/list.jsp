<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<title>商品分类 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<script type="text/javascript">
$(function() {
	$(".category-a:not([href])").click(function(){
		var $this = $(this);
		if ($this.hasClass('on')){
			$this.siblings('ul').hide();
			$this.removeClass('on');
		}else{
			$('.on').removeClass('on').siblings('ul').hide();
			$this.siblings('ul').fadeIn();
			$this.addClass('on');
		}
	});
});
</script>
</head>

<body>
<c:set var="header_title" value="商品分类" scope="request" />
<c:set var="header_menu" value="2" scope="request" />
<jsp:include page="../inc.header.jsp" />
<jsp:include page="../inc.search.jsp" />
<section class="main">
    <div class="category">
		<ul class="category-lst">
    		<li class="category-li">
    			<a class="category-a category-a-all" href="${base}/product/list.htm">查看所有商品</a>
    		</li>
			<c:set var="categories" value="${ElUtil.getSubCategories('', false)}"/>
			<c:forEach items="${categories}" var="category">
			<c:set var="subCategories" value="${ElUtil.getSubCategories(category.id,false)}"/>
			<c:set var="subCateSize" value="${fn:length(subCategories)}"/>
    		<li class="category-li">
				<c:if test="${subCateSize<1}">
    			<a class="category-a category-a-single" href="${base}/product/list.htm?cid=${category.id}"><span class="icon"></span>${category.name}</a>
	    		</c:if>
				<c:if test="${subCateSize>0}">
    			<a class="category-a"><span class="icon"></span>${category.name}</a>
    			<ul class="category2-lst">
					<li class="category2-li">
					<c:forEach items="${subCategories}" var="subCategory" varStatus="status">
					<a class="category2-a" href="${base}/product/list.htm?cid=${subCategory.id}"><span class="category2-bar"></span>${subCategory.name}</a>
					<c:if test="${status.count % 3 ==0}">
        			</li>
					<c:if test="${!status.last}">
					<li class="category2-li">
	    			</c:if>
	    			</c:if>
	    			</c:forEach>
					<c:if test="${subCateSize % 3 !=0}">
 					<c:forEach begin="0" end="${2-subCateSize%3}">
       				<a class="category2-a" href="javascript:void(0)"><span class="category2-bar"></span></a>
	    			</c:forEach>
	    			</c:if>
        			</li>
				</ul>
	    		</c:if>
    		</li>
    		</c:forEach>
		</ul>
	</div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>