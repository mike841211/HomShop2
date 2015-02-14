<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<style type="text/css">
	a {text-decoration:none;}
</style>
<style type="text/css">
	.cell-img{position: relative;overflow: visible;padding: 0 0 24px;*+z-index: 9;}
	.cell-img div {position: absolute;overflow: visible;top:2px;left:0;z-index: 9;}
	.cell-img span {position: absolute;overflow: visible;top:1px;left:40px;padding:3px;background:#fff;border:1px solid #ddd;display:none;/* border-radius:100px; */}
	.cell-img .preview span {display:block;}
	.cell-img .preview span img{width:200px;height:200px;}
</style>
</head>
<body>
<!--menu-->
<ul id="popupMenu" class="mini-menu" style="display:none;">
	<!-- <li onclick="setFreightTemplate()">运费模版</li> -->
	<li>
	    <span iconCls="icon-edit">上架</span>
	    <ul>
		    <li iconCls="icon-ok" onclick="setStatus('isSale','1')">上架</li>
            <li iconCls="icon-remove" onclick="setStatus('isSale','0')">下架</li>
	    </ul>
    </li>
	<li>
	    <span iconCls="icon-edit">免运费</span>
	    <ul>
		    <li iconCls="icon-ok" onclick="setStatus('isFreeShipping','1')">免运费</li>
            <li iconCls="icon-remove" onclick="setStatus('isFreeShipping','0')">取消</li>
	    </ul>
    </li>
	<li>
	    <span iconCls="icon-edit">新品</span>
	    <ul>
		    <li iconCls="icon-ok" onclick="setStatus('isNew','1')">新品</li>
            <li iconCls="icon-remove" onclick="setStatus('isNew','0')">取消</li>
	    </ul>
    </li>
	<li>
	    <span iconCls="icon-edit">热销</span>
	    <ul>
		    <li iconCls="icon-ok" onclick="setStatus('isHot','1')">热销</li>
            <li iconCls="icon-remove" onclick="setStatus('isHot','0')">取消</li>
	    </ul>
    </li>
	<li>
	    <span iconCls="icon-edit">促销</span>
	    <ul>
		    <li iconCls="icon-ok" onclick="setStatus('isPromotion','1')">促销</li>
            <li iconCls="icon-remove" onclick="setStatus('isPromotion','0')">取消</li>
	    </ul>
    </li>
	<li>
	    <span iconCls="icon-edit">推荐</span>
	    <ul>
		    <li iconCls="icon-ok" onclick="setStatus('isRecomend','1')">推荐</li>
            <li iconCls="icon-remove" onclick="setStatus('isRecomend','0')">取消</li>
	    </ul>
    </li>
</ul>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="add()">增加</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">修改</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<a class="mini-menubutton" plain="true" iconCls="icon-control" menu="#popupMenu">设置</a>
	<span class="separator"></span>
	<input id="categoryId" class="mini-treeselect" emptyText="类别"
	  valueField="id" textField="name" popupWidth="200" showClose="true"
	  url="../product_category/getProductCategoryTreeData.do"
	 />
	<input class="mini-combobox" id="searchby" data="[{id:'name',text:'品名'},{id:'sn',text:'编码'}]" value="name" style="width:60px;" />
	<input class="mini-textbox" id="searchkey" onenter="search()"/>
	<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" multiSelect="true" fitColumns="true"
		url="datalist.do"
	 >
		<div property="columns">
			<div type="indexcolumn"></div>
			<div type="checkcolumn"></div>
			<div width="60" headerAlign="center" align="center" renderer="onSkuRenderer">管理SKU</div>
			<div field="sampleImage" width="40" headerAlign="center" align="center" >图片</div>
			<div field="sn" width="80" headerAlign="center">商品货号</div>
			<div field="name" width="100%" headerAlign="center">品名</div>
			<div field="categoryName" width="100" headerAlign="center">类别</div>
			<%-- <div field="brandName" width="80" headerAlign="center">品牌</div> --%>
			<div field="isSale" width="40" headerAlign="center" align="center">上架</div>
			<div field="isFreeShipping" width="50" headerAlign="center" align="center">免运费</div>
			<div field="isNew" width="40" headerAlign="center" align="center">新品</div>
			<div field="isHot" width="40" headerAlign="center" align="center">热销</div>
			<div field="isPromotion" width="40" headerAlign="center" align="center">促销</div>
			<div field="isRecomend" width="40" headerAlign="center" align="center">推荐</div>
			<div field="marketPrice" width="60" headerAlign="center" align="right" dataType="currency">市场价</div>
			<div field="price" width="60" headerAlign="center" align="right" dataType="currency">售价</div>
			<div field="stock" width="60" headerAlign="center" align="right" dataType="int">库存</div>
			<div field="blockedStock" width="60" headerAlign="center" align="right" dataType="int">冻结库存</div>
			<div field="sales" width="60" headerAlign="center" align="right" dataType="int">销量</div>
		</div>
	</div>
</div>
<%--
<div id="editWindow" class="mini-window" title="设置运费模板" style="width:300px;"
    showModal="true" allowResize="true" allowDrag="true"
    >
	<div class="mini-fit" style="padding:10px;">
		<div id="editform" class="form" >
		  <table style="table-layout:fixed;">
			<tr>
			  <td style="width:80px;">运费模版：</td>
			  <td><input name="tbShopFreightTemplate.id" id="freightTemplate" class="mini-combobox"
					showClose="true" valueFromSelect="true"
					valueField="value" textField="text" value="" required="true"
				  />
			  </td>
			</tr>
		  </table>
		</div>
	</div>
	<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
		<a class="mini-button" onclick="updateFreightTemplate()" style="width:60px;margin-right:20px;">保存</a>
		<a class="mini-button" onclick="editWindow.hide()" style="width:60px;">取消</a>
	</div>
</div>
 --%>
<script type="text/javascript">
	mini.parse();

	var categoryId = mini.get("categoryId");
	categoryId.on('closeclick',function(e) {
		var obj = e.sender;
		obj.setText("");
		obj.setValue("");
	});
	miniExt.tree.setExpandOnLoad(categoryId.tree);

	function onSkuRenderer(e) {
		// var grid = e.sender;
		var record = e.record;
		var uid = record._uid;
		var id = record.id;
		return '<a href="javascript:skuList(\'' + id + '\')" >管理SKU</a>';
	}

	var grid = mini.get("datagrid1");
	grid.on("drawcell",function(e){
		var icon_yes = '<img src="${base}/resources/admin/images/icon/yes.png" />';
		var icon_no  = '<img src="${base}/resources/admin/images/icon/no.gif" />';
		if (e.field == "sampleImage") {
			e.cellCls = "cell-img";
			//e.cellHtml = e.value==''||e.value==null ? '' : '<img src="${base}/resources/admin/images/icon/images.png" onclick="window.open(\''+e.value+'\')"/>';
			e.cellHtml = e.value==''||e.value==null ? '' : '<a href="###" onclick="showImg(\''+e.value+'\')" onmouseenter="showPreviewImg(this,\''+e.value+'\');" onmouseleave="hidePreviewImg(this);"><img src="${base}/resources/admin/images/icon/images.png" /></a><span></span>';
		}
		else if (e.field == "isSale") {
			e.cellHtml = e.value=='1' ? icon_yes : icon_no;
		}
		else if (e.field == "isFreeShipping") {
			e.cellHtml = e.value=='1' ? icon_yes : icon_no;
		}
		else if (e.field == "isNew") {
			e.cellHtml = e.value=='1' ? icon_yes : icon_no;
		}
		else if (e.field == "isHot") {
			e.cellHtml = e.value=='1' ? icon_yes : icon_no;
		}
		else if (e.field == "isPromotion") {
			e.cellHtml = e.value=='1' ? icon_yes : icon_no;
		}
		else if (e.field == "isRecomend") {
			e.cellHtml = e.value=='1' ? icon_yes : icon_no;
		}
	});

	grid.load();

	function showImg(src) {
		miniExt.win.open({
			url: src,
			title: "图片",
			bodyStyle:"padding:10;px",
			showMaxButton: true,
			width: 500,
			height: 500
		});
	}
	function add() {
		miniExt.win.open({
			url: "product/add.htm",
			title: "新建商品",
			width: 800,
			height: 600
		}).callback(function(){
			grid.reload();
		});
	}
	function edit() {
		var row = grid.getSelected();
		if (row) {
			miniExt.win.open({
				url: "product/edit.htm?id="+row.id,
				title: "编辑商品",
				width: 800,
				height: 600
			}).callback(function(){
				grid.reload();
			});
		} else {
			alert("请选中一条记录");
		}
	}
	function dele() {
		var rows = grid.getSelecteds();
		if (rows.length > 0) {
			if (confirm("确定删除选中记录？")) {
				var ids = [];
				for (var i = 0, l = rows.length; i < l; i++) {
					var r = rows[i];
					ids.push(r.id);
				}
				//var id = ids.join(',');
				//grid.loading("操作中，请稍后......");
				$.ajax({
					type: "POST",
					url: "delete.do",
					dataType: "json",
					data: {ids:ids.join(',')},
					success: function (json) {
						if (json.status=="success"){
							grid.reload();
						}else{
							alert(json.message);
						}
					}
				});
			}
		} else {
			alert("请选中一条记录");
		}
	}
	function search() {
		var params={};
		var searchby = mini.get("searchby").getFormValue();
		var searchkey = mini.get("searchkey").getFormValue();
		var category_id = mini.get("categoryId").getFormValue();
		params.searchby = searchby;
		params.searchkey= searchkey;
		params.cid= category_id;
		grid.load(params);
	}

	//private
	function getGridIds(){
		var ids = [];
		var rows = grid.getSelecteds();
		for (var i = 0, l = rows.length; i < l; i++) {
			ids.push(rows[i].id);
		}
		return ids;
	}

	function setStatus(field,status){
		//var ids = getGridIds();
		var ids = miniExt.grid.getValues(grid);
		if (ids.length > 0) {
			$.ajax({
				type: "POST",
				url: "setStatus.do",
				dataType: "json",
				data: {ids:ids,field:field,status:status},
				success: function (json) {
					if (json.status=="success"){
						miniExt.showMsg("保存成功");
						grid.reload();
					}else{
						alert(json.message);
					}
				}
			});
		} else {
			alert("请选中一条记录");
		}
	}

	<%--
	/*
	var editWindow = mini.get("editWindow");
	var editform = new mini.Form("#editform");
	var freightTemplate = mini.get("freightTemplate");
	function setFreightTemplate(){
		var ids = miniExt.grid.getValues(grid);
		if (ids.length > 0) {
			editWindow.show();
			if (!freightTemplate.getUrl()){
				freightTemplate.setUrl("../freight_template/getAllForSelect.do");
			}
		} else {
			alert("请选中一条记录");
		}
	}
	function updateFreightTemplate(){
		//var ids = getGridIds();
		var ids = miniExt.grid.getValues(grid);
		if (ids.length > 0) {
			var templateId = freightTemplate.getValue();
			if (!templateId){
				return;
			}
			$.ajax({
				type: "POST",
				url: "updateFreightTemplate.do",
				dataType: "json",
				data: {ids:ids,templateId:templateId},
				success: function (json) {
					if (json.status=="success"){
						miniExt.showMsg("保存成功");
						grid.reload();
						editWindow.hide();
					}else{
						alert(json.message);
					}
				}
			});
		} else {
			alert("请选中一条记录");
		}
	}
	*/
	 --%>

	function skuList(id) {
		miniExt.win.open({
			//state: "max", // 默认最大化
			showMaxButton:true,
			url: "sku/list.htm?id="+id,
			title: "商品SKU",
			width: '90%',
			height: '90%'
		}).callback(function(){
			grid.reload();
		});
	}

	function showPreviewImg(a,src) {
		$(a).parent().addClass('preview');
		$(a).next('span').html('<img src="'+src+'" />');
		grid.doLayout();
	}
	function hidePreviewImg(a) {
		$(a).parent().removeClass('preview');
	}
</script>
</body>
</html>
