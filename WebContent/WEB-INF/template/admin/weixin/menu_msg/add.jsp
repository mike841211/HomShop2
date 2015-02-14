<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../../inc.header.jsp" />
<script charset="utf-8" src="${base}/resources/common/editor/kindeditor-min.js"></script>
<script>
	var editor;
	KindEditor.ready(function(K) {
		editor = K.create('#editor', {
			uploadJson : '${KindEditor_uploadJson}',
			fileManagerJson : '${KindEditor_fileManagerJson}',
			allowFileManager : true,
			afterUpload : function(url) {
				addAttachments(url);
			}
		});
	});
	function addAttachments(url){
		var attachments = mini.get("attachments");
		// var values = attachments.getValue().split("|");
		// if (values[0]=="") {values[0]=url;}
		// else{values.push(url);}
		// attachments.setValue(values.join("|"));
		var values = attachments.getValue();
		if (values=="") {values=url;}
		else{values += "|"+url;}
		attachments.setValue(values);
	}
	function upload(id) {
		editor.loadPlugin('image', function() {
			editor.plugin.imageDialog({
				showRemote : true,
				clickFn : function(url, title, width, height, border, align) {
					//KindEditor('#'+id).val(url);
					mini.get(id).setValue(url);
					//$('#preview').attr('src',url);
					editor.hideDialog();
				}
			});
		});
	}
	function showImg(id) {
		var src = mini.get(id).getValue();
		if (src==''){
			alert('没有图片');
			return;
		}
		miniExt.win.open({
			url: src,
			title: "图片",
			width: 800,
			height: 600
		});
	}
</script>
</head>
<body>
<div class="mini-fit" style="padding:10px;">
  <form id="form1" onsubmit="return false">
    <input name="id" class="mini-hidden" />
	<input class="mini-hidden" name="attachments" id="attachments" />
    <div style="padding-left:11px;padding-bottom:5px;">
      <table style="table-layout:fixed;width:100%;">
        <tr>
          <td style="width:80px;">标题：</td>
          <td><input name="title" class="mini-textbox" width="437" required="true" requiredErrorText="标题不能为空" />
		  <label><input type="checkbox" id="continue">连续添加</label></td>
        </tr>
        <tr>
          <td>类别：</td>
          <td><input name="tbWxMenu.id" id="tbWxMenu" class="mini-treeselect" emptyText="类别" showClose="true" popupWidth="200" required="true"
		  valueField="id" textField="name" url="../menu/getTreeData.do" /></td>
        </tr>
        <tr>
          <td>封面：</td>
          <td><input name="cover" id="cover" class="mini-textbox" width="437" emptyText="大图片建议尺寸：360像素 * 200像素" />
		  <input class="mini-checkbox" trueValue="1" falseValue="0" name="showCover" id="showCover" text="显示封面" value="1" checked="true" >
		  <a class="mini-button" plain="true" iconCls="icon-add" onclick="upload('cover')" >上传</a>
		  <a class="mini-button" plain="true" iconCls="icon-add" onclick="showImg('cover')" >查看</a>
		  </td>
        </tr>
        <tr>
          <td>外部链接：</td>
		  <td><input name="link" class="mini-textbox" style="width:437px;" /></td>
		</tr>
        <tr>
          <td>摘要：</td>
		  <td><input name="summary" class="mini-textarea" style="width:437px;height:120px;" value="" /></td>
		</tr>
        </tr>
        <tr>
		  <td>设置：</td>
		  <td>
			<input class="mini-checkbox" trueValue="1" falseValue="0" name="isShow" id="isShow" text="发布" value="1" checked="true" >
		  </td>
        </tr>
        <tr>
          <td>内容：</td>
          <td><textarea id="editor" name="content" style="width:100%;height:290px;"></textarea></td>
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
	mini.parse();

	var tbWxMenu = mini.get("tbWxMenu");
	tbWxMenu.on('closeclick',function(e) {
		var obj = e.sender;
		obj.setText("");
		obj.setValue("");
	});

	var form = new mini.Form("#form1");

	function saveData() {
		form.validate();
		if (form.isValid() == false) {return;}

		//editor.sync();
		var data = form.getData(true,false);
		data["content"] = editor.html();

		$.ajax({
			url: "save.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					top.miniExt.showMsg("保存成功");
					//form.setChanged(false);
					if($("#continue").is(":checked")){
						form.reset();
						editor.html('');
						//miniExt.form.reset(form);
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
