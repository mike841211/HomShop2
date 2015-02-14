<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script type="text/javascript" src="${base}/resources/admin/js/shipping_method.js"></script>
<script type="text/javascript">
var province = {<c:forEach items="${province}" var="item" varStatus="status">"${item.code}":"${item.name}",</c:forEach>"000000":"海外"};
</script>
<style type="text/css">
	.line {margin:5px 0;}
	.line span {display: inline-block;width:5em;}
	.main {width:850px;border:1px solid #999; padding:0px 5px;}
	.default {padding:5px; margin:8px 0px 5px; border: 1px solid #9fcd75;background: #f1fde9;}
	table{border-collapse:collapse;width:100%}
	table, td, th{border:1px solid #cccccc; text-align:center;}
	.add,.delete {color: #36c; cursor:pointer;}
	.edit {position: absolute;bottom: 3px;right: 10px;color: #36c; cursor:pointer;}
	#J_province {position:absolute;width:400px;border:1px solid red;background-color:#FFFFFF;padding:5px;display:none;}
	 ul{margin:0px; padding:0px;}
	 li{list-style: none;display: block;float: left;width: 4em;height: 2em;overflow: hidden;}
	.area { padding-right:3em;}
	.allarea {text-align: left;display:none;}
	.cellarea { position: relative; padding: 3px 5px;}
	.input { width:4em; text-align: right;padding:1px 3px;}
	.detail_row:hover {background: #FCFDD7;}
</style>
</head>
<body>
<div class="mini-fit" style="padding:10px 20px 10px 10px;">
	<form id="form1">
		<input type="hidden" name="id" value="${shippingMethod.id}" />
		<input type="hidden" name="createDate" value="${shippingMethod.createDate}" />
		<div class="line"><span>配送方式：</span><input type="text" name="name" id="name" value="${shippingMethod.name}"/></div>
		<div class="line"><span>备注：</span><input type="text" name="remark" value="${shippingMethod.remark}"></div>
		<div class="line"><span>排序：</span><input type="text" class="input" name="displayorder" value="${shippingMethod.displayorder}"><label><input type="checkbox" name="enabled" value="1" ${shippingMethod.enabled=='1' ? 'checked' : ''}>启用</label></div>
		  <div style="color:gray;">除指定地区外，其余地区的运费采用“默认运费”</div>
		  <div class="main">
			<div class="default"><b>默认运费：</b>
			  首重
			  <input type="text" class="input" name="firstWeight" id="firstWeight" maxlength="18" autocomplete="off" value="${shippingMethod.firstWeight}" />
			  克，
			  <input type="text" class="input" name="firstPrice" id="firstPrice" maxlength="18" autocomplete="off" value="${shippingMethod.firstPrice}" />
			  元， 续重每
			  <input type="text" class="input" name="continueWeight" id="continueWeight" maxlength="18" autocomplete="off" value="${shippingMethod.continueWeight}" />
			  克， 增加运费
			  <input type="text" class="input" name="continuePrice" id="continuePrice" maxlength="18" autocomplete="off" value="${shippingMethod.continuePrice}" />
			  元
			</div>
			<div id="J_details" style="display:${fn:length(shippingMethod.tbShopShippingMethodDetails)>0 ? '' : 'none'};">
			  <table id="J_details_table">
				<tr bgcolor="#eeeeee">
				  <td>配送到</td>
				  <td width="80">首重(克)</td>
				  <td width="80">首重费(元)</td>
				  <td width="80">续重(克)</td>
				  <td width="80">续重费(元)</td>
				  <td width="60">操作</td>
				</tr>
				<c:set var="J_all_areaCode" value=""/>
				<c:set var="J_all_areaName" value=""/>
				<c:forEach items="${shippingMethod.tbShopShippingMethodDetails}" var="item">
				<c:set var="J_all_areaCode" value="${J_all_areaCode},${item.areaCode}"/>
				<c:set var="J_all_areaName" value="${J_all_areaName},${item.areaName}"/>
				<tr class="detail_row">
				  <td class="cellarea" style="text-align:left;"><span class="area">${item.areaName}</span><span class="allarea"></span><span class="edit">编辑</span>
				  <input type="hidden" name="areaCodes" class="J_row_areaCode" value="${item.areaCode}" /><input type="hidden" name="areaNames" class="J_row_areaName" value="${item.areaName}" /></td>
				  <td><input type="text" class="input" name="firstWeights" maxlength="18" autocomplete="off" value="${item.firstWeight}" /></td>
				  <td><input type="text" class="input" name="firstPrices" maxlength="18" autocomplete="off" value="${item.firstPrice}" /></td>
				  <td><input type="text" class="input" name="continueWeights" maxlength="18" autocomplete="off" value="${item.continueWeight}" /></td>
				  <td><input type="text" class="input" name="continuePrices" maxlength="18" autocomplete="off" value="${item.continuePrice}" /></td>
				  <td><span class="delete">删除</span></td>
				</tr>
				</c:forEach>
			  </table>
			</div>
			<div style="padding:5px 3px;"><span class="add">为指定地区设置运费</span>
			  <input type="hidden" id="J_all_areaCode" value="${fn:substringAfter(J_all_areaCode,',')}" />
			  <input type="hidden" id="J_all_areaName" value="${fn:substringAfter(J_all_areaName,',')}" />
			</div>
		  </div>
	</form>
	<div id="div_template" style="display:none;">
	  <table id="tbl_template">
		<tr class="detail_row">
		  <td class="cellarea" style="text-align:left;"><span class="area">未添加地区</span><span class="allarea"></span><span class="edit">编辑</span>
		  <input type="hidden" name="areaCodes" class="J_row_areaCode" /><input type="hidden" name="areaNames" class="J_row_areaName" /></td>
		  <td><input type="text" class="input" name="firstWeights" maxlength="18" autocomplete="off" value="1000" /></td>
		  <td><input type="text" class="input" name="firstPrices" maxlength="18" autocomplete="off" /></td>
		  <td><input type="text" class="input" name="continueWeights" maxlength="18" autocomplete="off" value="1000" /></td>
		  <td><input type="text" class="input" name="continuePrices" maxlength="18" autocomplete="off" /></td>
		  <td><span class="delete">删除</span></td>
		</tr>
	  </table>
	</div>
	<div id="J_province">
	  <ul></ul>
	  <div style="clear:both;margin-top:5px;"><input type="button" class="btn_select" value="确定" /><input type="button" class="btn_cancel" value="取消" /></div>
	</div>
</div>
<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
	<a class="mini-button" onclick="saveData()" style="width:60px;margin-right:20px;">保存</a>
	<a class="mini-button" onclick="closeWindow('cancel')" style="width:60px;">取消</a>
</div>

<script type="text/javascript">
	mini.parse();

	function saveData() {
		if ($.trim($('#name').val())==''){
			alert('请填写配送方式名称');
			return;
		}
		if (!$.isNumeric($('#firstWeight').val())){
			alert('请填写默认首重');
			return;
		}
		if (!$.isNumeric($('#firstPrice').val())){
			alert('请填写默认首重费');
			return;
		}
		if (!$.isNumeric($('#continueWeight').val())){
			alert('请填写续重');
			return;
		}
		if (!$.isNumeric($('#continuePrice').val())){
			alert('请填写默认续重费');
			return;
		}
		var hasEmpty = false;
		$('#J_details_table').find('tr').each(function(i){
			if(i==0){return true;}
			if ($.trim($(this).find(".J_row_areaCode").val())==''){
				hasEmpty = true;
				alert('第 '+i+' 行没有选择地区');
				return false; //跳出循环
			}
			var inputs = $(this).find(".input");
			if (!$.isNumeric(inputs.eq(0).val())){
				alert('第 '+i+' 行没有填写首重');
				hasEmpty = true;
				return false;
			} else if (!$.isNumeric(inputs.eq(1).val())){
				alert('第 '+i+' 行没有填写首重费');
				hasEmpty = true;
				return false;
			} else if (!$.isNumeric(inputs.eq(2).val())){
				alert('第 '+i+' 行没有填写续重');
				hasEmpty = true;
				return false;
			} else if (!$.isNumeric(inputs.eq(3).val())){
				alert('第 '+i+' 行没有填写续重费');
				hasEmpty = true;
				return false;
			}
		})
		if (hasEmpty){
			return;
		}

		$.ajax({
			url: "update.do",
			type: "POST",
			dataType: "json",
			data: $("form").serialize(),
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					miniExt.win.callback();
					closeWindow();
				}else{
					alert(json.message);
				}
			}
		});
	}

	function closeWindow(action) {
		miniExt.win.close(action);
	}
</script>
</body>
</html>
