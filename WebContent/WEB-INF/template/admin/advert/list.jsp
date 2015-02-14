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
	<li iconCls="icon-ok" onclick="setInuse('1')">可用</li>
	<li iconCls="icon-remove" onclick="setInuse('0')">不可用</li>
</ul>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="add()">增加</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">修改</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<a class="mini-menubutton" plain="true" iconCls="icon-control" menu="#popupMenu">设置</a>
	<span class="separator"></span>
	<input class="mini-combobox" id="searchby" data="[{id:'title',text:'标题'},{id:'keyword',text:'标识'}]" value="title" style="width:60px;" />
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
			<div field="inuse" width="40" headerAlign="center" align="center" renderer="onInuseRenderer">可用</div>
			<div field="keyword" width="200" headerAlign="center">标识</div>
			<div field="displayorder" width="40" headerAlign="center" align="center">排序</div>
			<div width="40" headerAlign="center" align="center" renderer="onActionRenderer" >图片</div>
			<div field="title" width="25%" headerAlign="center">标题</div>
			<div field="link" width="25%" headerAlign="center">标题链接</div>
			<div field="remark" width="50%" headerAlign="center">说明</div>
			<div field="modifyDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd">编辑日期</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");

	function onActionRenderer(e) {
		var value = e.record.imgpath;
		return value==''||value==null ? '' : '<a href="###" onclick="showImg(\''+value+'\')"><img src="${base}/resources/admin/images/icon/images.png" /></a>';
	}

	function onInuseRenderer(e) {
		var icon_yes = '<img src="${base}/resources/admin/images/icon/yes.png" />';
		var icon_no  = '<img src="${base}/resources/admin/images/icon/no.gif" />';
		return e.value=='1' ? icon_yes : icon_no;
	}

	grid.load();

	function showImg(src) {
		miniExt.win.showImage(src,{
			width: 800,
			height: 600
		});
	}
	function add() {
		miniExt.win.open({
			url: "ad/add.htm",
			title: "新建广告位",
			showMaxButton: true,
			width: 820,
			height: 700
		}).callback(function(){
			grid.reload();
		});
	}
	function edit() {
		var row = grid.getSelected();
		if (row) {
			miniExt.win.open({
				url: "ad/edit.htm?id="+row.id,
				title: "编辑广告位",
				showMaxButton: true,
				width: 820,
				height: 700
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

	function setInuse(inuse){
		var ids = miniExt.grid.getValues(grid);
		if (ids.length > 0) {
			$.ajax({
				type: "POST",
				url: "setInuse.do",
				dataType: "json",
				data: {ids:ids,inuse:inuse},
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
