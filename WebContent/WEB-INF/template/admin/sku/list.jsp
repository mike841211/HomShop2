<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<style type="text/css">
	a {text-decoration:none;}
	.specUL {margin:0;padding:0;}
	.specLI {list-style-type:none;float:left;margin-right: 10px;}
</style>
</head>
<body>
<div class="mini-fit" style="padding:5px;">
	<div class="mini-toolbar" style="border-bottom:0;">
		<a class="mini-button" plain="true" iconCls="icon-add" onclick="newRow()">增加</a>
		<a class="mini-button" plain="true" iconCls="mini-pager-reload" onclick="grid.reload()">刷新</a>
	</div>
	<div class="mini-fit">
		<div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;" allowResize="false"
			url="datalist.do?id=${product.id}"  idField="id" showToolbar="true" onshowrowdetail="onShowRowDetail" showPager="false"
		>
			<div property="toolbar" style="padding:0 10px;" >
				<h2>[${product.sn}] ${product.name}</h2>
			</div>
			<div property="columns">
				<div type="indexcolumn"></div>
	            <div type="expandcolumn" ></div>
				<div name="action" width="100" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;"></div>
				<div field="sn" width="100" headerAlign="center">商品货号</div>
				<c:forEach items="${product.tbShopSpecifications}" var="item">
				<div name="${item.id}" field="specificationJson" width="60" headerAlign="center" align="center" renderer="onSpecRenderer" align="center">${item.name}</div></c:forEach>
				<div field="weight" width="60" headerAlign="center" align="right" dataType="int">重量(克)</div>
				<div field="cost" width="70" dataType="currency" headerAlign="right" align="right">成本</div>
				<div field="price" width="70" dataType="currency" headerAlign="right" align="right">单价</div>
				<div field="stock" width="60" headerAlign="center" align="right" dataType="int">库存</div>
				<div field="blockedStock" width="60" headerAlign="center" align="right" dataType="int">冻结库存</div>
				<div field="sales" width="60" headerAlign="center" align="right" dataType="int">销量</div>
				<div field="modifyDate" width="100" dateFormat="yyyy-MM-dd" headerAlign="center" align="center">编辑日期</div>
				<div field="createDate" width="100" dateFormat="yyyy-MM-dd" headerAlign="center" align="center">创建日期</div>
				<div width="100%"></div>
			</div>
		</div>
	</div>
</div>

<div id="editForm1" style="display:none;position:relative;">
    <input class="mini-hidden" name="_uid" id="_uid" value="" />
    <input class="mini-hidden" name="id" value="" />
    <table style="width:100%;">
        <tr>
            <td style="text-align:right;padding-top:5px;padding-right:20px;" colspan="6">
			<ul class="specUL">
			<c:forEach items="${product.tbShopSpecifications}" var="item">
            <li class="specLI"><div type="comboboxcolumn" width="100"  align="center" headerAlign="center">${item.name}：
                <input property="editor" class="mini-combobox" style="width:100px;" data="specData['${item.id}'].values" name="specifications" id="spec_${item.id}" />
            </div></li></c:forEach>
			</ul>
            </td>
        </tr>
        <tr>
            <td>图片：</td>
            <td colspan="5"><input name="sampleImage" id="sampleImage" class="mini-combobox" data="IMAGESTORE" style="width:400px;" valueField="src" textField="src" value="" /><a class="mini-button" plain="true" iconCls="icon-add" onclick="showImage()" >查看</a></td>
        </tr>
        <tr>
            <td style="width:80px;">系统编号：</td>
            <td style="width:150px;"><input name="syssn" class="mini-textbox" value="" emptyText="自动生成" vtype="int" readonly /></td>
            <td style="width:80px;">SKU货号：</td>
            <td style="width:150px;"><input name="sn" class="mini-textbox" value="" /></td>
            <td style="width:80px;"></td>
            <td></td>
        </tr>
        <tr>
            <td>成本：</td>
            <td><input name="cost" class="mini-spinner" minValue="0" decimalPlaces="2" maxValue="9999999999999" /></td>
            <td>单价：</td>
            <td><input name="price" class="mini-spinner" minValue="0" decimalPlaces="2" maxValue="9999999999999" /></td>
            <td>市场价：</td>
            <td><input name="marketPrice" class="mini-spinner" minValue="0" decimalPlaces="2" maxValue="9999999999999" /></td>
        </tr>
        <tr>
            <td>重量：</td>
            <td><input name="weight" class="mini-spinner" minValue="0" maxValue="9999999999999" /></td>
            <td>库存：</td>
            <td><input name="stock" class="mini-spinner" minValue="0" maxValue="9999999999999" /></td>
            <td>库位：</td>
            <td><input name="storeLocation" class="mini-textbox" /></td>
        </tr>
        <tr>
            <td colspan="6">
                <a class="button " href="javascript:updateRow();">保存</a>
                <a class="button" href="javascript:cancelRow();">取消</a>
            </td>
        </tr>
    </table>
</div>

