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
	<input class="mini-combobox" id="searchby" data="[{id:'username',text:'用户名'},{id:'name',text:'姓名'}]" value="username" style="width:60px;" />
	<input class="mini-textbox" id="searchkey" onenter="search()"/>
	<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" ondrawcell="onDrawCell" multiSelect="true" fitColumns="false"
		url="datalist.do"
	>
		<div property="columns">
			<div type="indexcolumn"></div>
			<div type="checkcolumn"></div>
			<div field="lock" width="40" headerAlign="center" align="center" allowSort="true">状态</div>
			<div field="username" width="100" headerAlign="center" allowSort="true">用户名</div>
			<div field="email" width="200" headerAlign="center">Email</div>
			<div field="name" width="100" headerAlign="center" allowSort="true">姓名</div>
			<div field="department" width="100" headerAlign="center" allowSort="true">部门</div>
			<div field="loginDate" width="140" headerAlign="center" dateFormat="yyyy-MM-dd HH:mm:ss" allowSort="true">最后登录时间</div>
			<div field="loginIp" width="110" headerAlign="center" allowSort="true">最后登陆IP</div>
			<div field="modifyDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd" allowSort="true">编辑日期</div>
			<div field="createDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd" allowSort="true">创建日期</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function onDrawCell(e){
		if (e.field == "lock") {
			e.cellHtml = e.value=='1'?'<span style="color:red;">禁用</span>':'<span style="color:green;">正常</span>';
		}
	};

	function add() {
		miniExt.win.open({
			url: "admin/add.htm",
			title: "新建管理员",
			//bodyStyle:"padding:0;overflow:hidden;",//IE9
			width: 500,
			height: 400
		}).callback(function(){
			grid.reload();
		});
	}
	function edit() {
		var row = grid.getSelected();
		if (row) {
			miniExt.win.open({
				url: "admin/edit.htm?id="+row.id,
				title: "编辑管理员",
				//bodyStyle:"padding:0;overflow:hidden;",//IE9
				width: 500,
				height: 400
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
</script>
</body>
</html>
