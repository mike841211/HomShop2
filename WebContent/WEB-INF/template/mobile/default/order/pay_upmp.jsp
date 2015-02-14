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
<style type="text/css">
	.btn-pay {text-align: center;margin: 40px auto 20px;}
	.info {width: 90%;border-radius: 4px;line-height: 24px;opacity: 0.9;margin: 0 auto;background: #F1FDE9;border: 1px solid #9FCD75;color: #196F28;}
	.info div {padding: 10px 20px;}
</style>
</head>

<body>
<div class="btn-pay"><a href="uppay://uppayservice/?style=token&paydata=${paydata}"><img src="${base}/resources/mobile/default/images/upmp.png" alt="银联手机支付"/></a><br>点击支付</div>
<div class="info">
	<div>
		<b>提示：</b><br>
		1.如果点击支付无反应，请尝试安装银联安全支付控件。<br>【<a href="http://mobile.unionpay.com/getclient?platform=android&type=securepayplugin" target="_blank">安卓控件下载</a>】【<a href="http://mobile.unionpay.com/getclient?platform=ios&type=securepayplugin" target="_blank">苹果控件下载</a>】<br>
		2.已确认安装了安全控件仍无法支付可能您的浏览器版本不支持银联支付，请尝试使用其他浏览器付款。<br>
		3.微信浏览器不支持银联在线支付，请尝试使用其他浏览器付款<%-- ，请尝试点击右上角菜单在其他浏览器中打开 --%>。
	</div>
</div>
</body>
</html>