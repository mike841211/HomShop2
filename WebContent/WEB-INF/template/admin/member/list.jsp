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
	<li iconCls="icon-ok" onclick="setEnabled('1')">启用</li>
	<li iconCls="icon-remove" onclick="setEnabled('0')">禁用</li>
</ul>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" href="${base}/member/register.htm" target="_blank">注册会员</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">编辑</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<a class="mini-menubutton" plain="true" iconCls="icon-control" menu="#popupMenu">设置</a>
	<span class="separator"></span>
	<input class="mini-combobox" id="searchby" data="[{id:'username',text:'用户名'},{id:'name',text:'姓名'},{id:'mobile',text:'手机'},{id:'email',text:'邮箱'}]" value="username" style="width:60px;" />
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
			<div field="enabled" width="40" headerAlign="center" align="center">状态</div>
			<div field="username" width="150" headerAlign="center" allowSort="true">用户名</div>
			<div field="name" width="70" headerAlign="center" allowSort="true">姓名</div>
			<div field="mobile" width="100" headerAlign="center" allowSort="true">手机</div>
			<div field="email" width="150" headerAlign="center">Email</div>
			<div field="gradeName" width="80" headerAlign="center" align="center">会员等级</div>
			<div field="score" width="80" headerAlign="center" align="center">可用积分</div>
			<div field="scoreAddup" width="80" headerAlign="center" align="center">累计积分</div>
			<div field="loginDate" width="140" headerAlign="center" dateFormat="yyyy-MM-dd HH:mm:ss" allowSort="true">最后登录时间</div>
			<div field="registDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd" allowSort="true">注册日期</div>
			<div field="registIparea" width="120" headerAlign="center" align="center">注册IP来源</div>
		</div>
	</div>
</div>
<script type="text/javascript">

	var memberType = {"1":"普通会员","2":"批发会员"};

	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function onDrawCell(e){
		if (e.field == "enabled") {
			e.cellHtml = e.value=='1' ? '<span style="color:green;">正常</span>' : '<span style="color:red;">禁用</span>';
		}
		else if (e.field == "memberType") {
			e.cellHtml = memberType[e.value];
		}
		else if (e.field == "score") {
			e.cellHtml = '<a href="javascript:showScoreLogs(\''+e.record['id']+'\');">'+e.value+'</a>';
		}
	};

	function edit() {
		var row = grid.getSelected();
		if (row) {
			miniExt.win.open({
				url: "member/edit.htm?id="+row.id,
				title: "编辑会员信息",
				width: 600,
				height: 520
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

	function setEnabled(enabled){
		var ids = miniExt.grid.getValues(grid);
		if (ids.length > 0) {
			$.ajax({
				type: "POST",
				url: "setEnabled.do",
				dataType: "json",
				data: {ids:ids,enabled:enabled},
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
	function showScoreLogs(id) {
		miniExt.win.open({
			url: "member/scorelogs.htm?id="+id,
			title: "积分记录",
			width: 800,
			height: 560
		});
	}

</script>
</body>
</html>
