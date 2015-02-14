<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
</head>
<body>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="add()">增加</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">修改</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<span class="separator"></span>
	<input class="mini-combobox" id="searchby" data="[{id:'name',text:'名称'}]" value="name" style="width:60px;" />
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
			<div field="displayorder" width="40" headerAlign="center" align="center" allowSort="true">排序</div>
			<div field="name" width="150" headerAlign="center" allowSort="true">品牌名称</div>
			<div field="logo" width="50" headerAlign="center" renderer="onLogoRenderer" align="center">LOGO</div>
			<div field="url" width="200" renderer="onUrlRenderer">官网</div>
			<div width="100%" headerAlign="center"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function onLogoRenderer(e) {
		return e.value==''||e.value==null ? '' : '<a href="${base}'+e.value+'" target="_blank">查看</a>';
	}

	function onUrlRenderer(e) {
		if (e.value==''||e.value==null){
			return '';
		}
		var url = e.value;
		if(url.toLowerCase().indexOf('http')!=0){
			url = "http://"+url;
		}
		return '<a href="'+url+'" target="_blank">'+e.value+'</a>';
	}

	function add() {
		miniExt.win.open({
			url: "brand/add.htm",
			title: "新建品牌",
			//bodyStyle:"padding:0;overflow:hidden;",//IE9
			width: 800,
			height: 580
		}).callback(function(){
			grid.reload();
		});
	}
	function edit() {
		var row = grid.getSelected();
		if (row) {
			miniExt.win.open({
				url: "brand/edit.htm?id="+row.id,
				title: "编辑品牌",
				//bodyStyle:"padding:0;overflow:hidden;",//IE9
				width: 800,
				height: 580
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
		params.searchby = searchby;
		params.searchkey= searchkey;
		grid.load(params);
	}
	function editInfo(title,id,field,hideEditor) {
		if(hideEditor!=true){hideEditor=false;};
		miniExt.win.open({
			url: "brand_editInfo.htm?id="+id+"&field="+field+"&hideEditor="+hideEditor,
			title: title,
			width: 800,
			height: 500
		});
	}
	function editBrandShopCategory(title,id) {
		miniExt.win.open({
			url: "brandShopCategory_list.htm?brand_id="+id,
			title: title,
			bodyStyle:"margin:-1px;",
			width: 800,
			height: 500
		});
	}
	function editBrandShopIndexModule(title,id) {
		miniExt.win.open({
			url: "brandShopIndexModule_list.htm?brand_id="+id,
			title: title,
			bodyStyle:"margin:-1px;",
			width: 800,
			height: 500
		});
	}
</script>
</body>
</html>
