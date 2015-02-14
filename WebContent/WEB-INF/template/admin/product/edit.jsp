<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script charset="utf-8" src="${base}/resources/common/editor/kindeditor-min.js"></script>
<script>
	var editor;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="introduction"]', {
			uploadJson : '${KindEditor_uploadJson}',
			fileManagerJson : '${KindEditor_fileManagerJson}',
			allowFileManager : true,
			afterUpload : function(url) {
				addAttachments(url);
			}
		});

		// 缩略图直接用imageStore第一张
		// K('#btnLogo').click(function() {
		// 	editor.loadPlugin('image', function() {
		// 		editor.plugin.imageDialog({
		// 			showRemote : false,
		// 			//imageUrl : K('#logo').val(),
		// 			//imageUrl : mini.get("logo").getValue(),
		// 			clickFn : function(url, title, width, height, border, align) {
		// 				//console.log(K('#logo$value'));
		// 				//K('input[name="logo"]').val(url);
		// 				addAttachments(url);
		// 				//mini.get("logo").setValue(url);
		// 				editor.hideDialog();
		// 			}
		// 		});
		// 	});
		// });

		// 商品图片上传
		K('#btnUpload').click(function() {
			editor.loadPlugin('image', function() {
				editor.plugin.imageDialog({
					clickFn : function(url, title, width, height, border, align) {
						addAttachments(url);
						addImageStore(url);
						editor.hideDialog();
					}
				});
			});
		});
		K('#btnUploadBatch').click(function() {
			editor.loadPlugin('multiimage', function() {
				editor.plugin.multiImageDialog({
					clickFn : function(urlList) {
						K.each(urlList, function(i, data) {
							addAttachments(data.url);
							addImageStore(data.url);
						});
						editor.hideDialog();
					}
				});
			});
		});
	});
	function addAttachments(url){
		var attachments = mini.get("attachments");
		// var values = attachments.getValue().split("|");
		// if (values[0]=="") {values[0]=url;}
		// else{values.push(url);}
		// attachments.setValue(values.join("|"));
		var values = attachments.getValue();
		if (values=="") {values=url;}
		else{values += "|"+url;}
		attachments.setValue(values);
	}
	function addImageStore(url){
		var imageStore = mini.get("imageStore");
		var values = imageStore.getValue();
		if (values=="") {values=url;}
		else{values += "|"+url;}
		imageStore.setValue(values);

		var row = {};
		row['url'] = url;
		grid2.addRow(row);
	}

	function saveImageStore(){
		var imageStore = mini.get("imageStore");
		var rows = grid2.getData();
		if (rows.length==0) {
			imageStore.setValue('');
		}else{
			var values = [];
			for (var i = 0; i < rows.length; i++) {
				values.push(rows[i].url);
			};
			imageStore.setValue(values.join('|'));
		}
	}

	function saveSampleImage(){
		var sampleImage = mini.get("sampleImage");
		var rows = grid2.getData();
		if (rows.length==0) {
			sampleImage.setValue('');
		}else{
			sampleImage.setValue(rows[0].url);
		}
	}
