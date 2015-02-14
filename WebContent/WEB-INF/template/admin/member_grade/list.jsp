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
	<a class="mini-button" plain="true" iconCls="mini-pager-reload" onclick="grid.reload()">刷新</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" multiSelect="true" showPager="false"
		url="datalist.do"
	>
		<div property="columns">
			<div type="indexcolumn" width="30"></div>
			<div type="checkcolumn"></div>
			<div field="lever" width="80" headerAlign="center" align="center">级别</div>
			<div field="name" width="100">名称</div>
			<div field="score" width="100" headerAlign="center" align="right">消费积分</div>
			<div field="discount" width="80" headerAlign="center" align="right" dataType="currency">优惠比例</div>
			<div field="isSpecial" width="80" headerAlign="center" align="center" renderer="onSpecialRenderer">特殊级别</div>
			<div field="modifyDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd">编辑日期</div>
			<div field="createDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd">创建日期</div>
			<div field="remark" width="100%">备注</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function onSpecialRenderer(e) {
		return e.value=='1' ? '<img src="${base}/resources/admin/images/icon/yes.png" />' : '<img src="${base}/resources/admin/images/icon/no.gif" />';
	}

	function add() {
		miniExt.win.open({
			url: "member_grade/add.htm",
			title: "新建会员等级",
			width: 400,
			height: 250
		}).callback(function(){
			grid.reload();
		});
	}
	function edit() {
		var row = grid.getSelected();
		if (row) {
			miniExt.win.open({
				url: "member_grade/edit.htm?id="+row.id,
				title: "编辑会员等级",
				width: 400,
				height: 250
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
</script>
</body>
</html>
