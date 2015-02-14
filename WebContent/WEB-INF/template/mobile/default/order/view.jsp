<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>订单详情 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<script type="text/javascript" src="${base}/resources/common/js/ChinaArea.js"></script>
<style type="text/css">
	.input1,.select1{width:100%;border: 1px solid #efefef;padding:2px;}
	.select1{background:#fff;margin-bottom:1px;}
	section{padding:10px;background:#fff;border-bottom:1px solid #cfcfcf;margin-bottom:0px;}
	.tbl-cell{padding:2px;}
	/*.cell1{width:80px;}*/
	.cell1{width:100px;}
	.cell2{text-align:right;}
	/*.cell3{text-align:right;width:100px;}*/
	/*.cell4{text-align:right;padding-right:20px;}*/
	.cell3{width:100px;}
	.cell4{text-align:right;}
	.details .tbl{border-bottom: 1px solid gray;padding-bottom: 3px;}
	.item-name{padding-top: 3px;}
	.caption{font-size:1.2em;font-weight:bold; padding-bottom:5px;}
	.search-lst {padding: 0;border-top: 1px solid #cfcfcf;}
	.product-name{color: #333;}
	.status{background:#efefef;border-bottom: 1px solid #cfcfcf;line-height: 1.6em;padding: 5px 10px}
	#unpaid_amount {font-size:26px;font-weight:bold;}
	.btn-comment {background: #6CB248;color:#fff;border-radius: 4px;padding: 2px 5px;position: absolute;right: 10px;top: 15px;}
	.status span {background: #466AFA;color:#fff;border-radius: 4px;padding: 2px 5px;}
</style>
<script type="text/javascript">
	$(function($){
		$('.product-img img').load(function(){
			$(this).closest('.product-img').addClass('loaded');
		});
	});
</script>
</head>

<body>
<c:set var="header_title" value="订单详情" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="status">
	<div class="tbl">
		<div class="tbl-cell relative">订单号：${order.sn}<br>
		<span class="orderStatus_${order.orderStatus}">${order.orderStatus.name}</span>
		<c:if test="${order.orderStatus != 'completed' && order.orderStatus != 'cancelled'}"> <span class="paymentStatus_${order.paymentStatus}">${order.paymentStatus.name}</span> <span class="shippingStatus_${order.shippingStatus}">${order.shippingStatus.name}</span></c:if>
		<c:if test="${CacheUtil.getConfig('SHOP_PRODUCT_COMMENT_ENABLED')=='1' && order.orderStatus=='completed'}"><a class="btn-comment" href="${base}/order/comment/${order.id}.htm">评价商品</a></c:if>
		</div>
	</div>
</section>
<section>
	<div class="caption">收货人信息</div>
	<div class="tbl">
		<div class="tbl-cell cell1">收 货 人：</div>
		<div class="tbl-cell">${order.name}</div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">手机号码：</div>
		<div class="tbl-cell">${order.mobile}</div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">固定电话：</div>
		<div class="tbl-cell">${order.phone}</div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">详细地址：</div>
		<div class="tbl-cell">${order.address}</div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">买家备注：</div>
		<div class="tbl-cell">${order.buyerRemark}</div>
	</div>
	<%-- <div class="tbl">
		<div class="tbl-cell cell1">卖家备注：</div>
		<div class="tbl-cell">${order.salerRemark}</div>
	</div> --%>
</section>
<section>
	<div class="caption">配送信息</div>
	<div class="tbl">
		<div class="tbl-cell cell1">配送方式：</div>
		<div class="tbl-cell">${order.shippingMethod}</div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">物流公司：</div>
		<div class="tbl-cell">${order.shippingCompany}</div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">物流单号：</div>
		<div class="tbl-cell">${order.shippingCode}</div>
	</div>
</section>
<section>
	<div class="caption">金额信息</div>
	<div class="tbl mg-t10">
		<div class="tbl-cell cell3">商品金额：</div>
		<div class="tbl-cell cell4">￥<span class="red2" id="total_price">${order.totalPrice}</span> 元</div>
	</div>
	<c:if test="${order.adjustAmount!='0.00'}">
	<div class="tbl">
		<div class="tbl-cell cell3">卖家调整：</div>
		<div class="tbl-cell cell4">￥<span class="red2">${order.adjustAmount}</span> 元</div>
	</div>
	</c:if>
	<div class="tbl">
		<div class="tbl-cell cell3">运费金额：</div>
		<div class="tbl-cell cell4">￥<span class="red2">${order.shippingFee}</span> 元</div>
	</div>
	<%-- <div class="tbl">
		<div class="tbl-cell cell3">合计金额：</div>
		<div class="tbl-cell cell4">￥<span class="red2">${order.totalAmount}</span> 元</div>
	</div> --%>
	<c:if test="${order.useScoreAmount!='0.00'}">
	<div class="tbl">
		<div class="tbl-cell cell3">积分抵扣：</div>
		<div class="tbl-cell cell4">￥<span class="green">-${order.useScoreAmount}</span> 元</div>
	</div>
	</c:if>
	<div class="tbl mg-t10">
		<div class="tbl-cell cell3">实付金额：</div>
		<div class="tbl-cell cell4">￥<span class="red2" id="unpaid_amount">${order.unpaidAmount}</span> 元</div>
	</div>
</section>
<section>
<c:choose>
	<c:when test="${order.orderStatus == 'completed'}"><a class="abtn bc-green">该订单已完成！</a></c:when>
	<c:when test="${order.orderStatus == 'cancelled'}"><a class="abtn bc-grayd">该订单已取消！</a></c:when>
	<%-- <c:when test="${order.orderStatus == 'unconfirmed'}"><c:set var="tips" value="该订单未确认金额，请联系客服！"/></c:when> --%>
	<c:when test="${order.paymentStatus == 'unpaid'}">
	<div class="tbl">
		<div class="tbl-cell"><a href="javascript:cancelled();" class="abtn bc-gray5">取消订单</a></div>
		<div class="tbl-cell"><a href="${base}/order/pay/${order.id}.htm" id="submit" class="abtn bc-red2">前往付款</a></div>
	</div>
	<script type="text/javascript">
	function cancelled(){
		if (confirm("确认取消此订单吗？")){
			$.ajax({
				url: base+"/order/cancelled.do",
				type: "POST",
				dataType: "json",
				data: {id:"${order.id}",status:"cancelled"},
				cache: false,
				success: function (json) {
					if (json.status=="success"){
						location.reload();
					}else if (json.code=="unlogin"){
						$('#J_tips').html("登入超时").show().delay(2000).fadeOut(1000);
					}else{
						alert(json.message);
					}
				}
			});
		}
	}
	</script>
	</c:when>
	<c:when test="${order.paymentStatus == 'paid' && order.shippingStatus == 'unshipped'}"><a class="abtn bc-orange">等待发货！</a></c:when>
	<c:when test="${order.paymentStatus == 'paid' && order.shippingStatus == 'shipped'}"><a href="javascript:completed();" class="abtn bc-red2">确认收货</a>
	<script type="text/javascript">
	function completed(){
		if (confirm("确认已收到商品")){
			$.ajax({
				url: base+"/order/completed.do",
				type: "POST",
				dataType: "json",
				data: {id:"${order.id}",status:"completed"},
				cache: false,
				success: function (json) {
					if (json.status=="success"){
						location.reload();
					}else if (json.code=="unlogin"){
						$('#J_tips').html("登入超时").show().delay(2000).fadeOut(1000);
					}else{
						alert(json.message);
					}
				}
			});
		}
	}
	</script>
	</c:when>
</c:choose>
</section>
<section class="details">
	<div class="caption">商品明细</div>
	<div class="search-lst">
		<ul class="product-lst">
			<c:forEach items="${order.tbShopOrderItems}" var="item">
            <li class="product-li">
                <a href="${base}/product/item/${item.productId}.htm" class="product-a">
                    <span class="product-img"><img src="${empty item.imagePath ? noimage : item.imagePath}" alt="${item.name}"></span>
                    <span class="product-info">
                        <strong class="l2h product-name">${item.name}</strong>
						<span class="l2h"><span class="sn">${item.sn}</span> <span class="red2">${item.specifications}</span></span>
                        <span><strong class="red2">￥${item.price}</strong> x ${item.quantity}<%-- <c:if test="${item.discount<1}"> x ${item.discount}(会员折扣)</c:if> --%></span>
					</span>
                </a>
            </li>
			</c:forEach>
		</ul>
    </div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>
