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
		editor = K.create('textarea[name="introduction"]', {
			uploadJson : '${KindEditor_uploadJson}',
			fileManagerJson : '${KindEditor_fileManagerJson}',
			allowFileManager : true
		});

		K('#btnLogo').click(function() {
			editor.loadPlugin('image', function() {
				editor.plugin.imageDialog({
					showRemote : true,
					//imageUrl : K('#logo').val(),
					//imageUrl : mini.get("logo").getValue(),
					clickFn : function(url, title, width, height, border, align) {
						//console.log(K('#logo$value'));
						//K('input[name="logo"]').val(url);
						mini.get("logo").setValue(url);
						editor.hideDialog();
					}
				});
			});
		});
	});
</script>
</head>
<body>
<div class="mini-fit" style="padding:10px;">
  <form id="form1" onsubmit="return false">
    <input name="id" class="mini-hidden" value="${brand.id}" />
    <input name="createDate" class="mini-hidden" value="${brand.createDate}" />
	<div id="tabs1" class="mini-tabs" activeIndex="0" style="height:490px;">
	  <div name="first" title="基本信息">
		  <table style="table-layout:fixed;">
			<tr>
			  <td style="width:70px;">品牌名称：</td>
			  <td><input name="name" class="mini-textbox" required="true" requiredErrorText="品牌名称不能为空" value="${brand.name}" /></td>
			</tr>
			<tr>
			  <td>英文名：</td>
			  <td><input name="enname" id="enname" class="mini-textbox" value="${brand.enname}" /></td>
			</tr>
			<tr>
			  <td>LOGO：</td>
			  <td><input name="logo" id="logo" class="mini-textbox" value="${brand.logo}" /> <a class="mini-button" iconCls="icon-add" plain="true" id="btnLogo">上传</a></td>
			</tr>
			<tr>
			  <td>官网：</td>
			  <td><input name="url" id="url" class="mini-textbox" value="${brand.url}" /></td>
			</tr>
			<tr>
			  <td>排序：</td>
			  <td><input name="displayorder" class="mini-textbox" vtype="int" value="${brand.displayorder}" /></td>
			</tr>
		  </table>
	  </div>
	  <div title="品牌介绍">
		  <textarea name="introduction" style="width:99%;height:450px;">${brand.introduction}</textarea>
	  </div>
	</div>
  </form>
 </div>
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
		data["introduction"] = editor.html();

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
