<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../../inc.header.jsp" />
<style type="text/css">
	.cell-img{position: relative;overflow: visible;padding: 0 0 24px;}
	.cell-img div {position: absolute;overflow: visible;top:0;z-index: 9;}
	.cell-img span {position: absolute;overflow: visible;top:1px;left:40px;padding:3px;background:#fff;border:1px solid #ddd;display:none;}
	.cell-img img{width:20px;height:20px;}
	.cell-img:hover span {display:block;}
	.cell-img:hover span img{width:200px;height:200px;}
	.cell-img a{margin:0 5px;}
</style>
</head>
<body>
<div class="mini-toolbar" style="border-bottom:0;">
	<input class="mini-combobox" id="searchby" data="[{id:'feedBackId',text:'维权单号'}]" value="feedBackId" style="width:80px;" />
	<input class="mini-textbox" id="searchkey" onenter="search()" style="width:180px;"/>
	<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" ondrawcell="onDrawCell" multiSelect="true" fitColumns="true"
		url="datalist.do" onshowrowdetail="onShowRowDetail"
	>
		<div property="columns">
			<div type="indexcolumn"></div>
			<!-- <div type="checkcolumn"></div> -->
			<div field="feedBackId" width="150" headerAlign="center">维权单号</div>
			<div field="transId" width="210" headerAlign="center">支付单号</div>
			<div field="reason" width="100" headerAlign="center" align="center">投诉原因</div>
			<div field="solution" width="100" headerAlign="center" align="center">期望结果</div>
			<div field="extInfo" width="100%" headerAlign="center">备注</div>
			<div field="picUrl0" width="210" headerAlign="center">图片</div>
			<div field="timeStamp" width="120" align="center" headerAlign="center" dateFormat="yyyy-MM-dd">投诉时间</div>
			<div field="status" width="90" headerAlign="center" align="center">处理状态</div>
			<div width="40" headerAlign="center" align="center" renderer="onActionRenderer">操作</div>
		</div>
	</div>
</div>

<script type="text/javascript">

	/* 处理状态 */
	var STATUS = {"unconfirmed":"未处理","confirmed":"已解决","reject":"用户拒绝","user_unconfirmed":"等待用户确认"};

	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function onDrawCell(e){
		if (e.field == "picUrl0") {
			var record = e.record;
			var s = '';
			for (var i=0; i<5; i++){
				var src = record['picUrl'+i]
				if (src){
					s += '<a href="javascript:window.open(\''+src+'\');">图片'+i+'</a>';
				}
			}
			e.cellCls = "cell-img";
			e.cellHtml = s;
		} else if (e.field == "timeStamp") {
			e.cellHtml = mini.formatDate(new Date(e.value*1000), "yyyy-MM-dd HH:mm");
		} else if (e.field == "status") {
			e.cellHtml = STATUS[e.value];
		}
	};

	function onActionRenderer(e) {
		// var grid = e.sender;
		var record = e.record;
		var uid = record._uid;

		var s;
		s = ' <span class="buttonLink" >';
		if (record.status=="unconfirmed"){
			s += ' <a href="javascript:updatePayfeedback(\'' + uid + '\',\'' + record.feedBackId + '\',\'' + record.openId + '\')" >确认</a>';
		}
		s += "</span>";

		return s;
	}


	function search() {
		var params={};
		var searchby = mini.get("searchby").getFormValue();
		var searchkey = mini.get("searchkey").getFormValue();
		params.searchby = searchby;
		params.searchkey= searchkey;
		grid.load(params);
	}

	// *** 操作 ***

	function updatePayfeedback(uid,feedBackId,openId){
		var row = grid.getRowByUID(uid);
		if (row['status']!='unconfirmed'){
			return;
		}
		$.post(
			'updatePayfeedback.do',
			{"feedbackids":[feedBackId],"openids":[openId]},
			function(json) {
				if (json.status=="success"){
					row['status'] = 'user_unconfirmed';
					grid.updateRow(row);
				}else{
					alert(json.message);
				}
			},"json"
		);
	}
	function dele(row_uid){
		if (!confirm('确定删除订单？\n提示：只能删除已取消的订单！')){ return; }
		var row = grid.getRowByUID(row_uid);
		$.post(
			'delete.do',
			{"ids":row.id},
			function(json) {
				if (json.status=="success"){
					grid.removeRow(row);
				}else{
					alert(json.message);
				}
			},"json"
		);
	}
</script>
</body>
</html>
