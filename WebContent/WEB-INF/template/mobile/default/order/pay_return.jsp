<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>订单支付 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/base.css">
<script type="text/javascript" src="${base}/resources/common/js/jquery-1.11.0.min.js"></script>
<style type="text/css">
	.tips {display:block;background: none;font-size:32px;font-weight:bold;}
	.error {color:#CC0000;}
	.wait {color:#3150CC;}
	.success {color:#299C19;}
	.success a,.error a,.wait a{color:#999;font-size:16px;margin:0 10px;}
</style>
<script type="text/javascript">
function getPaymentStatus(){
	$.ajax({
		url: "${base}/order/getPaymentStatus.do",
		type: "POST",
		dataType: "json",
		data: {id:"${id}"},
		cache: false,
		success: function (json) {
			if (json.status=="success"){
				if (json.result.paymentStatus=="paid"){
					$('#J_tips').addClass("success").html('恭喜，支付成功<br><a href="${base}/">返回首页</a><a href="${base}/order/list.htm">订单中心</a><a href="${base}/order/view/${id}.htm">查看订单</a>');
					//todo
				}else{
					setTimeout("getPaymentStatus()",10000);
				}
			}else{
				alert(json.message);
			}
		}
	});
}
$(function($){
<c:choose>
	<c:when test="${paymentType=='ALIPAY'}">
		<c:choose>
			<c:when test="${error}">
			$('#J_tips').addClass("error").html('验证失败');
			</c:when>
			<c:otherwise>
			$('#J_tips').addClass("wait").html('支付操作完成<br>等待确认支付结果...');
			getPaymentStatus();
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${paymentType=='UNIONPAY'}">
		<c:choose>
			<c:when test="${error}">
			$('#J_tips').addClass("error").html('${errorMessage}<br><a href="${base}/">返回首页</a><a href="${base}/order/list.htm">订单中心</a><a href="${base}/order/view/${id}.htm">查看订单</a>');
			</c:when>
			<c:otherwise>
			$('#J_tips').addClass("wait").html('支付操作完成<br>等待确认支付结果...');
			getPaymentStatus();
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>
});
</script>
</head>

<body>
<div class="tips" id="J_tips">等待支付...</div>
</body>
</html>