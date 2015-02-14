<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script charset="utf-8" src="${base}/resources/common/editor/kindeditor-min.js"></script>
<script charset="utf-8" src="${base}/resources/common/editor/lang/zh_CN.js"></script>
<link rel="stylesheet" href="${base}/resources/common/editor/themes/default/default.css" />
<script type="text/javascript">
	var editor;
	KindEditor.ready(function(K) {
		editor = KindEditor.editor({
			uploadJson : '${KindEditor_uploadJson}',
			fileManagerJson : '${KindEditor_fileManagerJson}',
			allowFileManager : true
		});

		// 图片上传
		K('#btnUpload').click(function() {
			editor.loadPlugin('image', function() {
				editor.plugin.imageDialog({
					tabIndex : 1,
					clickFn : function(url, title, width, height, border, align) {
						miniExt.win.callback(url);
						miniExt.win.close();
					}
				});
			});
		}).click();
	});
</script>
<style type="text/css">
.mini-button {display:block;width:300px;height:50px;margin:30% auto 0;}
.mini-button-text {line-height: 42px;font-size: 28px;}
</style>
</head>
<body>
<div class="mini-fit">
	<a class="mini-button" id="btnUpload">上传图片</a>
</div>
<script type="text/javascript">
	mini.parse();
</script>
</body>
</html>
