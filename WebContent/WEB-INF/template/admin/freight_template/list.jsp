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
	<input class="mini-combobox" id="searchby" data="[{id:'name',text:'标题'}]" value="name" style="width:60px;" />
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
			<div field="name" width="400" headerAlign="center">标题</div>
			<div header="快递" headerAlign="center">
				<div property="columns">
					<div headerAlign="center" field="expFee" width="60" align="center">首重</div>
					<div headerAlign="center" field="expPlusfee" width="60" align="center">续重</div>
				</div>
			</div>
			<div header="EMS" headerAlign="center">
				<div property="columns">
					<div headerAlign="center" field="emsFee" width="60" align="center">首重</div>
					<div headerAlign="center" field="emsPlusfee" width="60" align="center">续重</div>
				</div>
			</div>
			<div header="平邮" headerAlign="center">
				<div property="columns">
					<div headerAlign="center" field="pyFee" width="60" align="center">首重</div>
					<div headerAlign="center" field="pyPlusfee" width="60" align="center">续重</div>
				</div>
			</div>
			<div field="modifyDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd" allowSort="true">编辑日期</div>
			<div field="createDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd" allowSort="true">创建日期</div>
			<div width="100%"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function add() {
		miniExt.win.open({
			url: "freight_template/add.htm",
			title: "新建运费模板",
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
				url: "freight_template/edit.htm?id="+row.id,
				title: "编辑运费模板",
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
		params.searchby = searchby;
		params.searchkey= searchkey;
		grid.load(params);
	}
</script>
</body>
</html>