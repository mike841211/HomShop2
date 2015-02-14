<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../../inc.header.jsp" />
<style type="text/css">
	.cell-img{position: relative;overflow: visible;padding: 0 0 24px;*+z-index: 9;}
	.cell-img div {position: absolute;overflow: visible;top:0;left:-2px;z-index: 9;}
	.cell-img span {position: absolute;overflow: visible;top:1px;left:40px;padding:3px;background:#fff;border:1px solid #ddd;display:none;/* border-radius:100px; */}
	.cell-img img{width:20px;height:20px;/* border-radius:100px; */}
	.cell-img:hover span {display:block;}
	.cell-img:hover span img{width:200px;height:200px;}
</style>
</head>
<body>
<div class="mini-splitter" style="width:100%;height:100%;">
	<div size="180" showCollapseButton="true">
		<div class="mini-toolbar" style="padding:2px;border-top:0;border-left:0;border-right:0;">
			<span style="padding-left:5px;">分组</span>
			<a class="mini-button" plain="true" iconCls="icon-reload" onclick="tree.reload()">刷新</a>
		</div>
		<div class="mini-fit">
			<ul id="tree1" class="mini-tree" url="../group/datalist.do" style="width:100%;"
				showTreeIcon="true" textField="name" idField="id" parentField="pid" resultAsTree="false" ondrawnode="onDrawNode"
			>
			</ul>
		</div>
	</div>
	<div showCollapseButton="true">
		<div class="mini-toolbar" style="border-top:0;border-right:0;border-left:0;">
			<a class="mini-button" plain="true" iconCls="icon-add" onclick="setMessage()">发送消息</a>
			<a class="mini-button" plain="true" iconCls="icon-reload" onclick="updateInfo()">更新信息</a>
			<a class="mini-button" plain="true" iconCls="icon-edit" onclick="selectGroup()">移动分组</a>
			<a class="mini-button" plain="true" iconCls="icon-add" onclick="loadUsers()">下载用户</a>
			<span class="separator"></span>
			<input class="mini-combobox" id="searchby" data="[{id:'nickname',text:'昵称'},{id:'sex',text:'性别'},{id:'country',text:'国家'},{id:'province',text:'省份'},{id:'city',text:'城市'}]" value="nickname" style="width:60px;" />
			<input class="mini-textbox" id="searchkey" onenter="search()"/>
			<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
		</div>
		<div class="mini-fit">
			<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;" borderStyle="border:0;"
				idField="id" pageSize="20" multiSelect="true" fitColumns="true"
				url="datalist.do"
			 >
				<div property="columns">
					<div type="indexcolumn"></div>
					<div type="checkcolumn"></div>
					<div field="subscribe" width="40" headerAlign="center" align="center">关注</div>
					<div field="groupId" width="80" headerAlign="center" align="center">分组</div>
					<div field="nickname" width="200" headerAlign="center">昵称</div>
					<div field="headimgurl" width="50" headerAlign="center" align="center">头像</div>
					<div field="sex" width="40" headerAlign="center" align="center">性别</div>
					<div field="country" width="100" headerAlign="center" align="center">国家</div>
					<div field="province" width="100" headerAlign="center" align="center">省份</div>
					<div field="city" width="100" headerAlign="center" align="center">城市</div>
					<div field="subscribeTime" width="90" headerAlign="center" align="center" >关注日期</div>
					<div width="100%"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="editWindow" class="mini-window" title="移动分组" style="width:300px;"
    showModal="true" allowResize="true" allowDrag="true"
    >
	<div class="mini-fit" style="padding:10px;">
		<div id="editform" class="form" >
		  <table style="table-layout:fixed;">
			<tr>
			  <td style="width:80px;">分组：</td>
			  <td><input name="group" id="group" class="mini-combobox"
					showClose="true" valueFromSelect="true"
					valueField="id" textField="name" value="" required="true"
				  />
			  </td>
			</tr>
		  </table>
		</div>
	</div>
	<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
		<a class="mini-button" onclick="updateGroup()" style="width:60px;margin-right:20px;">保存</a>
		<a class="mini-button" onclick="editWindow.hide()" style="width:60px;">取消</a>
	</div>
</div>

<div id="msgWindow" class="mini-window" title="发送消息" style="width:500px;"
    showModal="true" allowResize="true" allowDrag="true"
    >
	<div class="mini-fit" style="padding:10px;">
		<div id="editform" class="form" >
		  <table style="table-layout:fixed;">
			<tr>
			  <td style="width:80px;">消息：</td>
			  <td><input id="message" class="mini-textarea" value="" required="true" width="400" height="200" /></td>
			</tr>
		  </table>
		</div>
	</div>
	<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
		<a class="mini-button" onclick="sendMessage()" style="width:60px;margin-right:20px;">保存</a>
		<a class="mini-button" onclick="msgWindow.hide()" style="width:60px;">取消</a>
	</div>
</div>

<script type="text/javascript">
	mini.parse();

	var tree = mini.get("tree1");
	tree.on("nodeselect", function (e) {
		if (e.isLeaf) {
			grid.load({ group_id: e.node.id });
		}
	});

	var grid = mini.get("datagrid1");

	grid.on("drawcell",function(e){
		if (e.field == "subscribe") {
			e.cellHtml = e.value=='1' ? '<img src="${base}/resources/admin/images/icon/yes.png" />' : '<img src="${base}/resources/admin/images/icon/no.gif" />';
		}
		else if (e.field == "groupId") {
			var data = tree.getData();
			for (var i in data){
				if (data[i].id==e.value){
					e.cellHtml = data[i].name;
					return;
				}
			}
			//e.cellHtml = "";
		}
		else if (e.field == "sex") {
			if (e.value==1){
				e.cellHtml = '男';
			}else if (e.value==2){
				e.cellHtml = '女';
			}else{
				e.cellHtml = '';
			}
		}
		else if (e.field == "headimgurl") {
			e.cellCls = "cell-img";
			var src = e.value ? e.value : '${base}/resources/common/images/avatar.jpg';
			e.cellHtml = '<img src="'+ src +'" /><span><img src="'+ src +'" /></span>';
		}
		else if (e.field == "subscribeTime") {
			var date = new Date(e.value*1000);
			e.cellHtml = mini.formatDate(date,"yyyy-MM-dd");
		}
	});

	grid.load();

	function search() {
		var params={};
		var searchby = mini.get("searchby").getFormValue();
		var searchkey = mini.get("searchkey").getFormValue();
		params.searchby = searchby;
		params.searchkey= searchkey;
		grid.load(params);
	}

	function onDrawNode(e) {
		var tree = e.sender;
		var node = e.node;

		var isLeaf = tree.isLeaf(node);

		//所有子节点加上超链接
		if (isLeaf == true) {
			var html = node.name;
			html += '('+node.count+')';
			e.nodeHtml = html;
		}
	}


	var editWindow = mini.get("editWindow");
	//var editform = new mini.Form("#editform");
	var group = mini.get("group");
	function selectGroup(){
		var ids = miniExt.grid.getValues(grid,"openid");
		if (ids.length > 0) {
			editWindow.show();
			if (group.getData().length==0){
				group.setData(tree.getData());
			}
		} else {
			alert("请选中一条记录");
		}
	}
	function updateGroup(){
		//var ids = getGridIds();
		var ids = miniExt.grid.getValues(grid,"openid");
		if (ids.length > 0) {
			var groupId = group.getValue();
			if (!groupId){
				return;
			}
			$.ajax({
				type: "POST",
				url: "updateGroup.do",
				dataType: "json",
				data: {ids:ids,groupId:groupId},
				success: function (json) {
					if (json.status=="success"){
						miniExt.showMsg("保存成功");
						grid.reload();
						editWindow.hide();
					}else{
						alert(json.message);
					}
				}
			});
		} else {
			alert("请选中一条记录");
		}
	}
	function updateInfo(){
		//var ids = getGridIds();
		var ids = miniExt.grid.getValues(grid,"openid");
		if (ids.length > 0) {
			$.ajax({
				type: "POST",
				url: "updateInfo.do",
				dataType: "json",
				data: {ids:ids},
				success: function (json) {
					if (json.status=="success"){
						miniExt.showMsg("更新成功");
						grid.reload();
						editWindow.hide();
					}else{
						alert(json.message);
					}
				}
			});
		} else {
			alert("请选中一条记录");
		}
	}

	var msgWindow = mini.get("msgWindow");
	var message = mini.get("message");
	function setMessage(){
		var ids = miniExt.grid.getValues(grid,"openid");
		if (ids.length > 0) {
			msgWindow.show();
		} else {
			alert("请选择接收人");
		}
	}
	function sendMessage(){
		//var ids = getGridIds();
		var ids = miniExt.grid.getValues(grid,"openid");
		if (ids.length > 0) {
			var content = message.getValue();
			if (!content){
				return;
			}
			$.ajax({
				type: "POST",
				url: "sendCustomMessage.do",
				dataType: "json",
				data: {ids:ids,content:content},
				success: function (json) {
					if (json.status=="success"){
						miniExt.showMsg("消息已提交");
						msgWindow.hide();
					}else{
						alert(json.message);
					}
				}
			});
		} else {
			alert("请选中一条记录");
		}
	}
	function loadUsers(){
		$.ajax({
			type: "POST",
			url: "loadUsers.do",
			dataType: "json",
			success: function (json) {
				if (json.status=="success"){
					top.miniExt.showMsg("任务已提交到服务器，请稍后刷新...");
				}else{
					alert(json.message);
				}
			}
		});
	}

</script>
</body>
</html>
