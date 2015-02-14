<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script charset="utf-8" src="${base}/resources/common/editor/kindeditor-min.js"></script>
<script>
	//KindEditor.options.filterMode = false;
	var editor;
	KindEditor.ready(function(K) {
		editor = K.create('#editor', {
			uploadJson : '${KindEditor_uploadJson}',
			fileManagerJson : '${KindEditor_fileManagerJson}',
			filterMode : false,
			designMode : false,
			//wellFormatMode : false,
			allowFileManager : true
		});
	});

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
    <input name="id" class="mini-hidden" value="${advert.id}" />
    <input name="createDate" class="mini-hidden" value="${advert.createDate}" />
    <div style="padding-left:11px;padding-bottom:5px;">
      <table style="table-layout:fixed;width:100%;">
        <tr>
          <td style="width:80px;">标题：</td>
          <td><input name="title" class="mini-textbox" width="437" value="${advert.title}" /></td>
        </tr>
        <tr>
          <td>图片：</td>
          <td><input name="imgpath" id="imgpath" class="mini-textbox" width="437" value="${advert.imgpath}" />
		  <a class="mini-button" plain="true" iconCls="icon-add" onclick="upload('imgpath')" >上传</a>
		  <a class="mini-button" plain="true" iconCls="icon-add" onclick="showImg('imgpath')" >查看</a>
		  </td>
        </tr>
        <tr>
          <td>链接：</td>
          <td><input name="link" class="mini-textbox" width="437" value="${advert.link}" /></td>
        </tr>
        <tr>
          <td>标识：</td>
          <td><input name="keyword" id="keyword" class="mini-textbox" width="437" required="true" value="${advert.keyword}" />
		  <input class="mini-combobox" data="keywordData" onitemclick="onItemClick" /></td>
        </tr>
        <tr>
          <td>说明：</td>
          <td><input name="remark" id="remark" class="mini-textbox" width="437" value="${advert.remark}" /></td>
        </tr>
        </tr>
        <tr>
          <td>排序：</td>
          <td><input name="displayorder" class="mini-spinner" vtype="int" value="${advert.displayorder}" /></td>
        </tr>
        <tr>
		  <td>设置：</td>
		  <td>
			<input class="mini-checkbox" trueValue="1" falseValue="0" name="inuse" id="inuse" text="可用" value="${advert.inuse}" checked="${advert.inuse=='1'}" >
		  </td>
        </tr>
        <tr>
          <td>内容：</td>
          <td><textarea id="editor" name="content" style="width:100%;height:410px;">${advert.content}</textarea></td>
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
	var keywordData = [
		{value:'INDEX_MODULE',text:'首页自定义模块'},
		{value:'INDEX_SLIDE',text:'首页滑动图片'},
		{value:'',text:'其他'}
	];
	function onItemClick(e) {
		mini.get("keyword").setValue(e.item.value);
		mini.get("remark").setValue(e.item.text);
	}

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
