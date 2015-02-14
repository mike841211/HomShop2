<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
</head>
<body>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="updateShow('1')">显示</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="updateShow('0')">屏蔽</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<span class="separator"></span>
	<input class="mini-combobox" id="searchby" data="[{id:'username',text:'评论人'},{id:'ip',text:'IP'},{id:'contents',text:'内容'},{id:'replyContent',text:'回复'}]" value="username" style="width:80px;" />
	<input class="mini-textbox" id="searchkey" onenter="search()"/>
	<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" ondrawcell="onDrawCell" multiSelect="true" fitColumns="true"
		url="datalist.do" onshowrowdetail="onShowRowDetail"
	>
		<div property="columns">
			<div type="indexcolumn"></div>
			<div type="checkcolumn"></div>
            <!-- <div type="expandcolumn" ></div> -->
			<div name="action" width="50" headerAlign="center" align="center" renderer="onActionRenderer">操作</div>
			<div field="isShow" width="60" headerAlign="center" align="center">是否显示</div>
			<div field="replyContent" width="50" headerAlign="center" align="center">已回复</div>
			<div field="productName" width="100%" headerAlign="center">评论商品</div>
			<div field="username" width="100" headerAlign="center" align="center">评论人</div>
			<div field="createDate" width="140" align="center" headerAlign="center" dateFormat="yyyy-MM-dd HH:mm:ss">评论时间</div>
			<div field="ip" width="100" headerAlign="center" align="center">IP</div>
		</div>
	</div>
</div>

<div id="editForm1" style="display:none;">
	<div  class="mini-tabs" style="width:100%;" activeIndex="0">
		<div title="评论内容">
			<input class="mini-hidden" name="_uid" id="_uid"/>
			<input name="contents" id="contents" class="mini-textarea" style="width:100%;height:107px;"/>
		</div>
		<div title="回复内容">
			<input name="replyContent" id="replyContent" class="mini-textarea" style="width:100%;height:107px;"/>
		</div>
	</div>
	<div style="padding-top:5px;padding-left:8px;">
		<a class="Update_Button" href="javascript:replyComment();">保存</a>
	</div>
</div>

<script type="text/javascript">

	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function onDrawCell(e){
		if (e.field == "productName") {
			e.cellHtml = '<a href="${base}/product/item/'+e.record['product_id']+'.htm" target="_blank">'+e.value+'['+e.record['specifications']+']</a>';
		}
		if (e.field == "isShow") {
			e.cellHtml = e.value=='1'?'<span>显示</span>':'<span style="color:red;">屏蔽</span>';
		}
		if (e.field == "replyContent") {
			e.cellHtml = (e.value==null || e.value=='') ? '' : '是';
		}
	};

	function onActionRenderer(e) {
		// var grid = e.sender;
		var record = e.record;
		var uid = record._uid;
		var id = record.id;
		var s;
		s = ' <span class="buttonLink" >';
		s += ' <a href="javascript:void(0);" >查看</a>';
		s += "</span>";

		return s;
	}

	var editForm = document.getElementById("editForm1");
	function onShowRowDetail(e) {
		var row = e.record;

		//将editForm元素，加入行详细单元格内
		var td = grid.getRowDetailCellEl(row);
		td.appendChild(editForm);
		editForm.style.display = "";

		//表单加载信息
		var form = new mini.Form("editForm1");
		if (grid.isNewRow(row)) {
			form.reset();
		} else {
			form.setData(row);
		}
	}

	grid.on('rowdblclick',function(e) {
		var row = e.record;
		//显示行详细
		if(grid.isShowRowDetail(row)){
			grid.hideRowDetail(row);
		}else{
			grid.hideAllRowDetail();
			grid.showRowDetail(row);
		}
	});
	grid.on('cellclick',function(e) {
		if (e.column.name!='action'){
			return;
		}
		var row = e.record;
		//显示行详细
		if(grid.isShowRowDetail(row)){
			grid.hideRowDetail(row);
		}else{
			grid.hideAllRowDetail();
			grid.showRowDetail(row);
		}
	});



	function search() {
		var params={};
		var searchby = mini.get("searchby").getFormValue();
		var searchkey = mini.get("searchkey").getFormValue();
		params.searchby = searchby;
		params.searchkey= searchkey;
		grid.load(params);
	}

	function replyComment(){
		var row_uid = $("#_uid").val();
		var row = grid.getRowByUID(row_uid);

		var data = {};
		data.id = row.id;
		data.replyContent = mini.get('replyContent').getValue();
		//data.contents = mini.get('contents').getValue();
		$.post(
			'reply.do',
			data,
			function(json) {
				if (json.status=="success"){
					top.miniExt.showMsg("保存成功");
					row = $.extend(row, data);
					grid.acceptRecord(row);
				}else{
					alert(json.message);
				}
			},"json"
		);
	}

	function updateShow(p){
		var rows = grid.getSelecteds();
		if (rows.length > 0) {
			var ids = [];
			for (var i = 0, l = rows.length; i < l; i++) {
				var r = rows[i];
				ids.push(r.id);
			}
			$.post(
				"setShow.do",
				{ids:ids, bool:p},
				function (json) {
					if (json.status=="success"){
						grid.reload();
					}else{
						alert(json.message);
					}
				},"json"
			);
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
					url: "setDelete.do",
					dataType: "json",
					data: {ids:ids},
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
