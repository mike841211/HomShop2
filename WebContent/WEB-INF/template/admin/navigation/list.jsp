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
	<a class="mini-button" plain="true" iconCls="icon-ok" onclick="setIsShow('1')">启用</a>
	<a class="mini-button" plain="true" iconCls="icon-lock" onclick="setIsShow('0')">停用</a>
	<a class="mini-button" plain="true" iconCls="mini-pager-reload" onclick="grid.reload()">刷新</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" multiSelect="true" showPager="false" ondrawcell="onDrawCell"
		url="datalist.do"
	>
		<div property="columns">
			<div type="indexcolumn" width="30"></div>
			<div type="checkcolumn"></div>
			<div field="isShow" width="60" headerAlign="center" align="center">启用</div>
			<div field="position" width="60" headerAlign="center" align="center">位置</div>
			<div field="displayorder" width="60" headerAlign="center" align="center">排序</div>
			<div field="title" width="160">名称</div>
			<div field="url" width="100%">链接</div>
			<div field="isBlankTarget" width="60" headerAlign="center" align="center">新窗口</div>
			<div field="modifyDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd">编辑日期</div>
			<div field="createDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd">创建日期</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();


	var POSITION = {"top":"顶部","middle":"中间","bottom":"底部"};
	function onDrawCell(e){
		if (e.field == "position") {
			e.cellHtml = POSITION[e.value];
		}
		else if (e.field == "isBlankTarget" || e.field == "isShow") {
			e.cellHtml = e.value=='1' ? '<img src="${base}/resources/admin/images/icon/yes.png" />' : '<img src="${base}/resources/admin/images/icon/no.gif" />';
		}
		else if (e.field == "url") {
			e.cellHtml = '<a href="'+e.value+'" target="_blank">'+e.value+'</a>';
		}
	};

	function add() {
		miniExt.win.open({
			url: "navigation/add.htm",
			title: "新建导航菜单",
			width: 600,
			height: 300
		}).callback(function(){
			grid.reload();
		});
	}
	function edit() {
		var row = grid.getSelected();
		if (row) {
			miniExt.win.open({
				url: "navigation/edit.htm?id="+row.id,
				title: "编辑导航菜单",
				width: 600,
				height: 300
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
	function setIsShow(isShow) {
		var rows = grid.getSelecteds();
		if (rows.length > 0) {
			var ids = [];
			for (var i = 0, l = rows.length; i < l; i++) {
				var r = rows[i];
				ids.push(r.id);
			}
			//var id = ids.join(',');
			//grid.loading("操作中，请稍后......");
			$.ajax({
				type: "POST",
				url: "setIsShow.do",
				dataType: "json",
				data: {ids:ids,isShow:isShow},
				success: function (json) {
					if (json.status=="success"){
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
