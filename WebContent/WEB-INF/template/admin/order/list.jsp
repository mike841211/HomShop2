<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<style type="text/css">
	.mini-buttonedit-readOnly .mini-buttonedit-buttons {display:none;}
</style>
</head>
<body>
<!--menu-->
<!-- <ul id="popupMenu" class="mini-menu" style="display:none;">
	<li iconCls="icon-edit" onclick="setStatus('orderStatus',1)"><span class="orderStatus-1">已处理</span></li>
	<li iconCls="icon-edit" onclick="setStatus('paymentStatus',2)"><span class="paymentStatus-2">已付款</span></li>
	<li iconCls="icon-edit" onclick="setStatus('shipStatus',2)"><span class="shipStatus-2">已发货</span></li>
	<li iconCls="icon-edit" onclick="setStatus('orderStatus',2)"><span class="orderStatus-2">已完成</span></li>
	<li>
	    <span iconCls="icon-edit">订单状态</span>
	    <ul>
		    <li onclick="setStatus('orderStatus',0)"><span class="orderStatus-0">未处理</span></li>
		    <li onclick="setStatus('orderStatus',1)"><span class="orderStatus-1">已处理</span></li>
		    <li onclick="setStatus('orderStatus',2)"><span class="orderStatus-2">已完成</span></li>
		    <li onclick="setStatus('orderStatus',3)"><span class="orderStatus-3">已作废</span></li>
	    </ul>
    </li>
	<li>
	    <span iconCls="icon-edit">付款状态</span>
	    <ul>
		    <li onclick="setStatus('paymentStatus',0)"><span class="paymentStatus-0">未付款</span></li>
		    <li onclick="setStatus('paymentStatus',2)"><span class="paymentStatus-2">已付款</span></li>
	    </ul>
    </li>
	<li>
	    <span iconCls="icon-edit">配送状态</span>
	    <ul>
		    <li onclick="setStatus('shipStatus',0)"><span class="shipStatus-0">未发货</span></li>
		    <li onclick="setStatus('shipStatus',2)"><span class="shipStatus-2">已发货</span></li>
	    </ul>
    </li>
</ul> -->
<div class="mini-toolbar" style="border-bottom:0;">
	<!-- <a class="mini-menubutton" plain="true" iconCls="icon-control" menu="#popupMenu">批量设置</a>
	<span class="separator"></span> -->
	<input class="mini-combobox" id="searchby" data="[{id:'code',text:'订单编号'},{id:'memberUsername',text:'会员账号'},{id:'name',text:'收货人'},{id:'paymentCode',text:'支付单号'}]" value="code" style="width:80px;" />
	<input class="mini-textbox" id="searchkey" onenter="search()"/>
	<a class="mini-button" plain="true" iconCls="icon-search" onclick="search()">查询</a>
	<a class="mini-button" plain="true" iconCls="icon-download" onclick="showWindow1()">导出订单</a>
</div>
<div class="mini-fit">
	<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
		idField="id" pageSize="20" ondrawcell="onDrawCell" multiSelect="true" fitColumns="true"
		url="datalist.do" onshowrowdetail="onShowRowDetail"
	>
		<div property="columns">
			<div type="indexcolumn"></div>
			<!-- <div type="checkcolumn"></div> -->
            <div type="expandcolumn" ></div>
			<div field="sn" width="100" headerAlign="center">订单编号</div>
			<div field="memberUsername" width="100" headerAlign="center">会员帐号</div>
			<div field="name" width="80" headerAlign="center">收货人</div>
			<div field="mobile" width="100" headerAlign="center">手机</div>
			<div field="totalAmount" width="100" headerAlign="center" align="right" dataType="currency">订单总额</div>
			<div field="orderStatus" width="70" headerAlign="center" align="center">订单状态</div>
			<div field="paymentStatus" width="70" headerAlign="center" align="center">付款状态</div>
			<div field="shippingStatus" width="70" headerAlign="center" align="center">配送状态</div>
			<div field="shippingMethod" width="70" headerAlign="center" align="center">配送方式</div>
			<div field="paymentMethod" width="70" headerAlign="center" align="center">付款方式</div>
			<div field="createDate" width="130" align="center" headerAlign="center" dateFormat="yyyy-MM-dd HH:mm:ss">下单时间</div>
			<div width="100%" headerAlign="center" renderer="onActionRenderer">操作</div>
		</div>
	</div>
