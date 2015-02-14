<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../../inc.header.jsp" />
<style type="text/css">
	.cell-img{position: relative;overflow: visible;padding-bottom: 24px;}
	.cell-img div {position: absolute;overflow: visible;z-index: 9;top:0;}
	.cell-img img{width:20px;height:20px;}
	.cell-img:hover img{width:200px;height:200px;}
</style>
</head>
<body>
<!--menu-->
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<span class="separator"></span>
	<input class="mini-combobox" id="searchby" data="[{id:'nickname',text:'昵称'},{id:'content',text:'文本内容'}]" value="nickname" style="width:60px;" />
	<input class="mini-textbox" id="searchkey" onenter="search()"/>
	<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
	<span class="separator"></span>
	刷新时间：<input class="mini-combobox" id="time" data="[{value:0,text:'停止自动刷新'},{value:15,text:'15秒'},{value:30,text:'30秒'},{value:60,text:'1分钟'},{value:180,text:'3分钟'},{value:600,text:'10分钟'},{value:1200,text:'20分钟'},{value:1800,text:'30分钟'}]" valueField="value" value="0" onvaluechanged="valuechanged(e.value)" />
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" multiSelect="true" fitColumns="true"
		url="datalist.do" onshowrowdetail="onShowRowDetail"
	 >
		<div property="columns">
			<div type="indexcolumn"></div>
			<div type="checkcolumn"></div>
            <div type="expandcolumn" ></div>
			<div field="fromUserNameText" width="100" headerAlign="center" align="center">发送人</div>
			<div field="msgType" width="50" headerAlign="center" align="center">类别</div>
			<div field="content" width="100%" headerAlign="center">留言内容</div>
			<div field="lastreply" width="40%" headerAlign="center">最后回复</div>
			<div field="description" width="200" headerAlign="center" align="center">描述</div>
			<div field="createTime" width="80" headerAlign="center" align="center">日期</div>
		</div>
	</div>
</div>

<div id="editForm1" style="display:none;">
	<input class="mini-hidden" name="_uid" id="_uid"/>
	<div  class="mini-tabs" style="width:100%;" activeIndex="1">
		<div title="留言内容">
			<input name="content" id="content" class="mini-textarea" style="width:100%;height:107px;"/>
			<div style="height:27px;"></div>
		</div>
		<div title="最后回复">
			<input name="lastreply" id="lastreply" class="mini-textarea" style="width:100%;height:107px;"/>
			<div style="padding-top:5px;padding-left:8px;">
				<a class="Update_Button" href="javascript:reply();">提交回复</a> 【注：仅当用户最后一次主动发消息给公众号的48小时内可不限制发送次数，超过48小时不可发送】

			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	mini.parse();

	var grid = mini.get("datagrid1");

	var MsgType= {"text":"文本","image":"图片","voice":"语音","video":"视频","location":"位置","link":"链接"};
	grid.on("drawcell",function(e){
		if (e.field == "msgType") {
			e.cellHtml = MsgType[e.value];
		}
		else if (e.field == "createTime") {
			var date = new Date(e.value*1000);
			e.cellHtml = mini.formatDate(date,"yyyy-MM-dd");
		}
		else if (e.field == "content") {
			switch (e.record.msgType){
				case "text":
					e.cellHtml = e.record.content;
					break;
				case "image":
					e.cellCls = "cell-img";
					e.cellHtml = '<img src="'+ e.record.picUrl +'" />';
					break;
				case "voice":
					e.cellHtml = e.record.recognition+'<br>【'+ e.record.format +'】 MediaId:'+ e.record.mediaId;
					break;
				case "video":
					e.cellHtml = 'MediaId:'+ e.record.mediaId+'ThumbMediaId:'+ e.record.thumbMediaId;
					break;
				case "location":
					e.cellHtml = e.record.label+' 位置：'+e.record.locationX+'，'+e.record.locationY+'，'+e.record.scale;
					break;
				case "link":
					e.cellHtml = '<a href="'+ e.record.url +'" target="_blank" >'+e.record.title+'</a>';
					break;

			}
		}
	});

	grid.load();

	function dele() {
		var rows = grid.getSelecteds();
		if (rows.length > 0) {
			if (confirm("确定删除选中记录？")) {
				var ids = [];
				for (var i = 0, l = rows.length; i < l; i++) {
					var r = rows[i];
					ids.push(r.msgId);
				}
				//var id = ids.join(',');
				//grid.loading("操作中，请稍后......");
				$.ajax({
					type: "POST",
					url: "delete.do",
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
	function search() {
		var params={};
		var searchby = mini.get("searchby").getFormValue();
		var searchkey = mini.get("searchkey").getFormValue();
		params.searchby = searchby;
		params.searchkey= searchkey;
		grid.load(params);
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

	function reply() {
		var row_uid = $("#_uid").val();
		var row = grid.getRowByUID(row_uid);

		var content = mini.get('lastreply').getValue();
		if (!content){
			alert("消息不能为空");
			return;
		}
		var data = {};
		data.msgId = row.msgId;
		data.fromUserName = row.fromUserName;
		data.lastreply = content;

		var editForm = new mini.Form("#editForm1");
		editForm.loading("正在提交，请稍后......");
		$.ajax({
			url: "reply.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					top.miniExt.showMsg("保存成功");
					row = $.extend(row, data);
					grid.updateRow(row);
				}else{
					alert(json.message);
				}
			},
			complete: function (json) {
				editForm.unmask();
			}
		});
	}

	var _t;
	function valuechanged(timeout){
		if (timeout>0){
			_t = setInterval(function(){grid.load();},timeout*1000);
		}else{
			clearInterval(_t);
		}
	}

</script>
</body>
</html>
