<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../../inc.header.jsp" />
</head>
<body>
<!--menu-->
<ul id="popupMenu" class="mini-menu" style="display:none;">
	<li iconCls="icon-ok" onclick="setStatus('isShow','1')">发布</li>
	<li iconCls="icon-remove" onclick="setStatus('isShow','0')">隐藏</li>
	<li iconCls="icon-ok" onclick="setStatus('showCover','1')">显示封面</li>
	<li iconCls="icon-remove" onclick="setStatus('showCover','0')">隐藏封面</li>
</ul>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="add()">增加</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">修改</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<a class="mini-menubutton" plain="true" iconCls="icon-control" menu="#popupMenu">设置</a>
	<span class="separator"></span>
	<input class="mini-combobox" id="searchby" data="[{id:'title',text:'标题'}]" value="title" style="width:60px;" />
	<input class="mini-textbox" id="searchkey" onenter="search()"/>
	<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" multiSelect="true" fitColumns="true" <%-- allowCellWrap="true" --%>
		url="datalist.do"
	 >
		<div property="columns">
			<div type="indexcolumn"></div>
			<div type="checkcolumn"></div>
			<div field="isShow" width="40" headerAlign="center" align="center">发布</div>
			<!-- <div field="topIndex" width="40" headerAlign="center" align="center">置顶</div> -->
			<div field="replyType" width="50" headerAlign="center" align="center">分类</div>
			<div field="matching" width="50" headerAlign="center" align="center">匹配</div>
			<div field="keyword" width="200" headerAlign="center">关键字</div>
			<div field="title" width="200" headerAlign="center">标题</div>
			<div field="cover" width="40" headerAlign="center" align="center" >封面</div>
			<div field="showCover" width="70" headerAlign="center" align="center" >显示封面</div>
			<div field="summary" width="100%" headerAlign="center">摘要</div>
			<div field="modifyDate" width="90" align="center" headerAlign="center" dateFormat="yyyy-MM-dd" allowSort="true">编辑日期</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");

	var ReplyType= {"text":"文本","news":"图文"};
	grid.on("drawcell",function(e){
		if (e.field == "isShow") {
			e.cellHtml = e.value=='1' ? '<img src="${base}/resources/admin/images/icon/yes.png" />' : '<img src="${base}/resources/admin/images/icon/no.gif" />';
		}
		else if (e.field == "showCover") {
			e.cellHtml = e.value=='1' ? '<img src="${base}/resources/admin/images/icon/yes.png" />' : '<img src="${base}/resources/admin/images/icon/no.gif" />';
		}
		else if (e.field == "cover") {
			e.cellHtml = e.value==''||e.value==null ? '' : '<a href="###" onclick="miniExt.win.showImage(\''+e.value+'\')"><img src="${base}/resources/admin/images/icon/images.png" /></a>';
		}
		else if (e.field == "replyType") {
			e.cellHtml = ReplyType[e.value];
		}
		else if (e.field == "matching") {
			e.cellHtml = e.value=='complete' ? '完全' : '模糊';
		}
	});

	grid.load();

	function add() {
		miniExt.win.open({
			url: "weixin/autoreply/add.htm",
			title: "新建自动回复",
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
				url: "weixin/autoreply/edit.htm?id="+row.id,
				title: "编辑自动回复",
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
		var article_category = mini.get("article_category").getFormValue();
		params.searchby = searchby;
		params.searchkey= searchkey;
		params.categoryId= article_category;
		grid.load(params);
	}

	function setStatus(field,status){
		//var ids = getGridIds();
		var ids = miniExt.grid.getValues(grid);
		if (ids.length > 0) {
			$.ajax({
				type: "POST",
				url: "setStatus.do",
				dataType: "json",
				data: {ids:ids,field:field,status:status},
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