<script type="text/javascript">
	var IMAGESTORE = [];
	var imageStore = "${product.imageStore}".split("|");
	for (var i=0,len=imageStore.length; i<len; i++){
		IMAGESTORE.push({src:imageStore[i]});
	}

	var specData = {<c:forEach items="${product.tbShopSpecifications}" var="item" varStatus="itemStatus">
	"${item.id}" : {name:"${item.name}",values:[<c:forEach items="${item.tbShopSpecificationValues}" var="valueitem" varStatus="valueitemStatus">
	{"id":"${valueitem.id}", "text":"${valueitem.name}" }<c:if test="${!valueitemStatus.last}">,</c:if></c:forEach>
	]}<c:if test="${!itemStatus.last}">,</c:if>
	</c:forEach>};

	mini.parse();

	var grid = mini.get("datagrid1");
	grid.load();

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

	// 选中规格
	function setSpecData(row) {
		if (!row.specificationJson){return;}
		var specs = mini.decode(row.specificationJson);
		for (var i=0,l=specs.length; i<l; i++){
			mini.get('spec_'+specs[i].id).setValue(specs[i].value_id);
		}
	}
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
			// 选中规格
			setSpecData(row);
		}
	}


	function onActionRenderer(e) {
		var grid = e.sender;
		var record = e.record;
		var uid = record._uid;
		var rowIndex = e.rowIndex;

		var s = '<a class="Edit_Button" href="javascript:editRow(\'' + uid + '\')">编辑</a> '
				+ '<a class="Delete_Button" href="javascript:delRow(\'' + uid + '\')">删除</a> '
				+ '<a class="Copy_Button" href="javascript:copyRow(\'' + uid + '\')">复制</a> ';

		return s;
	}

	function onSpecRenderer(e) {
		if (e.record._state=="added"){ //新建
			return "";
		}
		var data = specData[e.column.name].values;
		for (var i = 0, l = data.length; i < l; i++) {
			if (e.value && e.value.indexOf(data[i]["id"])>-1){
				return data[i]["text"];
			}
		}
		return "";
	}


	var editForm = document.getElementById("editForm1");

	function newRow() {
		var row = {};
		grid.addRow(row, 0);
		editRow(row._uid);
		for (var i in specData){
			mini.get("spec_"+i).select(0);
		}
	}

	function editRow(row_uid) {
		var row = grid.getRowByUID(row_uid);
		if (row) {
			//显示行详细
			grid.hideAllRowDetail();
			grid.showRowDetail(row);

			//将editForm元素，加入行详细单元格内
			var td = grid.getRowDetailCellEl(row);
			td.appendChild(editForm);
			editForm.style.display = "";

			//表单加载信息
			var form = new mini.Form("editForm1");
			form.setData(row);
			setSpecData(row);

			grid.doLayout();
		}
	}
	function cancelRow() {
		//grid.reload();

		var row_uid = $('#_uid').val();
		var row = grid.getRowByUID(row_uid);
		if (row) {
			//显示行详细
			grid.hideAllRowDetail();

			//表单加载员工信息
			var form = new mini.Form("editForm1");
			if (grid.isNewRow(row)) {
				grid.removeRow(row);
			} else {
				form.setData(row);
				// 选中规格
				setSpecData(row);
			}
		}
	}
	function delRow(row_uid) {
		if (confirm("确定删除此记录？")) {
			var row = grid.getRowByUID(row_uid);
			if (grid.isNewRow(row)) {
				grid.removeRow(row);
				return;
			}
			if (row) {
				grid.loading("删除中，请稍后......");
				$.ajax({
					url: "delete.do",
					type: "POST",
					dataType: "json",
					data: {"ids":row.id},
					cache: false,
					success: function (json) {
						if (json.status=="success"){
							grid.removeRow(row);
						}else{
							alert(json.message);
						}
					},
					complete: function (json) {
						grid.unmask();
					}
				});
			}
		}
	}
	function updateRow() {
		var form = new mini.Form("editForm1");

		var data = form.getData(true,false);
		data['tbShopProduct.id'] = "${product.id}";

		data['specificationJson'] = [];
		var hasEmpty = false;
		$('input[name=specifications]').each(function(i,item){
			var specification = {};
			var id = $(this).attr('id').substr(5,32);
			specification['id'] = id;
			specification['name'] = specData[id].name;
			if (item.value=='undefined'){
				hasEmpty = true;
				alert(specification['name']+" 没有选择！");
				return false;
			}
			specification['value_id'] = item.value;
			specification['value_name'] = mini.get("spec_"+id).text;
			data['specificationJson'].push(specification);
		});
		if (hasEmpty){
			return;
		}

		//检查是否重复
		var rows = grid.getData();
		if (rows.length>1){
			for (var i=0, l=rows.length; i<l; i++) {
				var row = rows[i];
				if (row._state=="added" || data.id==row.id){ //新建或当前行
					continue;
				}
				var exists = true;
				for (var i2=0, l2=data['specificationJson'].length; i2<l2; i2++) {
					if (row.specificationJson.indexOf(data['specificationJson'][i2]["value_id"])==-1){
						exists = false;
						break;
					}
				}
				if (exists){
					alert("已存在相同规格SKU");
					return;
				}
			}
		}
		data['specificationJson'] = mini.encode(data['specificationJson']);
		delete data['specifications']; //不传递

		grid.loading("正在保存，请稍后......");
		$.ajax({
			url: data["id"]=="" ? "save.do" : "update.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					miniExt.showMsg("保存成功");
					grid.reload();
				}else{
					alert(json.message);
				}
			},
			complete: function (json) {
				grid.unmask();
			}
		});
	}
	function copyRow(row_uid){
		//复制信息
		var row = mini.copyTo({},grid.getRowByUID(row_uid));
		delete row['id'];
		delete row['syssn'];
		delete row['sn'];
		delete row['sampleImage'];
		grid.addRow(row, 0);
		editRow(row._uid);
	}
	function showImage(){
		//复制信息
		var src = mini.get('sampleImage').getValue();
		if (src){
			miniExt.win.showImage(src);
		}
	}
</script>
</body>
</html>
