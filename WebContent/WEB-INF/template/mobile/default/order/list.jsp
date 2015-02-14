<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<title>我的订单 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<script type="text/javascript" src="${base}/resources/mobile/default/js/jquery.pager.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/shop.js"></script>
<script type="text/javascript">
$(function($){
	$(".pagination").pager({pagenumber: ${pager.pageIndex},pagecount: ${pager.pageCount},buttonClickCallback: Shop.pageChange});
});
</script>
<style type="text/css">
	section{min-height:400px;}
	.tbl{border-bottom:1px solid #efefef;position:relative;}
	.cell-h{font-weight:bold;padding:10px 0;}
	.list a{display: block;padding: 10px;}
	.cell-date{width:75px;}
	.cell-sn{width:90px;}
	.cell-price{width:85px;text-align:right;}
	.cell-status{width:70px;text-align:right;}
	.icon-arr {top:13px;}
	.pagination .tbl{border-bottom:0;}
</style>
</head>

<body>
<c:set var="header_title" value="我的订单" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section>
	<div class="tbl">
	<div style="padding:0 10px;">
		<div class="tbl-cell cell-h cell-date">日期</div>
		<div class="tbl-cell cell-h cell-sn">单号</div>
		<div class="tbl-cell cell-h cell-price">总额</div>
		<div class="tbl-cell cell-h cell-status">状态</div>
		<div class="tbl-cell"></div>
	</div>
	</div>
	<c:forEach items="${pager.dataList}" var="order">
	<div class="tbl list">
		<a href="view/${order.id}.htm">
		<div class="tbl-cell cell-date gray">${fn:substring(order.createDate,2,4)}/${fn:substring(order.createDate,5,7)}/${fn:substring(order.createDate,8,10)}</div>
		<div class="tbl-cell cell-sn">${order.sn}</div>
		<div class="tbl-cell cell-price red2">￥${order.totalAmount}</div>
		<c:choose>
		<c:when test="${order.orderStatus == 'completed' || order.orderStatus == 'cancelled'}">
		<div class="tbl-cell cell-status orderStatus_${order.orderStatus}">${order.orderStatus.name}</div>
		</c:when>
		<c:when test="${order.paymentStatus == 'unpaid'}">
		<div class="tbl-cell cell-status paymentStatus_${order.paymentStatus}">${order.paymentStatus.name}</div>
		</c:when>
		<c:when test="${order.shippingStatus == 'unshipped'}">
		<div class="tbl-cell cell-status shippingStatus_${order.shippingStatus}">${order.shippingStatus.name}</div>
		</c:when>
		<c:otherwise>
		<div class="tbl-cell cell-status orderStatus_${order.orderStatus}">${order.orderStatus.name}</div>
		</c:otherwise>
		</c:choose>
		<div class="tbl-cell"></div>
		</a>
		<span class="icon-arr"></span>
	</div>
	</c:forEach>
	<div class="pagination"></div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>