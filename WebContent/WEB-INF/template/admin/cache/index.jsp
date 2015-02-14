<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script src="${base}/resources/admin/js/cacheNames.js" type="text/javascript"></script>
</head>
<body>
<div class="mini-fit">
  <table style="table-layout:fixed;">
	<tr>
	  <td>
		<div id="combo1" class="mini-combobox" style="width:150px;" popupWidth="500px" textField="text" valueField="value" onvaluechanged="valuechanged" data="cacheNames">
			<div property="columns">
				<div field="value"></div>
				<div field="text"></div>
				<div field="remark"></div>
			</div>
		</div>
		<input class="mini-textbox" name="cacheName" value="" style="width:150px;" /> <a class="mini-button" onclick="clear()">更新指定缓存</a> <a class="mini-button" onclick="clearAll()">清空所有缓存对象</a></td>
	</tr>
  </table>
</div>

<script type="text/javascript">
	mini.parse();

	function valuechanged(e) {
		mini.getbyName('cacheName').setValue(e.value);
	}

	function clear(cacheName) {
		if (cacheName==undefined){
			cacheName = mini.getbyName('cacheName').getValue();
		}
		if (cacheName==""){return;}

		$.post(
			"clear.do",
			{'cacheName':cacheName},
			function (json) {
				if (json.status=="success"){
					miniExt.showMsg("更新成功");
				}else{
					alert(json.message);
				}
			},"json"
		);
	}

	function clearAll() {
		$.post(
			"clearAll.do",
			function (json) {
				if (json.status=="success"){
					miniExt.showMsg("更新成功");
				}else{
					alert(json.message);
				}
			},"json"
		);
	}
</script>
</body>
</html>
