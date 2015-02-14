<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>填写订单 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<c:set var="isMemberLogined" value="${true==sessionScope.SESSION_MEMBER_ISLOGIN}"/>
<c:set var="multiple" value="${CacheUtil.getConfig('SHOP_SCORE_ORDERCREDIT_MULTIPLE')}"/>
<c:set var="USESCORE" value="${CacheUtil.getConfig('SHOP_ORDER_USESCORE')=='1'}"/>
<script type="text/javascript" src="${base}/resources/common/js/ChinaArea.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/shop.js"></script>
<script type="text/javascript">
$(function($){
	var multiple = ${multiple}; // 积分抵扣倍数(分)
	<%-- var multiple = parseInt("${multiple}"); --%>
	var memberScore = ${empty memberScore ? 0 : memberScore}; // 会员可用积分
	// 计算实付金额
	function calcUnpaidAmount() {
		var couponAmount = 0; // 优惠券抵扣，暂不计入
		var totalAmount = parseFloat($("#J_total_amount").html());
		var maxScoreAmount = totalAmount-couponAmount;
		var useScoreAmount = 0;
		<c:if test="${USESCORE && isMemberLogined}">
		var useScore = parseInt($('#J_useScore').val());
		useScoreAmount = useScore/multiple/100;
		if (useScoreAmount>maxScoreAmount){useScoreAmount=maxScoreAmount;}
		var _useScore = Math.floor(useScoreAmount * 100 * multiple);
		if (_useScore!=useScore){
			$('#J_useScore').val(_useScore);
			$('#J_tips').html("本次最多可用 "+ _useScore +" 积分").show().delay(1000).fadeOut(500);
		}
		$("#J_useScoreAmount").val(useScoreAmount.toFixed(2));
		$("#J_useScoreAmount_txt").html((-useScoreAmount).toFixed(2));
		</c:if>
		$("#J_unpaid_amount").html((totalAmount-couponAmount-useScoreAmount).toFixed(2));
	}
	function setTotalAmount() {
		var totalPrice = parseFloat($("#J_totalPrice").val());
		var shippingFee = parseFloat($("#J_shippingFee").val());
		$("#J_total_amount").html((totalPrice+shippingFee).toFixed(2));
		calcUnpaidAmount();
	}
	$('#J_useScore').change(function(){
		var useScore = parseInt($(this).val());
		if (isNaN(useScore)){useScore=0;}
		else if (useScore<0){useScore=0;}
		else if (useScore>memberScore){
			useScore=memberScore;
			$('#J_tips').html("您的积分不足！").show().delay(1000).fadeOut(500);
		}
		useScore = useScore-useScore%multiple; // 整除
		$(this).val(useScore);
		calcUnpaidAmount();
	});

	function getShippingFee() {
		var areaCode = $('#province').val();
		//if(!areaCode){return;}
		var shippingMethodId = $('#J_shippingMethodId').val();
		if(!shippingMethodId){return;}
		var totalWeight = $('#J_totalWeight').val();
		$.ajax({
			url: "${base}/order/getShippingFee.do",
			data: {areaCode:areaCode, shippingMethodId:shippingMethodId, totalWeight:totalWeight},
			type: "POST",
			dataType: "json",
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					$("#J_shippingFee").val(json.result.shippingFee);
					$("#J_shipping_fee").html(json.result.shippingFee.toFixed(2));
					setTotalAmount();
				}else{
					alert(json.message);
				}
			}
		});
	}
	getShippingFee(); // 计算运费

	$('#province,#J_shippingMethodId').change(function(){
		getShippingFee();
	});

	ChinaArea.init({base:"${base}",code:"${member.areaCode}"});

	function verifyInput(id,message,value,undefined){
		if (value==undefined){
			value = $("#"+id).val().trim();
		}
		if (value==""){
			$('#J_tips').html(message).show().delay(2000).fadeOut(1000);
			return false;
		}
		return true;
	}
	$('#submit').click(function() {
		// 检查订单信息
		if (!verifyInput("name","请填写收货人")){return;}
		if (!verifyInput("mobile","请填写收货人手机")){return;}

		var province = $("#province").find("option:selected").text();
		if (!verifyInput("province","请填写省份",province)){return;}
		var city = $("#city").find("option:selected").text();
		if (!verifyInput("city","请填写城市",city)){return;}
		var district = $("#district").find("option:selected").text();
		//if (!verifyInput("city"),"请填写区县",district){return;}
		var _address = $('#_address').val().trim();
		if (!verifyInput("_address","请填写详细地址",_address)){return;}
		$('#address').val(province +' '+ city +' '+ district +' '+_address);

		$.post(
			'${base}/order/directorder/save.do',
			$('#form1').serialize(),
			function(json) {
				if (json.status=="success"){
					location.href="${base}/order/pay/"+json.result+".htm";
				}else{
					alert(json.message);
				}
			},
			"json"
		);
	});
});
</script>
<style type="text/css">
	.input1,.select1{width:100%;border: 1px solid #efefef;padding:2px;}
	.select1{background:#fff;margin-bottom:1px;}
	section{padding:10px;background:#fff;border-bottom:1px solid #cfcfcf;margin-bottom:0px;}
	.tbl-cell{padding:2px;}
	.cell1{width:80px;}
	.cell2{text-align:right;}
	.cell3{text-align:right;width:100px;}
	.cell4{text-align:right;padding-right:20px;}
	.cell-tips{text-align:center;color:#999;}
	.cell-tips span{color:#1261EE;font-weight:bold;}
	.details .tbl{border-bottom: 1px solid gray;padding-bottom: 3px;}
	.item-name{padding-top: 3px;}
	.caption{font-size:1.2em;font-weight:bold; padding-bottom:5px;}
	.new-abtn-type{display:block;padding:8px;border-radius:5px;background-color:#c00;font-size:14px;color:#fff;text-align:center;width:90%;margin:0px auto;}
	.search-lst {padding: 0;border-top: 1px solid #cfcfcf;}
	.product-name{color: #333;}
	.unlogined {display:block;text-align:center;color: #FFF;width: 200px;background: #6DB107;margin: 5px auto 5px;border-radius: 5px;line-height: 30px;}
	.useScore{width: 70px;margin:0 5px;border:none;text-align:center;color:green;font-size:20px;font-weight:bold;}
	#J_unpaid_amount {font-size:26px;font-weight:bold;}
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
<c:set var="header_title" value="填写订单" scope="request" />
<jsp:include page="../inc.header.jsp" />
<form id="form1">
<section>
	<div class="caption">收货人信息</div>
	<div class="tbl">
		<div class="tbl-cell cell1">收货人：</div>
		<div class="tbl-cell"><input type="text" class="input1" name="name" id="name" value="${member.name}"/></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">手　机：</div>
		<div class="tbl-cell"><input type="number" class="input1" name="mobile" id="mobile" value="${member.mobile}"/></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">固定电话：</div>
		<div class="tbl-cell"><input type="number" class="input1" name="phone" id="phone" value="${member.phone}"/></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">地　区：</div>
		<div class="tbl-cell"><input type="hidden" name="areaCode" id="areaCode" value="" />
		<select name="province" id="province" class="select1"></select><br>
		<select name="city" id="city" class="select1"></select><br>
		<select name="district" id="district" class="select1"></select></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">地　址：</div>
		<div class="tbl-cell"><input type="text" class="input1" value="${member.address}" id="_address" /><input type="hidden" name="address" id="address" value="" /></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">配送方式：</div>
		<div class="tbl-cell"><select name="shippingMethodId" id="J_shippingMethodId" class="select1">
			<c:set var="shippingMethods" value="${CacheUtil.getShippingMethods()}"/>
			<c:forEach items="${shippingMethods}" var="shippingMethod">
			<option value="${shippingMethod.value}">${shippingMethod.text}</option></c:forEach>
			</select></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">备　注：</div>
		<div class="tbl-cell"><input type="text" class="input1" name="buyerRemark" id="buyerRemark" value=""/></div>
	</div>
	<div class="tbl mg-t10">
		<div class="tbl-cell cell3">商品金额：</div>
		<div class="tbl-cell cell4">￥<span class="red2" id="total_price"><fmt:formatNumber value="${totalPrice}" pattern="#,##0.00"/></span> 元
		<input type="hidden" name="totalPrice" id="J_totalPrice" value="${totalPrice}"></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell3">运费金额：</div>
		<div class="tbl-cell cell4">￥<span class="red2" id="J_shipping_fee">0.00</span> 元 <!-- <span class="gray">须同时选择地区及配送方式</span> -->
		<input type="hidden" name="shippingFee" id="J_shippingFee" value="0"></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell3">合计金额：</div>
		<div class="tbl-cell cell4">￥<span class="red2" id="J_total_amount">0.00</span> 元</div>
	</div>
	<c:if test="${USESCORE && isMemberLogined}">
	<div class="tbl mg-t10">
		<div class="tbl-cell cell4">使用<input type="number" id="J_useScore" class="useScore" value="0">积分抵扣：￥<span class="green" id="J_useScoreAmount_txt">-0.00</span> 元<input type="hidden" name="useScoreAmount" id="J_useScoreAmount" value="0"></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell-tips">积分余额：<span><fmt:formatNumber value="${memberScore}" pattern="#,##0"/></span></div>
	</div>
	</c:if>
	<c:if test="${USESCORE && !isMemberLogined}">
	<div class="tbl">
		<div class="tbl-cell"><a class="unlogined" href="${base}/member/login.htm">会员登入可享积分优惠</a></div>
	</div>
	</c:if>
	<div class="tbl mg-t10">
		<div class="tbl-cell cell3">实付金额：</div>
		<div class="tbl-cell cell4">￥<span class="red2" id="J_unpaid_amount">0.00</span> 元</div>
	</div>
</section>
<section>
	<a href="javascript:void(0)" id="submit" class="new-abtn-type">提交订单</a>
</section>
<section class="details">
	<div class="caption">商品明细</div>
	<div class="search-lst">
		<ul class="product-lst">
            <li class="product-li">
                <a href="${base}/product/item/${sku.tbShopProduct.id}.htm" class="product-a">
                    <span class="product-img"><img src="${empty sku.tbShopProduct.sampleImage ? noimage : sku.tbShopProduct.sampleImage}" alt="${sku.tbShopProduct.name}"></span>
                    <span class="product-info">
                        <strong class="l2h product-name">${sku.tbShopProduct.name}</strong>
						<span class="l2h"><span class="sn">${sku.sn}</span> <span class="red2">${sku.specificationValueName}</span></span>
                        <span><strong class="red2">￥${sku.price}</strong> x ${param.quantity}<c:if test="${discount<1}"> x ${discount}(会员折扣)</c:if></span>
					</span>
                </a>
            </li>
		</ul>
    </div>
	<input type="hidden" name="totalWeight" id="J_totalWeight" value="${totalWeight}">
	<input type="hidden" name="skuid" id="skuid" value="${param.skuid}">
	<input type="hidden" name="quantity" id="quantity" value="${param.quantity}">
</section>
</form>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>
