<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<%-- <script charset="utf-8" src="${base}/resources/common/editor/kindeditor-min.js"></script>
<script charset="utf-8" src="${base}/resources/common/editor/lang/zh_CN.js"></script>
<link rel="stylesheet" href="${base}/resources/common/editor/themes/default/default.css" /> --%>
<script>
//	var editor;
//	KindEditor.ready(function(K) {
//		editor = KindEditor.editor({
//			uploadJson : '${KindEditor_uploadJson}',
//			fileManagerJson : '${KindEditor_fileManagerJson}',
//			allowFileManager : true
//		});
//
//		K('#btnLogo').click(function() {
//			editor.loadPlugin('image', function() {
//				editor.plugin.imageDialog({
//					showRemote : true,
//					//imageUrl : K('#logo').val(),
//					//imageUrl : mini.get("logo").getValue(),
//					clickFn : function(url, title, width, height, border, align) {
//						//console.log(K('#logo$value'));
//						//K('input[name="logo"]').val(url);
//						mini.get("logo").setValue(url);
//						editor.hideDialog();
//					}
//				});
//			});
//		});
//	});

	$(function($){
		var $specificationTable = $("#specificationTable");
		var $addSpecificationValueButton = $("#addSpecificationValueButton");
		var $deleteSpecificationValue = $("a.deleteSpecificationValue");
		var specificationValueIndex = ${valueSize};

		// 增加规格值
		$addSpecificationValueButton.click(function() {
			var html = [
				'<tr class="specificationValueTr">',
				'	<td></td>',
				'	<td><input type="text" name="tbShopSpecificationValues[' + specificationValueIndex + '].name" maxlength="200" class="text specificationValuesName" /></td>',
				//'	<td>',
				//'		<span class="fieldSet">',
				//'			<input type="text" name="specificationValues[' + specificationValueIndex + '].image" maxlength="200" class="text specificationValuesImage" disabled="disabled" />',
				//'			<input type="button" class="button browserButton" value="选择图片" disabled="disabled" />',
				//'		</span>',
				//'	</td>',
				'	<td><input type="text" name="tbShopSpecificationValues[' + specificationValueIndex + '].displayorder" maxlength="9" class="text specificationValuesOrder" style="width: 40px;" /></td>',
				'	<td><a href="javascript:;" class="deleteSpecificationValue">[删除]</a></td>',
				'</tr>'];
			$specificationTable.append(html.join(''));//.find("input.browserButton:last").browser();
			specificationValueIndex ++;
		});

		// 删除规格值
		$(document).on("click","a.deleteSpecificationValue", function() {
			if (!confirm("删除确认？")){
				return false;
			}
			var $this = $(this);
			if ($specificationTable.find("tr.specificationValueTr").size() <= 1) {
				alert('必须至少保留一个规格值');
			} else {
				$this.closest("tr").remove();
			}
		});
	});
</script>
<style type="text/css">
.text {
	cursor: text;
	height: 19px;
	line-height: 19px;
	font-size: 9pt;
	border: 0;
	padding: 0;
	margin: 0;
	padding: 0;
	_position: absolute;
	_left: 2px;
	_top: 0px;

	background: #FFF;
	border: solid 1px #A5ACB5;
}
</style>
</head>
<body>
<div class="mini-fit" style="padding:10px;">
  <form id="form1" onsubmit="return false">
    <input name="id" class="mini-hidden" value="${specification.id}" />
    <input name="createDate" class="mini-hidden" value="${specification.createDate}" />
	  <table style="table-layout:fixed;">
		<tr>
		  <td style="width:70px;">名称：</td>
		  <td><input name="name" class="mini-textbox" required="true" requiredErrorText="名称不能为空" value="${specification.name}" /></td>
		</tr>
		<tr>
		  <td>备注：</td>
		  <td><input name="remark" id="remark" class="mini-textbox" value="${specification.remark}" /></td>
		</tr>
		<tr>
		  <td>排序：</td>
		  <td><input name="displayorder" class="mini-textbox" vtype="int" value="${specification.displayorder}" /></td>
		</tr>
		<%-- <tr>
		  <td>LOGO：</td>
		  <td><input name="logo" id="logo" class="mini-textbox" /> <a class="mini-button" iconCls="icon-add" plain="true" id="btnLogo">上传</a></td>
		</tr> --%>
	  </table>
	  <table style="table-layout:fixed;" id="specificationTable">
		<tr>
		  <td style="width:70px;"></td>
		  <td cols="3"><a href="javascript:;" id="addSpecificationValueButton" class="button">增加规格商品</a></td>
		</tr>
		<tr>
		  <th></th>
		  <th style="width:120px;">规格值</th>
		  <!-- <th style="width:220px;">规格值图片</th> -->
		  <th style="width:60px;">排序</th>
		  <th>删除</th>
		</tr>
		<c:forEach  items="${specification.tbShopSpecificationValues}" var="item" varStatus="varStatus" >
		<tr class="specificationValueTr">
		  <td><input type="hidden" name="tbShopSpecificationValues[${varStatus.index}].id" value="${item.id}" /></td>
		  <td><input type="text" name="tbShopSpecificationValues[${varStatus.index}].name" value="${item.name}" maxlength="200" class="text specificationValuesName" /></td>
		  <!-- <td><input name="ccccdisplayorder" class="mini-textbox" /></td> -->
		  <td><input type="text" name="tbShopSpecificationValues[${varStatus.index}].displayorder" value="${item.displayorder}" maxlength="9" class="text specificationValuesOrder" style="width: 40px;" /></td>
		  <td><c:if test="${fn:length(item.tbShopSkuSpecificationAndValues)>0}" var="hasSKU">[使用中]</c:if><c:if test="${!hasSKU}"><a href="javascript:;" class="deleteSpecificationValue">[删除]</a></c:if></td>
		</tr>
		</c:forEach>
	  </table>
  </form>
 </div>
</div>
<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
	<a class="mini-button" onclick="saveData()" style="width:60px;margin-right:20px;">保存</a>
	<a class="mini-button" onclick="closeWindow('cancel')" style="width:60px;">取消</a>
</div>

<script type="text/javascript">
	mini.parse();

	var form = new mini.Form("#form1");

	function saveData() {
		form.validate();
		if (form.isValid() == false) {return;}

		var $specificationValuesName = $(".specificationValuesName");
		var hasValue = false;
		$.each($specificationValuesName,function(){
			if ($(this).val()!=''){
				hasValue = true;
				return false;
			}
		});
		if (!hasValue){
			alert('规格值不能为空');
			return;
		}

		//editor.sync();
		//var data = form.getData(true,true);
		var data = $("form").serialize();

		$.ajax({
			url: "update.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
//					miniExt.win.callback();
//					closeWindow();
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
