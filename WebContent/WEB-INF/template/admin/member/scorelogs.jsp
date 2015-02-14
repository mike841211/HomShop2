<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<style type="text/css">
	.num1{color:#c00;}
	.num2{color:#060;}
	.mini-grid-border{border:0;}
</style>
</head>
<body>
<%-- <div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">编辑</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<span class="separator"></span>
	<input class="mini-combobox" id="searchby" data="[{id:'username',text:'用户名'},{id:'name',text:'姓名'},{id:'mobile',text:'手机'},{id:'email',text:'邮箱'}]" value="username" style="width:60px;" />
	<input class="mini-textbox" id="searchkey" onenter="search()"/>
	<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
</div> --%>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;border:0;"
		idField="id" pageSize="20" ondrawcell="onDrawCell" multiSelect="true" fitColumns="true"
		url="scorelogs.do?id=${param.id}"
	>
		<div property="columns">
			<div type="indexcolumn"></div>
			<div field="createDate" width="140" headerAlign="center" dateFormat="yyyy-MM-dd HH:mm:ss">记录时间</div>
			<div field="val" width="100" headerAlign="center" align="right" dataType="int">积分</div>
			<div field="remark" width="100%" headerAlign="center">说明</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function onDrawCell(e){
		if (e.field == "val") {
			e.cellCls = e.value>0 ? 'num1' : 'num2';
		}
	};

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
