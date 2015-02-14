<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../../inc.header.jsp" />
</head>
<body>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="add()">增加</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">修改</a>
	<%-- <a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a> --%>
	<a class="mini-button" plain="true" iconCls="icon-reload" onclick="grid.reload()">刷新</a>
	<span class="separator"></span>
	<a class="mini-button" plain="true" iconCls="icon-reload" onclick="loadGroups()">下载服务器数据</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" ondrawcell="onDrawCell" multiSelect="true" showPager="false"
		url="datalist.do"
	>
		<div property="columns">
			<div type="indexcolumn" width="30"></div>
			<div type="checkcolumn"></div>
			<div field="id" width="100" headerAlign="center">ID</div>
			<div field="name" width="100" headerAlign="center">名称</div>
			<div field="count" width="100" headerAlign="center">人数</div>
			<div width="100%"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function onDrawCell(e){
	};

	function add() {
		var name = prompt("分组名称：");
		if (null==name){return;}
		name=name.trim();
		if (name!=""){
			$.ajax({
				type: "POST",
				url: "save.do",
				data:{name:name},
				dataType: "json",
				success: function (json) {
					if (json.status=="success"){
						top.miniExt.showMsg("保存成功");
						grid.reload();
					}else{
						alert(json.message);
					}
				}
			});
		}
	}
	function edit() {
		var row = grid.getSelected();
		if (row) {
			var name = prompt("分组名称：",row.name);
			if (null==name){return;}
			name=name.trim();
			if (name!="" && name!=row.name){
				$.ajax({
					type: "POST",
					url: "update.do",
					data:{id:row.id,name:name},
					dataType: "json",
					success: function (json) {
						if (json.status=="success"){
							top.miniExt.showMsg("保存成功");
							row.name=name;
							grid.updateRow(row);
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
	/*
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
	*/
	function loadGroups(){
		$.ajax({
			type: "POST",
			url: "loadGroups.do",
			dataType: "json",
			success: function (json) {
				if (json.status=="success"){
					top.miniExt.showMsg("更新已提交到服务器...");
					grid.reload();
				}else{
					alert(json.message);
				}
			}
		});
	}
</script>
</body>
</html>
