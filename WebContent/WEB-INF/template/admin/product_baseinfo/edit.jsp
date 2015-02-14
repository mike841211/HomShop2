<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script charset="utf-8" src="${base}/resources/common/editor/kindeditor-min.js"></script>
<script>
	var editor;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="content"]', {
			uploadJson : '${KindEditor_uploadJson}',
			fileManagerJson : '${KindEditor_fileManagerJson}',
			allowFileManager : true
		});
	});
</script>
</head>
<body>
<div class="mini-fit" style="padding:10px;">
  <form id="form1" onsubmit="return false">
      <input name="id" class="mini-hidden" value="${productBaseinfo.id}" />
      <input name="createDate" class="mini-hidden" value="${productBaseinfo.createDate}" />
      <table style="table-layout:fixed;">
        <tr>
          <td style="width:70px;">标题：</td>
          <td><input name="title" class="mini-textbox" style="width:500px" required="true" requiredErrorText="品牌名称不能为空" value="${productBaseinfo.title}" /></td>
        </tr>
        <tr>
          <td>标识：</td>
          <td><input name="sign" id="sign" class="mini-textbox" style="width:500px" value="${productBaseinfo.sign}" /></td>
        </tr>
        <tr>
          <td>内容：</td>
          <td><textarea name="content" style="width:100%;height:430px;">${productBaseinfo.content}</textarea></td>
        </tr>
      </table>
  </form>
</div>
<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
	<a class="mini-button" onclick="saveData()" style="width:60px;margin-right:20px;">保存</a>
	<a class="mini-button" onclick="closeWindow('cancel')" style="width:60px;">取消</a>
</div>

<script type="text/javascript">
	mini.parse();

	var form = new mini.Form("#form1");

	function saveData() {
		form.validate();
		if (form.isValid() == false) {return;}

		//editor.sync();
		var data = form.getData(true,true);
		data["content"] = editor.html();

		$.ajax({
			url: "update.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					miniExt.win.callback();
					closeWindow();
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
