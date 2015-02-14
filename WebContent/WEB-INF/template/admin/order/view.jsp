<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script type="text/javascript">
try {
	var WScriptShell = new ActiveXObject("WScript.Shell");
	WScriptShell.RegWrite("HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\header", "");
	WScriptShell.RegWrite("HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\footer", "");
} catch(e) {

}
</script>
<style type="text/css">
/* reset */
html,body,h1,h2,h3,h4,h5,h6,div,dl,dt,dd,ul,ol,li,p,blockquote,pre,hr,figure,table,caption,th,td,form,fieldset,legend,input,button,textarea,menu{margin:0;padding:0;}
header,footer,section,article,aside,nav,hgroup,address,figure,figcaption,menu,details{display:block;}
table{border-collapse:collapse;border-spacing:0;}
caption,th{text-align:left;font-weight:normal;}
html,body,fieldset,img,iframe,abbr{border:0;}
del,ins,u,s,a,a:hover{text-decoration:none;}
body,textarea,input,button,select,keygen,legend{font:12px/1.5 arial,\5b8b\4f53;color:#333;outline:0;}
body{background:#fff;line-height: 20px;}
a,a:hover{color:#333;}
a:hover{text-decoration:underline;}

.font1,.font2{color:#C00}


.tablepanel{padding:10px; width:800px;margin:0 auto;}
.table{width: 100%;margin:0 auto;}
.table th, .table td {padding: 8px;line-height: 20px;border-top: 1px solid #ddd;font-family:Arial, Helvetica, sans-serif;}
.table th {font-weight: bold;border-top: 0;}

#div_print{text-align:center;}
a.button {float:none;}
.print_show {display:none;}
@media print {
	th,td{color:#000;}
	.print_hide {display:none;}
	.print_show {display:block;}
}
</style>
</head>

<body>
<div class="tablepanel print_hide">
	<table class="table">
		<thead>
		  <tr>
			<th>NO.${order.sn}</th>
		  </tr>
		</thead>
	</table>
	<table class="table">
		<thead>
		  <tr>
			<th width="80"></th>
			<th>名称</th>
			<th width="120">编号</th>
			<th width="80">单价</th>
			<th width="80">数量</th>
			<th width="80">小计</th>
		  </tr>
		</thead>
		<tbody>
		<c:forEach items="${order.tbShopOrderItems}" var="item">
		  <tr>
			<td><a href="${base}/product/item/${item.productId}.htm" target="_blank"><img src="${item.imagePath}" width="60" alt=""></a></td>
			<td><a href="${base}/product/item/${item.productId}.htm" target="_blank">${item.name} <span class="font1">[${item.specifications}]</span></a></td>
			<td>${item.sn}</td>
			<td class="price"><fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></td>
			<td class="quantity"><fmt:formatNumber value="${item.quantity}" pattern="#,##0"/></td>
			<td class="subtotal"><fmt:formatNumber value="${item.price*item.quantity}" pattern="#,##0.00"/></td>
		  </tr>
		</c:forEach>
		  <tr>
			<td colspan="6"></td>
		  </tr>
		</tbody>
	</table>
	<table class="table">
		<thead>
		  <tr>
			<th colspan="6">配送信息</th>
		  </tr>
		</thead>
		<tbody>
		  <tr>
			<td width="50">收货人：</td>
			<td>${order.name}</td>
			<td width="50">手　机：</td>
			<td width="100">${order.mobile}</td>
			<td width="60">固定电话：</td>
			<td width="100">${order.phone}</td>
		  </tr>
		  <tr>
			<td>地　址：</td>
			<td colspan="3">${order.address}</td>
			<td>配送方式：</td>
			<td>${order.shippingMethod}${order.freightCollect=='1' ? '(到付)' : ''}</td>
		  </tr>
		  <tr>
			<td>备　注：</td>
			<td colspan="5">${order.buyerRemark}</td>
		  </tr>
		  <tr>
			<td>状　态：</td>
			<td>${order.orderStatus.name}-${order.paymentStatus.name}-${order.shippingStatus.name}</td>
			<td colspan="4" align="right">商品金额(<span class="font2"><fmt:formatNumber value="${order.totalPrice+order.adjustAmount}" pattern="#,##0.00"/></span>) + 运费(<span class="font2"><fmt:formatNumber value="${order.shippingFee}" pattern="#,##0.00"/></span>) = <span class="font1"><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span> 元</td>
		  </tr>
		  <c:if test="${not empty order.paymentMethod}">
		  <tr>
			<td colspan="6">付款方式：${order.paymentMethod.name} <c:if test="${not empty order.paymentCode}"> ，付款单号：${order.paymentCode}</c:if></td>
		  </tr>
		  </c:if>
		  <tr>
			<td colspan="6"></td>
		  </tr>
		</tbody>
	</table>
	<div id="div_print" class="print_hide"><a href="javascript:print();" class="button">打印</a></div>
</div>
<div class="tablepanel print_show">
	<table class="table">
		<thead>
		  <tr>
			<th>NO.${order.sn}</th>
		  </tr>
		</thead>
	</table>
	<table class="table">
		<thead>
		  <tr>
			<th colspan="4">订单信息</th>
		  </tr>
		</thead>
		<tbody>
		  <tr>
			<td>订单编号：${order.sn}</td>
			<td>会员名称：${order.memberUsername}</td>
			<td width="150">订购日期：${fn:substring(order.createDate,0,10)}</td>
			<td width="130">打印日期：<fmt:formatDate value="<%= new Date() %>" pattern="yyyy-MM-dd"/></td>
		  </tr>
		  <tr>
			<td colspan="4"></td>
		  </tr>
		</tbody>
	</table>
	<table class="table">
		<thead>
		  <tr>
			<th>名称</th>
			<th width="120">编号</th>
			<th width="80">单价</th>
			<th width="80">数量</th>
			<th width="80">小计</th>
		  </tr>
		</thead>
		<tbody>
		<c:forEach items="${order.tbShopOrderItems}" var="item">
		  <tr>
			<td>${item.name} [${item.specifications}]</td>
			<td>${item.sn}</td>
			<td class="price"><fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></td>
			<td class="quantity"><fmt:formatNumber value="${item.quantity}" pattern="#,##0"/></td>
			<td class="subtotal"><fmt:formatNumber value="${item.price*item.quantity}" pattern="#,##0.00"/></td>
		  </tr>
		</c:forEach>
		  <tr>
			<td colspan="5"></td>
		  </tr>
		</tbody>
	</table>
	<table class="table">
		<thead>
		  <tr>
			<th colspan="6">配送信息</th>
		  </tr>
		</thead>
		<tbody>
		  <tr>
			<td width="50">收货人：</td>
			<td>${order.name}</td>
			<td width="50">手　机：</td>
			<td width="100">${order.mobile}</td>
			<td width="60">固定电话：</td>
			<td width="100">${order.phone}</td>
		  </tr>
		  <tr>
			<td>地　址：</td>
			<td colspan="3">${order.address}</td>
			<td>配送方式：</td>
			<td>${order.shippingMethod}${order.freightCollect=='1' ? '(到付)' : ''}</td>
		  </tr>
		  <tr>
			<td>备　注：</td>
			<td colspan="5">${order.buyerRemark}</td>
		  </tr>
		  <tr>
			<td></td>
			<td></td>
			<td colspan="4" align="right">商品金额(<span class="font2"><fmt:formatNumber value="${order.totalPrice+order.adjustAmount}" pattern="#,##0.00"/></span>) + 运费(<span class="font2"><fmt:formatNumber value="${order.shippingFee}" pattern="#,##0.00"/></span>) = <span class="font1"><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span> 元</td>
		  </tr>
		  <tr>
			<td colspan="6"></td>
		  </tr>
		</tbody>
	</table>
</div>
</body>
</html>