</div>

<div id="editForm1" style="display:none;">
	<div  class="mini-tabs" style="width:100%;" activeIndex="0">
		<div title="金额信息">
			<input class="mini-hidden" name="_uid" id="_uid"/>
			<input class="mini-hidden" name="gainScoreMutiple" id="gainScoreMutiple"/>
			<table style="width:100%;">
				<tr>
					<td style="width:80px;">商品原金额：</td>
					<td style="width:130px;"><input name="totalPrice" id="totalPrice" class="mini-spinner" minValue="-999999" decimalPlaces="2" maxValue="999999" readonly /></td>
					<td style="width:80px;">调整金额：</td>
					<td><input name="adjustAmount" id="adjustAmount" class="mini-spinner" minValue="-999999" decimalPlaces="2" maxValue="999999" onvaluechanged="moneyChanged" required />（优惠填负数）</td>
				</tr>
				<tr style="display:none;;">
					<td>优惠券抵扣：</td>
					<td><input name="couponAmount" id="couponAmount" class="mini-spinner" minValue="0" decimalPlaces="2" maxValue="999999" readonly /></td>
				</tr>
				<tr>
					<td>积分抵扣：</td>
					<td><input name="useScoreAmount" id="useScoreAmount" class="mini-spinner" minValue="0" decimalPlaces="2" maxValue="999999" readonly /></td>
					<td>使用积分：</td>
					<td><input name="useScore" id="useScore" class="mini-spinner" minValue="0" maxValue="999999999" readonly /></td>
				</tr>
				<tr>
					<td>运费金额：</td>
					<td><input name="shippingFee" id="shippingFee" class="mini-spinner" minValue="0" decimalPlaces="2" maxValue="999999" onvaluechanged="moneyChanged" required /></td>
				</tr>
				<tr>
					<td>订单总额：</td>
					<td><input name="totalAmount" id="totalAmount" class="mini-spinner" minValue="-999999" decimalPlaces="2" maxValue="999999" readonly /></td>
					<td>获得积分：</td>
					<td><input name="gainScore" id="gainScore" class="mini-spinner" minValue="0" maxValue="999999999" readonly /></td>
				</tr>
				<tr>
					<td>支付单号：</td>
					<td colspan="3"><input name="paymentCode" id="paymentCode" class="mini-textbox" readonly style="width:343px;"/></td>
				</tr>
			</table>
			<div style="padding-top:5px;padding-left:88px;">
				<a class="Update_Button" href="javascript:updateOrderAmount();">保存</a>
			</div>
		</div>
		<div title="配送信息">
			<table style="width:100%;">
				<tr>
					<td style="width:80px;">收货人：</td>
					<td style="width:150px;"><input name="name" id="name" class="mini-textbox" /></td>
					<td style="width:80px;">手机：</td>
					<td style="width:150px;"><input name="mobile" id="mobile" class="mini-textbox" /></td>
					<td style="width:80px;">电话：</td>
					<td><input name="phone" id="phone" class="mini-textbox" /></td>
				</tr>
				<tr>
					<td>收货地址：</td>
					<td colspan="5"><input name="address" id="address" class="mini-textbox" width="600" /></td>
				</tr>
				<tr>
					<td>买家备注：</td>
					<td colspan="5"><input name="buyerRemark" id="buyerRemark" class="mini-textbox" width="600" readonly /></td>
				</tr>
				<tr>
					<td>物流公司：</td>
					<td colspan="3"><input class="mini-combobox" data="DataShippingCompany" value="" onitemclick="onShippingCompanyChanged" emptyText="常用物流" /> >>> <input name="shippingCompany" id="shippingCompany" class="mini-textbox" /></td>
					<td>物流单号：</td>
					<td><input name="shippingCode" id="shippingCode" class="mini-textbox" /></td>
				</tr>
			</table>
			<div style="padding-top:5px;padding-left:88px;">
				<a class="Update_Button" href="javascript:updateShippingInfo();">保存</a>
			</div>
		</div>
		<div title="商家备注">
			<input name="salerRemark" id="salerRemark" class="mini-textarea" style="width:100%;height:107px;"/>
			<div style="padding-top:5px;padding-left:8px;">
				<a class="Update_Button" href="javascript:updateSalerRemark();">保存</a>
			</div>
		</div>
	</div>
