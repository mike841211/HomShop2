<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
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
	.tips {display:block;background: #3150CC;}
	.error {background: #CC0000;}
	.wait {background: #FCA13E;}
	.success {background: #299C19;}
	.success {background: none;color:#299C19;font-size:36px;font-weight:bold;}
	.success a{background: none;color:#999;font-size:16px;font-weight:bold;margin:0 10px;}
</style>
<script type="text/javascript">
function isWxpayJsApiSupported() {
	var UserAgent = navigator.userAgent;
	var index = UserAgent.indexOf("MicroMessenger");
	if (index > -1) {
		var major = parseInt(UserAgent.substring(index + 15, index + 16));
		return major >= 5;
	}
	return false;
}
function getPaymentStatus(){
	$.ajax({
		url: "${base}/order/getPaymentStatus.do",
		type: "POST",
		dataType: "json",
		data: {id:"${param.id}"},
		cache: false,
		success: function (json) {
			if (json.status=="success"){
				if (json.result.paymentStatus=="paid"){
					$('#J_tips').addClass("success").html('恭喜，支付成功<br><a href="${base}/">返回首页</a><a href="${base}/order/list.htm">订单中心</a><a href="${base}/order/view/${param.id}.htm">查看订单</a>');
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
	if(isWxpayJsApiSupported()){
		document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
			try{
				WeixinJSBridge.invoke('getBrandWCPayRequest',${BrandWCPayRequestPackage},function(res){
					if(res.err_msg == "get_brand_wcpay_request:ok" ) {
						$('#J_tips').addClass("wait").html("支付操作完成，等待确认支付结果...");
						getPaymentStatus();
					}else{
						if(res.err_msg != "get_brand_wcpay_request:cancel" ) {
							alert('支付失败\n'+res.err_msg);
						}
						history.back();
					}
				 });
			}catch(e){
				$('#J_tips').addClass("error").html("对不起，调用支付接口失败！");
				alert(e);
			}
		});
	}else{
		$('#J_tips').addClass("error").html("对不起，当前浏览器不支持微信支付！");
	}
});
</script>
</head>

<body>
<div class="tips" id="J_tips">等待支付...</div>
</body>
</html>