<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
</head>
<body>
<!--menu-->
<ul id="popupMenu" class="mini-menu" style="display:none;">
	<li iconCls="icon-ok" onclick="setIsShow('1')">发布</li>
	<li iconCls="icon-remove" onclick="setIsShow('0')">隐藏</li>
</ul>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="add()">增加</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">修改</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<a class="mini-menubutton" plain="true" iconCls="icon-control" menu="#popupMenu">设置</a>
	<span class="separator"></span>
	<input id="article_category" class="mini-treeselect" emptyText="类别"
	  valueField="id" textField="name" showClose="true"
	  url="../article_category/getArticleCatagoryTreeData.do"
	 />
	<input class="mini-combobox" id="searchby" data="[{id:'title',text:'标题'}]" value="title" style="width:60px;" />
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
			<div field="isShow" width="40" headerAlign="center" align="center">发布</div>
			<!-- <div field="topIndex" width="40" headerAlign="center" align="center">置顶</div> -->
			<div field="category_name" width="100" headerAlign="center" align="center">分类</div>
			<div field="title" width="100%" headerAlign="center">标题</div>
			<div field="id" width="250">ID</div>
			<div field="modifyDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd" allowSort="true">编辑日期</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var article_category = mini.get("article_category");
	article_category.on('closeclick',function(e) {
		var obj = e.sender;
		obj.setText("");
		obj.setValue("");
	});

	var grid = mini.get("datagrid1");

	grid.on("drawcell",function(e){
		if (e.field == "isShow") {
			e.cellHtml = e.value=='1' ? '<img src="${base}/resources/admin/images/icon/yes.png" />' : '<img src="${base}/resources/admin/images/icon/no.gif" />';
		}
		else if (e.field == "title") {
			e.cellHtml = '<a href="'+ location.origin +'${base}/article/view/'+ e.record.id +'.htm" target="_blank">'+ e.value +'</a>';
		}
	});

	grid.load();

	function showImg(src) {
		miniExt.win.open({
			url: src,
			title: "图片",
			width: 500,
			height: 500
		});
	}
	function add() {
		miniExt.win.open({
			url: "article/add.htm",
			title: "新建文章",
			width: 900,
			height: 650
		}).callback(function(){
			grid.reload();
		});
	}
	function edit() {
		var row = grid.getSelected();
		if (row) {
			miniExt.win.open({
				url: "article/edit.htm?id="+row.id,
				title: "编辑文章",
				width: 900,
				height: 650
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
		var article_category = mini.get("article_category").getFormValue();
		params.searchby = searchby;
		params.searchkey= searchkey;
		params.categoryId= article_category;
		grid.load(params);
	}

	function setIsShow(status){
		var ids = miniExt.grid.getValues(grid);
		if (ids.length > 0) {
			$.ajax({
				type: "POST",
				url: "setIsShow.do",
				dataType: "json",
				data: {ids:ids.join(','),status:status},
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
</script>
</body>
</html>
