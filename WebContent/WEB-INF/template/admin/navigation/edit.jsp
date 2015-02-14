<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script type="text/javascript" src="${base}/resources/admin/js/PRESETURL.js"></script>
</head>
<body>
<div class="mini-fit" style="padding:10px;">
  <form id="form1" onsubmit="return false">
    <input name="id" class="mini-hidden" value="${model.id}" />
    <input name="position" class="mini-hidden" value="${model.position}" />
    <input name="createDate" class="mini-hidden" value="${model.createDate}" />
    <div style="padding-left:11px;padding-bottom:5px;">
      <table style="table-layout:fixed;">
        <tr>
          <td style="width:80px;">名称：</td>
          <td><input name="title" id="title" class="mini-textbox" required="true" width="300" value="${model.title}" /></td>
        </tr>
        <tr>
          <td>预设：</td>
          <td><input class="mini-combobox" data="URLDATA" onitemclick="onItemClick" width="300" /></td>
        </tr>
        <tr>
          <td>链接：</td>
          <td><input name="url" id="url" class="mini-textbox" required="true" width="300" value="${model.url}" /></td>
        </tr>
        <tr>
          <td></td>
          <td><input type="checkbox" name="isBlankTarget" class="mini-checkbox" trueValue="1" falseValue="0" text="新窗口打开" checked="${model.isBlankTarget=='1'}"></td>
        </tr>
        <tr>
          <td>排序：</td>
          <td><input name="displayorder" class="mini-spinner" minValue="0" maxValue="999999999" value="${model.displayorder}" required="true" /></td>
        </tr>
        <tr>
          <td>状态：</td>
          <td><input type="checkbox" name="isShow" class="mini-checkbox" trueValue="1" falseValue="0" text="启用" checked="${model.isShow=='1'}"></td>
        </tr>
      </table>
    </div>
  </form>
</div>
<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
	<a class="mini-button" onclick="saveData()" style="width:60px;margin-right:20px;">保存</a>
	<a class="mini-button" onclick="closeWindow('cancel')" style="width:60px;">取消</a>
</div>

<script type="text/javascript">
	function onItemClick(e) {
		var url = e.item.value;
		if (url.substr(0,1)=='/'){
			url = "${base}"+url;
		}
		mini.get("url").setValue(url);
		mini.get("title").setValue(e.item.text);
	}

	mini.parse();

	var form = new mini.Form("#form1");

	function saveData() {
		form.validate();
		if (form.isValid() == false) {return;}

		form.loading("正在保存，请稍后......");
		$.ajax({
			url: "update.do",
			type: "POST",
			dataType: "json",
			data: form.getData(),
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					miniExt.win.callback();
					closeWindow();
				}else{
					alert(json.message);
				}
			},
			complete: function (json) {
				form.unmask();
			}
		});
	}

	function closeWindow(action) {
		miniExt.win.close(action);
	}
</script>
</body>
</html>
