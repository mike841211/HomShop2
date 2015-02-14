<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<style type="text/css">
.brands label {
	width: 100px;
	display: block;
	float: left;
	padding-right: 6px;
}
</style>
</head>
<body>
<div class="mini-fit" style="padding:10px;">
  <form id="form1" onsubmit="return false">
	<div id="tabs1" class="mini-tabs" activeIndex="0" style="height:260px;">
	  <div name="first" title="基本信息">
      <table style="table-layout:fixed;">
        <tr>
          <td style="width:80px;">分类名称：</td>
          <td><input name="name" id="name" class="mini-textbox" value="" required="true" requiredErrorText="名称不能为空" />
		  <label><input type="checkbox" id="continue">连续添加</label></td>
        </tr>
        <tr>
          <td>分类编号：</td>
          <td><input name="code" id="code" class="mini-textbox" required="true" /></td>
        </tr>
        <tr>
          <td>上级分类：</td>
          <td><input name="tbShopProductCategory.id" id="parentId" class="mini-treeselect"
			  valueField="id" textField="name" popupWidth="200" showClose="true"
			  value="" text=""
			  url="getProductCategoryTreeData.do"
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
          <td>页面关键词：</td>
          <td><input name="metaKeyword" id="metaKeyword" class="mini-textbox" style="width:350px;" /></td>
        </tr>
        <tr>
          <td>页面描述：</td>
          <td><input name="metaDescription" id="metaDescription" class="mini-textbox" style="width:350px;" value="" /></td>
        </tr>
      </table>
	  </div>
	  <div title="绑定品牌">
          <div class="brands">
			<c:forEach items="${brands}" var="brand" varStatus="varStatus"><label><input type="checkbox" name="brands" id="brands_${varStatus.index}" value="${brand.id}">${brand.name}</label></c:forEach>
		  </div>
	  </div>
	</div>
  </form>
</div>
<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
	<a class="mini-button" onclick="saveData()" style="width:60px;margin-right:20px;">保存</a>
	<a class="mini-button" onclick="closeWindow('cancel')" style="width:60px;">取消</a>
</div>

<script type="text/javascript">
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
		var brands = [];
		$('input[name=brands]:checked').each(function(i,item){
			brands.push(item.value);
		});
		//data['brands']=brands.join(',');
		data['brands']=brands;

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
