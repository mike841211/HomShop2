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

		// 商品图片上传
		K('#btnUpload').click(function() {
			editor.loadPlugin('image', function() {
				editor.plugin.imageDialog({
					clickFn : function(url, title, width, height, border, align) {
						grid2.addRow({"url":url});
						editor.hideDialog();
					}
				});
			});
		});
		K('#btnUploadBatch').click(function() {
			editor.loadPlugin('multiimage', function() {
				editor.plugin.multiImageDialog({
					clickFn : function(urlList) {
						K.each(urlList, function(i, data) {
							grid2.addRow({"url":data.url});
						});
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
<form id="form1" onsubmit="return false" style="height:100%;">
<div class="mini-fit">
	<div class="mini-toolbar" style="border-bottom:0;">
		<a class="mini-button" plain="true" iconCls="icon-add" id="btnUpload">上传图片</a>
		<a class="mini-button" plain="true" iconCls="icon-add" id="btnUploadBatch">批量上传</a>
		<span class="separator"></span>
		<a class="mini-button" plain="true" iconCls="icon-upload" onclick="upItem()">上移</a>
		<a class="mini-button" plain="true" iconCls="icon-download" onclick="downItem()">下移</a>
		<a class="mini-button" plain="true" iconCls="icon-remove" onclick="removeItem()">删除</a>
	</div>
	<div class="mini-fit">
		<div class="mini-splitter" style="width:100%;height:100%;">
			<div size="320">
				<div id="grid2" class="mini-datagrid" style="width:100%;height:100%;" fitColumns="true" borderStyle="border:0"
					showPager="false" onrowclick="onRowClick" emptyText="" showEmptyText="false">
					<div property="columns">
						<div type="indexcolumn" width="30"></div>
						<div field="url" width="100%">URL</div>
					</div>
				</div>
			</div>
			<div id="viewPanel" style="padding:10px;overflow:auto;">
			</div>
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
    var grid2 = mini.get("grid2");

	function saveData() {
		miniExt.win.callback(grid2.getData());
		closeWindow();
	}

	function closeWindow(action) {
		miniExt.win.close(action);
	}

	function onRowClick(e) {
        $('#viewPanel').html('<img src="'+e.record.url+'" alt="" />');
	}
    function upItem() {
        var items = grid2.getSelecteds();
        for (var i = 0, l = items.length; i < l; i++) {
            var item = items[i];
            var index = grid2.indexOf(item);
            grid2.moveRow(item, index-1);
        }
    }
    function downItem() {
        var items = grid2.getSelecteds();
        for (var i = 0, l = items.length; i < l; i++) {
            var item = items[i];
            var index = grid2.indexOf(item);
            grid2.moveRow(item, index + 2);
        }
    }
    function removeItem() {
        var items = grid2.getSelecteds();
        grid2.removeRows(items);
    }

</script>
</body>
</html>
