<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<title>订单支付 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<style type="text/css">
	body{background:#f8f8f8}
	section{min-height:400px;}
	.title{background:#efefef;border-bottom: 1px solid #CCC;}
	.info{background:#FFF;border-bottom: 1px solid #CCC;}
	.tbl-cell{text-align:center;padding:10px 5px;}
	.cell33{width:33%;}
	.dl1 {margin:20px 0 20px;}
	.unpaid {padding: 15px 0 10px;border-radius: 5px;width: 90%;margin: 20px auto 0;border: 1px solid #CCC;background:#fff;text-align:center;}
	.unpaid strong{color:#C00;font-size:26px;font-weight:bold;margin:0 5px;}
	.unpaid .text{color:#aaa;padding-top: 5px;}
</style>
</head>

<body>
<c:set var="header_title" value="订单支付" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section>
	<div class="tbl title">
		<div class="tbl-cell cell33">订单号</div>
		<div class="tbl-cell cell33">会员号</div>
		<div class="tbl-cell">收货人</div>
	</div>
	<div class="tbl info">
		<div class="tbl-cell cell33">${order.sn}</div>
		<div class="tbl-cell cell33">${order.memberUsername}</div>
		<div class="tbl-cell">${order.name}</div>
	</div>
	<div class="tbl title">
		<div class="tbl-cell cell33">商品金额</div>
		<div class="tbl-cell cell33">运费金额</div>
		<div class="tbl-cell">订单总额</div>
	</div>
	<div class="tbl info">
		<div class="tbl-cell cell33">￥<fmt:formatNumber value="${order.totalPrice+order.adjustAmount}" pattern="#,##0.00"/></div>
		<div class="tbl-cell cell33">￥<fmt:formatNumber value="${order.shippingFee}" pattern="#,##0.00"/></div>
		<div class="tbl-cell red2"><strong>￥<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong></div>
	</div>
	<div class="unpaid">
		<div>￥<strong><fmt:formatNumber value="${order.unpaidAmount}" pattern="#,##0.00"/></strong>元</div>
		<div class="text">未付金额</div>
	</div>
	<c:choose>
		<c:when test="${order.paymentStatus != 'unpaid'}"><c:set var="tips" value="该订单已付过款！"/></c:when>
		<c:when test="${order.orderStatus == 'cancelled'}"><c:set var="tips" value="该订单已取消！"/></c:when>
		<c:when test="${order.orderStatus == 'completed'}"><c:set var="tips" value="该订单已完成！"/></c:when>
		<%-- <c:when test="${order.orderStatus == 'unconfirmed'}"><c:set var="tips" value="该订单未确认金额，请联系客服！"/></c:when> --%>
	</c:choose>
	<div class="dl1">
		<dl>
		<c:choose>
			<c:when test="${not empty tips}">
			<dt class="message">${tips}</dt>
			</c:when>
			<c:when test="${fn:length(payments)==0}">
			<dt class="message">未开通在线支付</dt>
			</c:when>
			<c:otherwise>
			<dt>选择支付方式</dt>
			<c:forEach items="${payments}" var="payment">
			<dd class="icon ${payment.icon}"><a href="${base}${payment.url}">${payment.text}</a></dd>
			</c:forEach>
			</c:otherwise>
		</c:choose>
		</dl>
	</div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>