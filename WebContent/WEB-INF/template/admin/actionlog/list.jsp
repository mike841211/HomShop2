<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
</head>
<body>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<span class="separator"></span>
	<input class="mini-combobox" id="searchby" data="[{id:'action',text:'操作名称'},{id:'username',text:'操作人'}]" value="username" style="width:80px;" />
	<input class="mini-textbox" id="searchkey" onenter="search()"/>
	日期：
	<input id="createDateBegin" class="mini-datepicker" style="width:160px;" format="yyyy-MM-dd HH:mm:ss" timeFormat="HH:mm:ss"
        showTime="true" showOkButton="true" showClearButton="false"/>
	-
	<input id="createDateEnd" class="mini-datepicker" style="width:160px;" format="yyyy-MM-dd HH:mm:ss" timeFormat="HH:mm:ss"
        showTime="true" showOkButton="true" showClearButton="false"/>
	<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" multiSelect="true"
		url="datalist.do"
	>
		<div property="columns">
			<div type="indexcolumn"></div>
			<div type="checkcolumn"></div>
			<div field="username" width="100" headerAlign="center">操作人</div>
			<div field="action" width="120" headerAlign="center">操作名称</div>
			<div field="detail" width="100%" headerAlign="center">日志信息</div>
			<div field="ip" width="110" headerAlign="center">IP</div>
			<div field="createDate" dateFormat="yyyy-MM-dd HH:mm:ss" width="140" align="center" headerAlign="center">记录时间</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

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

		var createDateBegin = mini.get("createDateBegin").getFormValue();
		var createDateEnd = mini.get("createDateEnd").getFormValue();
		params.createDateBegin= createDateBegin;
		params.createDateEnd= createDateEnd;

		grid.load(params);
	}
</script>
</body>
</html>