</div>

<div id="window1" class="mini-window" title="导出订单" style="width:500px;"
    showModal="true" allowResize="true" allowDrag="true"
    >
	<div class="mini-fit" style="padding:10px;">
		<div id="editForm2" class="form" >
		  <table style="table-layout:fixed;">
			<tr>
			  <td style="width:80px;">订单号：</td>
		      <td><input name="sn1" class="mini-textbox" value="" /> - <input name="sn2" class="mini-textbox" value="" /></td>
			</tr>
			<tr>
			  <td>订单日期：</td>
		      <td><input name="createDate1" class="mini-datepicker" value="" /> - <input name="createDate2" class="mini-datepicker" value="" /></td>
			</tr>
			<tr>
			  <td>收货人：</td>
		      <td><input name="name" class="mini-textbox" value="" /></td>
			</tr>
			<tr>
			  <td>会员名：</td>
		      <td><input name="memberUsername" class="mini-textbox" value="" /></td>
			</tr>
		  </table>
		</div>
	</div>
	<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
		<a class="mini-button" onclick="exportExcel()" style="width:60px;margin-right:20px;">导出</a>
		<a class="mini-button" onclick="window1.hide()" style="width:60px;">取消</a>
	</div>
</div>

<script type="text/javascript">

	/* 订单状态 */
	//  unconfirmed, confirmed, completed, cancelled;
	var orderStatus = {"unconfirmed":"未确认","confirmed":"已确认","completed":"已完成","cancelled":"已取消"};

	/* 付款状态 */
	// unpaid, partialPayment, paid, partialRefunds, refunded;
	var paymentStatus = {"unpaid":"未支付","partialPayment":"部分支付","paid":"已支付","partialRefunds":"部分退款","refunded":"全额退款"};

	/* 配送状态 */
	// unshipped, partialShipment, shipped, partialReturns, returned;
	var shipStatus = {"unshipped":"未发货","partialShipment":"部分发货","shipped":"已发货","partialReturns":"部分退货","returned":"全部退货"};

	var DataShippingCompany = [
		{ id: '', text: '选择'},
		{ id: '顺丰快递', text: '顺丰快递'},
		{ id: 'EMS', text: 'EMS'},
		{ id: '申通E物流', text: '申通E物流'},
		{ id: '圆通速递', text: '圆通速递'},
		{ id: '中通速递', text: '中通速递'},
		{ id: '韵达快运', text: '韵达快运'},
		{ id: '宅急送', text: '宅急送'},
		{ id: '联邦快递', text: '联邦快递'},
		{ id: '汇通快运', text: '汇通快运'},
		{ id: '华强物流', text: '华强物流'},
		{ id: '', text: '其它'}
	];
	function onShippingCompanyChanged(e){
		mini.get('shippingCompany').setValue(e.sender.getValue());
	}

	var gainScoreMutiple; // 积分获得参数
	function moneyChanged(){
		var totalPrice = mini.get('totalPrice');
		var adjustAmount = mini.get('adjustAmount');
		var shippingFee = mini.get('shippingFee');
		var totalAmount = mini.get('totalAmount');
		totalAmount.setValue(totalPrice.getValue() + adjustAmount.getValue() + shippingFee.getValue());

		var gainScore = mini.get('gainScore');
		var couponAmount = mini.get('couponAmount');
		var useScoreAmount = mini.get('useScoreAmount');
		gainScore.setValue(Math.floor(totalPrice.getValue() + adjustAmount.getValue() - couponAmount.getValue() - useScoreAmount.getValue())*gainScoreMutiple);
	}

	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

	function onDrawCell(e){
		if (e.field == "orderStatus") {
			e.cellHtml = '<span class="orderStatus_'+e.value+'">'+orderStatus[e.value]+'</span>';
		}else if (e.field == "paymentStatus") {
			e.cellHtml = '<span class="paymentStatus_'+e.value+'">'+paymentStatus[e.value]+'</span>';
		}else if (e.field == "shippingStatus") {
			e.cellHtml = '<span class="shippingStatus_'+e.value+'">'+shipStatus[e.value]+'</span>';
		}else if (e.field == "deliveryTypeName") {
			if (e.record['freightCollect']=='1'){ //运费到付
				e.cellHtml = e.value+'<span class="shipStatus_'+e.value+'">(到付)</span>';
			}else{
				e.cellHtml = e.value;
			}
		}
	};

	function onActionRenderer(e) {
		// var grid = e.sender;
		var record = e.record;
		var uid = record._uid;
		var id = record.id;
		var os = record.orderStatus,
			ps = record.paymentStatus,
			ss = record.shippingStatus;

		var s;
		s = ' <span class="buttonLink" >';
		//s += ' <a href="./orderView.htm?id=' + id + '" target="_blank" >查看</a>';
		s += ' <a href="javascript:view(\'' + id + '\')" >查看</a>';
		if (os=="unconfirmed"){
			s += ' <a href="javascript:confirmed(\'' + uid + '\')" >已确认</a>';
			s += ' <a href="javascript:cancelled(\'' + uid + '\')" >取消订单</a>';
		} else if (os=="cancelled"){
			s += ' <a href="javascript:dele(\'' + uid + '\')">删除</a>';
		} else if (os=="completed"){
		} else {
			if (ps=="unpaid"){
				s += ' <a href="javascript:paid(\'' + uid + '\')" >已付款</a>';
			}
			if (ps=="paid" && ss=="unshipped"){
				s += ' <a href="javascript:shipped(\'' + uid + '\')" >发货</a>';
			}
			if (ss=="shipped"){
				s += ' <a href="javascript:completed(\'' + uid + '\')" >完成</a>';
			}
		}
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
			// 积分获得参数
			var gainScore = mini.get('gainScore');
			var totalPrice = mini.get('totalPrice');
			var adjustAmount = mini.get('adjustAmount');
			var couponAmount = mini.get('couponAmount');
			var useScoreAmount = mini.get('useScoreAmount');
			//gainScoreMutiple = gainScore.getValue()/Math.floor(totalPrice.getValue() + adjustAmount.getValue() - couponAmount.getValue() - useScoreAmount.getValue());
			gainScoreMutiple = mini.get('gainScoreMutiple').getValue();
			// 最大优惠限制
			adjustAmount.setMinValue(-(totalPrice.getValue()-useScoreAmount.getValue()-couponAmount.getValue()));
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



	function search() {
		var params={};
		var searchby = mini.get("searchby").getFormValue();
		var searchkey = mini.get("searchkey").getFormValue();
		params.searchby = searchby;
		params.searchkey= searchkey;
		grid.load(params);
	}

	function updateOrderAmount(){
		var row_uid = $("#_uid").val();
		var row = grid.getRowByUID(row_uid);

		if (row.orderStatus=="completed" || row.orderStatus=="cancelled"){
			alert('订单'+orderStatus[row.orderStatus]+'，不能修改！');
			return;
		}
		if (row.paymentStatus!="unpaid"){
			alert('订单'+paymentStatus[row.paymentStatus]+'，不能修改！');
			return;
		}

		var data = {};
		data.id = row.id;
		data.adjustAmount = mini.get('adjustAmount').getValue();
		data.shippingFee = mini.get('shippingFee').getValue();

		var editForm = new mini.Form("#editForm1");
		editForm.loading("正在保存，请稍后......");
		$.ajax({
			url: "updateOrderAmount.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					top.miniExt.showMsg("保存成功");
					row = $.extend(row, data);
					row.totalAmount = row.totalPrice + row.shippingFee + row.adjustAmount;
					row.gainScore = mini.get('gainScore').getValue();
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
	function updateShippingInfo(){
		var row_uid = $("#_uid").val();
		var row = grid.getRowByUID(row_uid);

		if (row.orderStatus=="completed" || row.orderStatus=="cancelled"){
			alert('订单'+orderStatus[row.orderStatus]+'，不能修改！');
			return;
		}
		if (row.shippingStatus!="unshipped"){
			alert('订单'+shippingStatus[row.shippingStatus]+'，不能修改！');
			return;
		}

		var data = {};
		data.id = row.id;
		data.name = mini.get('name').getValue();
		data.mobile = mini.get('mobile').getValue();
		data.phone = mini.get('phone').getValue();
		data.address = mini.get('address').getValue();
		data.shippingCompany = mini.get('shippingCompany').getValue();
		data.shippingCode = mini.get('shippingCode').getValue();

		var editForm = new mini.Form("#editForm1");
		editForm.loading("正在保存，请稍后......");
		$.ajax({
			url: "updateShippingInfo.do",
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
	function updateSalerRemark(){
		var row_uid = $("#_uid").val();
		var row = grid.getRowByUID(row_uid);

		var data = {};
		data.id = row.id;
		//data.id = mini.get('id').getValue();
		data.salerRemark = mini.get('salerRemark').getValue();

		var editForm = new mini.Form("#editForm1");
		editForm.loading("正在保存，请稍后......");
		$.ajax({
			url: "updateSalerRemark.do",
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


	// *** 操作 ***

	function view(id) {
		miniExt.win.open({
			url: "${base}/admin/order/view.htm?id="+id,
			title: "订单信息",
			width: 850,
			height: 500
		});
	}
	function confirmed(row_uid){
		if (!confirm("订单信息已确认？")){return;}
		var row = grid.getRowByUID(row_uid);
		row.orderStatus = "confirmed";
		$.post(
			'confirmed.do',
			{"id":row.id,"status":row.orderStatus},
			function(json) {
				if (json.status=="success"){
					grid.updateRow(row);
				}else{
					alert(json.message);
				}
			},"json"
		);
	}
	function cancelled(row_uid){
		if (!confirm('确定取消订单此订单？')){ return; }
		var row = grid.getRowByUID(row_uid);
		row.orderStatus = "cancelled";
		$.post(
			'cancelled.do',
			{"id":row.id,"status":row.orderStatus},
			function(json) {
				if (json.status=="success"){
					grid.updateRow(row);
				}else{
					alert(json.message);
				}
			},"json"
		);
	}
	function paid(row_uid){
		if (!confirm('确定已收到货款？')){ return; }
		var row = grid.getRowByUID(row_uid);
		row.paymentStatus = "paid";
		$.post(
			'paid.do',
			{"id":row.id,"status":row.paymentStatus},
			function(json) {
				if (json.status=="success"){
					grid.updateRow(row);
				}else{
					alert(json.message);
				}
			},"json"
		);
	}
	function shipped(row_uid){
		var row = grid.getRowByUID(row_uid);
		var shippingCompany = row.shippingCompany ? row.shippingCompany : "没有填写物流公司";
		var shippingCode = row.shippingCode ? row.shippingCode : "没有填写物流单号";
		var message = "确定发货？\n";
		message += "\n物流公司：" + shippingCompany;
		message += "\n物流单号：" + shippingCode;
		if (!confirm(message)){ return; }
		row.shippingStatus = "shipped";
		$.post(
			'shipped.do',
			{"id":row.id,"status":row.shippingStatus},
			function(json) {
				if (json.status=="success"){
					grid.updateRow(row);
				}else{
					alert(json.message);
				}
			},"json"
		);
	}
	function completed(row_uid){
		if (!confirm('确定完成？')){ return; }
		var row = grid.getRowByUID(row_uid);
		row.orderStatus = "completed";
		$.post(
			'completed.do',
			{"id":row.id,"status":row.orderStatus},
			function(json) {
				if (json.status=="success"){
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

	var window1 = mini.get("window1");
	function showWindow1(){
		window1.show();
	}
	function exportExcel(){
		var params = new mini.Form("editForm2").getData(true);
		window.open('${base}/admin/order/export.do?'+$.param(params),'export');
	}
</script>
</body>
</html>
