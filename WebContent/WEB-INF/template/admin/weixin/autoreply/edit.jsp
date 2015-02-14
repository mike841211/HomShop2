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
    <input name="id" class="mini-hidden" value="${model.id}" />
    <input name="createDate" class="mini-hidden" value="${model.createDate}" />
    <input name="hits" class="mini-hidden" value="${model.hits}" />
	<input class="mini-hidden" name="attachments" id="attachments" value="${model.attachments}" />
    <div style="padding-left:11px;padding-bottom:5px;">
      <table style="table-layout:fixed;width:100%;">
        <tr>
          <td style="width:80px;">标题：</td>
          <td><input name="title" class="mini-textbox" width="437" required="true" requiredErrorText="标题不能为空" value="${model.title}" /></td>
        </tr>
        <tr>
          <td>关键字：</td>
		  <td><input name="keyword" class="mini-textbox" style="width:437px;" required="true" requiredErrorText="关键字不能为空" value="${model.keyword}" /></td>
		</tr>
        <tr>
          <td>关键字匹配：</td>
		  <td><div name="matching" class="mini-radiobuttonlist" textField="text" valueField="id" value="${model.matching}" data="[{'text':'完全匹配','id':'complete'},{'text':'模糊匹配','id':'partial'}]" ></div></td>
		</tr>
        <tr>
          <td>类别：</td>
          <td><select name="replyType" id="replyType" class="mini-ComboBox"  style="width:150px" value="${model.replyType}" >
				  <option value="text">文本消息</option>
				  <option value="news">图文消息</option>
				  </select></td>
        </tr>
        <tr>
          <td>封面：</td>
          <td><input name="cover" id="cover" class="mini-textbox" width="437" emptyText="大图片建议尺寸：360像素 * 200像素" value="${model.cover}" />
		  <input class="mini-checkbox" trueValue="1" falseValue="0" name="showCover" id="showCover" text="显示封面" value="${model.showCover}" checked="${model.showCover=='1'}" >
		  <a class="mini-button" plain="true" iconCls="icon-add" onclick="upload('cover')" >上传</a>
		  <a class="mini-button" plain="true" iconCls="icon-add" onclick="showImg('cover')" >查看</a>
		  </td>
        </tr>
        <tr>
          <td>外部链接：</td>
		  <td><input name="link" class="mini-textbox" style="width:437px;" value="${model.link}" /></td>
		</tr>
        <tr>
          <td>摘要：</td>
		  <td><input name="summary" class="mini-textarea" style="width:437px;height:70px;" value="${model.summary}" /></td>
		</tr>
        </tr>
        <tr>
		  <td>设置：</td>
		  <td>
			<input class="mini-checkbox" trueValue="1" falseValue="0" name="isShow" id="isShow" text="发布" value="${model.isShow}" checked="${model.isShow=='1'}" >
		  </td>
        </tr>
        <tr>
          <td>内容：</td>
          <td><textarea id="editor" name="content" style="width:100%;height:290px;">${model.content}</textarea></td>
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

	var form = new mini.Form("#form1");

	function saveData() {
		form.validate();
		if (form.isValid() == false) {return;}

		//editor.sync();
		var data = form.getData(true,false);
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
