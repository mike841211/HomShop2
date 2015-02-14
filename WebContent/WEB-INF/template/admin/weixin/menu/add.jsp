<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../../inc.header.jsp" />
<script type="text/javascript" src="${base}/resources/admin/js/PRESETURL.js"></script>
</head>
<body>
<div class="mini-fit" style="padding:10px;">
  <form id="form1" onsubmit="return false">
    <div style="padding-left:11px;padding-bottom:5px;">
      <table style="table-layout:fixed;">
        <tr>
          <td style="width:80px;">菜单名称：</td>
          <td><input name="name" id="name" class="mini-textbox" value="" required="true" requiredErrorText="名称不能为空" />
		  <label><input type="checkbox" id="continue">连续添加</label></td>
        </tr>
        <tr>
          <td>上级菜单：</td>
          <td><input name="tbWxMenu.id" id="parentId" class="mini-treeselect"
			  valueField="id" textField="name" popupWidth="200" showClose="true"
			  value="" text="" url="getTreeData.do"
			  />
          </td>
        </tr>
        <tr>
          <td>排序：</td>
          <td><input name="displayorder" id="displayorder" class="mini-textbox" vtype="int" /></td>
        </tr>
        <tr>
          <td>设置：</td>
          <td><input type="checkbox" name="inuse" id="inuse" class="mini-checkbox" trueValue="1" falseValue="0" text="启用" value="1" checked="true" ></td>
        </tr>
        <tr>
          <td>菜单类型：</td>
          <td>
		   <select name="menuType" id="menuType" class="mini-combobox"><option value="" selected></option><option value="click">消息</option><option value="view">链接</option></select>
		</td>
        </tr>
        <tr>
          <td>消息功能：</td>
          <td><select name="functionKey" id="functionKey" class="mini-combobox" >
		  <option value="">回复菜单消息</option>
		  <option value="query_member_score">查询积分</option>
		  <option value="member_signin">每日签到</option>
		  <option value="transfer_customer_service">联系客服</option>
		  </select> 仅限消息菜单</td>
        </tr>
        <tr>
          <td>消息类型：</td>
          <td> <select name="messageType" id="messageType" class="mini-combobox" >
		  <option value=""></option>
		  <option value="text">文本消息</option>
		  <option value="news">图文消息</option></select> 仅限消息菜单
		  </td>
        </tr>
        <tr>
          <td>链接地址：</td>
          <td><input name="url" id="url" class="mini-textbox" style="width:350px;" value="" /></td>
        </tr>
        <tr>
          <td>预设链接：</td>
          <td><input class="mini-combobox" data="URLDATA" onitemclick="onItemClick" width="350" /></td>
        </tr>
      </table>
	</div>
</div>
<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
	<a class="mini-button" onclick="saveData()" style="width:60px;margin-right:20px;">保存</a>
	<a class="mini-button" onclick="closeWindow('cancel')" style="width:60px;">取消</a>
</div>

<script type="text/javascript">
	function onItemClick(e) {
		var url = e.item.value;
		if (url.substr(0,1)=='/'){
			url = "${CacheUtil.getConfig('SHOP_URL')}"+url;
		}
		mini.get("url").setValue(url);
	}

	mini.parse();

	var ctrlParentId = mini.get("parentId");
	ctrlParentId.on('closeclick',function(e) {
		var obj = e.sender;
		obj.setText("");
		obj.setValue("");
	});
	miniExt.tree.setExpandOnLoad(ctrlParentId.tree);

	var form = new mini.Form("#form1");
	function saveData() {

		form.validate();
		if (form.isValid() == false) {return;}

		var data = form.getData(true,false);      //获取表单多个控件的数据
		//var json = mini.encode(data);   //序列化成JSON

		$.ajax({
			url: "save.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					top.miniExt.showMsg("保存成功");
					form.setChanged(false);
					if($("#continue").is(":checked")){
						form.reset();
						ctrlParentId.load(ctrlParentId.url);
					}else{
						miniExt.win.callback();
						closeWindow();
					}
				}else{
					alert(json.message);
				}
			}
		});
	}

	function closeWindow(action) {
		miniExt.win.close(action);
	}

</script>
</body>
</html>