</script>
<style type="text/css">
	.specifications {margin:0;padding:0;list-style-type:none;}
	.specifications li {float:left;width:150px;height: 22px;overflow: hidden;}
	.specifications .remark {color:#808080;}
</style>
</head>
<body>
<div class="mini-fit" style="padding:10px;">
<form id="form1" onsubmit="return false" style="height:100%;">
<div class="mini-fit">
	<input class="mini-hidden" name="id" id="id" value="${product.id}" />
	<input class="mini-hidden" name="attachments" id="attachments" value="${product.attachments}" />
	<input class="mini-hidden" name="sampleImage" id="sampleImage" value="${product.sampleImage}" />
	<input class="mini-hidden" name="imageStore" id="imageStore" value="${product.imageStore}" />
	<div id="tabs1" class="mini-tabs" activeIndex="0" style="height:100%;">
		<div name="first" title="基本信息">
		  <table style="table-layout:fixed;">
		    <tr>
		      <td>商品名称：</td>
		      <td colspan="3"><input name="name" id="name" class="mini-textbox" required="true" value="${product.name}" style="width:433px;" /></td>
		    </tr>
			<tr>
			  <td>商品广告词：</td>
			  <td colspan="3"><input name="slogan" id="slogan" class="mini-textbox" style="width:433px;" value="${product.slogan}" /></td>
			</tr>
		    <tr>
		      <td style="width:80px;">系统编号：</td>
		      <td style="width:220px;"><input name="syssn" id="syssn" class="mini-textbox" value="${product.syssn}" emptyText="自动分配" vtype="int" readonly /></td>
		      <td style="width:80px;">商品货号：</td>
		      <td><input name="sn" id="sn" class="mini-textbox" value="${product.sn}" /></td>
		    </tr>
		    <tr>
		      <td>单位：</td>
		      <td><input name="unit" id="unit" class="mini-textbox" value="${product.unit}" /></td>
		      <td>条码：</td>
		      <td><input name="barcode" id="barcode" class="mini-textbox" value="${product.barcode}" /></td>
		    </tr>
		    <tr>
		      <td>分类：</td>
			  <td><input name="tbShopProductCategory.id" id="category" class="mini-treeselect"
				  valueField="id" textField="name" popupWidth="200" showClose="true"
				  value="${product.tbShopProductCategory.id}" text="${product.tbShopProductCategory.name}" required="true"
				  url="../product_category/getProductCategoryTreeData.do"
				  /></td>
		      <td>品牌：</td>
			  <td><input name="tbShopBrand.id" id="brand" class="mini-combobox"
			  		showClose="true" valueFromSelect="true"
					valueField="value" textField="text" value="${product.tbShopBrand.id}"
					url="../brand/getAllForSelect.do"
				  />
			  </td>
		    </tr>
		    <tr>
		      <td>售价：</td>
		      <td><input name="price" class="mini-spinner" minValue="0" decimalPlaces="2" maxValue="9999999999999" value="${product.price}" /></td>
		      <td>市场价：</td>
		      <td><input name="marketPrice" class="mini-spinner" minValue="0" decimalPlaces="2" maxValue="9999999999999" value="${product.marketPrice}" /></td>
		    </tr>
		    <tr>
		      <td>描述模版：</td>
			  <td><input name="tbShopProductBaseinfo.id" id="productBaseinfo" class="mini-combobox"
			  		showClose="true" valueFromSelect="true"
					valueField="value" textField="text" value="${product.tbShopProductBaseinfo.id}"
					url="../product_baseinfo/getAllForSelect.do"
				  />
			  </td>
		      <td></td>
		      <td></td>
		      <%-- <td>运费模版：</td>
			  <td><input name="tbShopFreightTemplate.id" id="freightTemplate" class="mini-combobox"
			  		showClose="true" valueFromSelect="true"
					valueField="value" textField="text" value="${product.tbShopFreightTemplate.id}" required="true"
					url="../freight_template/getAllForSelect.do"
				  />
			  </td> --%>
		    </tr>
			<tr>
			  <td>标签：</td>
			  <td colspan="3">
			  	<input class="mini-checkbox" trueValue="1" falseValue="0" name="isNew" id="isNew" text="新品" value="${product.isNew}" checked="${product.isNew=='1'}" >
			  	<input class="mini-checkbox" trueValue="1" falseValue="0" name="isHot" id="isHot" text="热销" value="${product.isHot}" checked="${product.isHot=='1'}" >
			  	<input class="mini-checkbox" trueValue="1" falseValue="0" name="isPromotion" id="isPromotion" text="促销" value="${product.isPromotion}" checked="${product.isPromotion=='1'}" >
			  	<input class="mini-checkbox" trueValue="1" falseValue="0" name="isRecomend" id="isRecomend" text="推荐" value="${product.isRecomend}" checked="${product.isRecomend=='1'}" >
			  </td>
			</tr>
			<tr>
			  <td>设置：</td>
			  <td colspan="3">
			  	<input class="mini-checkbox" trueValue="1" falseValue="0" name="isSale" id="isSale" text="上架" value="${product.isSale}" checked="${product.isSale=='1'}" >
			  	<input class="mini-checkbox" trueValue="1" falseValue="0" name="isFreeShipping" id="isFreeShipping" text="免运费" value="${product.isFreeShipping}" checked="${product.isFreeShipping=='1'}" >
			  </td>
			</tr>
			<tr>
			  <td>搜索关键词：</td>
			  <td colspan="3"><input name="keyword" id="keyword" class="mini-textbox" style="width:433px;" value="${product.keyword}" /></td>
			</tr>
			<tr>
			  <td>页面关键词：</td>
			  <td colspan="3"><input name="metaKeyword" id="metaKeyword" class="mini-textbox" style="width:433px;" value="${product.metaKeyword}" /></td>
			</tr>
			<tr>
			  <td>页面描述：</td>
			  <td colspan="3"><input name="metaDescription" id="metaDescription" class="mini-textbox" style="width:433px;" value="${product.metaDescription}" /></td>
			</tr>
		  </table>
		  <c:set var="hasSku" value="${fn:length(product.tbShopSkus)>0}" />
		<fieldset style="border:solid 1px #aaa;padding:0 8px 5px;">
			<legend>规格选项 <c:if test="${hasSku}"><span style="color:red;">(不可编辑：当前存在SKU)</span></c:if></legend>
			<ul class="specifications"><c:forEach items="${specifications}" var="item">
			  <li title="${item.name} ${item.remark}"><label><input type="checkbox" name="specifications" value="${item.id}">${item.name}<c:if test="${not empty item.remark}"><span class="remark">[${item.remark}]</span></c:if></label></li></c:forEach>
			</ul>
		</fieldset>
		</div>
		<div title="商品描述">
		  <textarea name="introduction" style="width:99%;height:470px;">${product.introduction}</textarea>
		</div>
		<div name="images" title="商品图片">
			<div class="mini-toolbar" style="border:0;background:none;padding:3px 10px;">
				第一张为缩略图，明细图片建议最小规格：360*360px
			</div>
			<div class="mini-toolbar" style="border-bottom:0;">
				<a class="mini-button" plain="true" iconCls="icon-add" id="btnUpload">上传图片</a>
				<a class="mini-button" plain="true" iconCls="icon-add" id="btnUploadBatch">批量上传</a>
				<span class="separator"></span>
				<a class="mini-button" plain="true" iconCls="icon-upload" onclick="upItem()">上移</a>
				<a class="mini-button" plain="true" iconCls="icon-download" onclick="downItem()">下移</a>
				<a class="mini-button" plain="true" iconCls="icon-remove" onclick="removeItem()">删除</a>
				<!-- <a class="mini-button" plain="true" iconCls="icon-save" onclick="saveImages()">保存修改</a> -->
			</div>
			<div class="mini-fit">
			<div class="mini-splitter" style="width:100%;height:100%;">
				<div size="320">
					<div id="grid2" class="mini-datagrid" style="width:100%;height:100%;" fitColumns="true" borderStyle="border:0"
					    showPager="false" onrowclick="onRowClick">
					    <div property="columns">
					        <div type="indexcolumn" width="30"></div>
					        <div field="url" width="100%">URL</div>
					    </div>
					</div>
				</div>
				<div id="viewPanel" style="padding:10px;overflow:auto;">
				</div>
			</div>
			</div>
		</div>
	</div>
</div>
</form>
</div>
<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
	<a class="mini-button" onclick="saveData()" style="width:60px;margin-right:20px;">保存</a>
	<a class="mini-button" onclick="closeWindow('cancel')" style="width:60px;">取消</a>
</div>

<script type="text/javascript">
	mini.parse();
    var grid2 = mini.get("grid2");

	var category = mini.get("category");
	category.on('closeclick',function(e) {
		var obj = e.sender;
		obj.setText("");
		obj.setValue("");
	});
	miniExt.tree.setExpandOnLoad(category.tree);

	var brand = mini.get("brand");
	brand.on('closeclick',function(e) {
		var obj = e.sender;
		obj.setText("");
		obj.setValue("");
	});

	/*
	var freightTemplate = mini.get("freightTemplate");
	freightTemplate.on('closeclick',function(e) {
		var obj = e.sender;
		obj.setText("");
		obj.setValue("");
	});
	*/

	var productBaseinfo = mini.get("productBaseinfo");
	productBaseinfo.on('closeclick',function(e) {
		var obj = e.sender;
		obj.setText("");
		obj.setValue("");
	});

	// var tabs = mini.get("tabs1");
	// tabs.on('beforeactivechanged',function(e) {
	// 	var obj = e.sender;
	// 	console.log(1,e);
	//
	// 	if(e.name=="images"){加载图片}
	//
	// });
	// tabs.on('activechanged',function(e) {
	// 	var obj = e.sender;
	// 	console.log(2,e);
	// });



	var form = new mini.Form("#form1");

	function saveData() {
		form.validate();
		if (form.isValid() == false) {
			var tabs = mini.get("tabs1");
			//console.log(tabs);
			tabs.activeTab(tabs.getTab(0));
			return;
		}

		//editor.sync();
		// 保存调整后的图片
		saveImageStore();
		saveSampleImage();
		var data = form.getData(true,false);
		data["introduction"] = editor.html();

		data['specifications'] = [];
		$('input[name=specifications]:checked').each(function(i,item){
			data['specifications'].push(item.value);
		});

		if (data['specifications'].length==0 && !confirm("当前商品确定不使用规格选项吗？")){
			return;
		}

		$.ajax({
			url: "update.do",
			type: "POST",
			dataType: "json",
			data: data,
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



	//商品图片
	function loadImageStoreToGrid(){
		var imageStore = mini.get("imageStore");
		var values = imageStore.getValue().split('|');
		var rows = [];
		for (var i=0; i<values.length; i++) {
			if(values[i]==""){continue;}
			var row = {};
			row['url'] = values[i];
			rows.push(row);
		};
		grid2.addRows(rows);
	}
	function onRowClick(e) {
        $('#viewPanel').html('<img src="'+e.record.url+'" alt="" />');
	}
    function upItem() {
        var items = grid2.getSelecteds();
        for (var i = 0, l = items.length; i < l; i++) {
            var item = items[i];
            var index = grid2.indexOf(item);
            grid2.moveRow(item, index-1);
        }
    }
    function downItem() {
        var items = grid2.getSelecteds();
        for (var i = 0, l = items.length; i < l; i++) {
            var item = items[i];
            var index = grid2.indexOf(item);
            grid2.moveRow(item, index + 2);
        }
    }
    function removeItem() {
        var items = grid2.getSelecteds();
        grid2.removeRows(items);
    }

    loadImageStoreToGrid();

	$(function($){
		var $specifications = $('.specifications');
		var specifications = [];<c:forEach items="${product.tbShopSpecifications}" var="item">
		specifications[specifications.length] = "${item.id}";</c:forEach>

		$('input',$specifications).each(function(){
			if ($.inArray($(this).val(),specifications)>-1){
				$(this).attr("checked",true);
				//if hassku 可增不可减
				//$(this).bind('click',function(i,item){
					//return false;
				//});
			}
		});
		<c:if test="${hasSku}">
		$('input[name=specifications]').bind('click',function(i,item){
			return false;
		});
		</c:if>
	});
</script>
</body>
</html>
