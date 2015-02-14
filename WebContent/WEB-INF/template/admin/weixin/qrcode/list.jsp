<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../../inc.header.jsp" />
<style type="text/css">
	#imgs img {margin:5px;min-width:40px;min-height:40px;background:#f5f5f5;}
</style>
</head>
<body>
<div class="mini-toolbar">
	<input class="mini-textbox" name="imgcode" id="imgcode" value="" emptyText="验证码" style="display:none;" />
	账号：<input class="mini-textbox" name="username" id="username" value="" emptyText="公众平台登入账号" />
	密码：<input class="mini-password" name="pwd" id="pwd" value="" emptyText="公众平台登入密码" />
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="mp_login()">公众平台登入</a>
	<span class="separator"></span>
	样式：<input class="mini-spinner" id="qrcodenum1" value="1" minValue="1" maxValue="20" width="50"> - <input class="mini-spinner" id="qrcodenum2" value="1" minValue="1" maxValue="20" width="50">
	大小：<input class="mini-spinner" id="pixsize" value="3" minValue="1" maxValue="10" width="50">
	<!-- 大小：<select id="pixsize" class="mini-combobox" width="50">
		<option value="1">1</option>
		<option value="2">2</option>
		<option value="3">3</option>
		<option value="4">4</option>
		<option value="5" selected>5</option>
		<option value="6">6</option>
		<option value="7">7</option>
		<option value="8">8</option>
		<option value="9">9</option>
		<option value="10">10</option>
	</select> -->
	<a class="mini-button" plain="true" iconCls="icon-download" onclick="getqrcode()">下载二维码</a> (须登入公众账号)
</div>
<div class="mini-fit">
	<div id="code"></div>
	<div id="imgs"></div>
</div>
<script type="text/javascript">
	mini.parse();

	function mp_login(){
		var username = mini.get('username').getValue();
		var pwd = mini.get('pwd').getValue();
		var imgcode = mini.get('imgcode').getValue();
		$.ajax({
			type: "POST",
			url: "mp_login.do",
			data:{username:username,pwd:pwd,imgcode:imgcode},
			dataType: "json",
			success: function (json) {
				if (json.code==-8){
					document.getElementById('imgcode').style.display="";
					document.getElementById('code').innerHTML='<img src="/admin/weixin/qrcode/getVerifyCode.do">';
					alert("请输入验证码");
				}else if (json.status=="success"){
					document.getElementById('imgcode').style.display="none";
					document.getElementById('code').innerHTML='';
					top.miniExt.showMsg("登入成功");
				}else{
					alert(json.message);
				}
			}
		});
	}
	function getqrcode(){
		var pixsize = mini.get('pixsize').getValue()*43;
		var basesrc = '${base}/admin/weixin/qrcode/getQrcode.do?pixsize='+pixsize;
		var qrcodenum1 = mini.get('qrcodenum1').getValue();
		var qrcodenum2 = mini.get('qrcodenum2').getValue();
		var imgs = [];
		for (var i=qrcodenum1; i<=qrcodenum2; i++){
			var src = basesrc+'&style='+i;
			imgs.push('<a target="_blank" class="verifyInfo" title="点击下载" href="'+src+'&action=download"><img src="'+src+'"/></a>');
		}
		document.getElementById('imgs').innerHTML = imgs.join('');
	}
</script>
</body>
</html>
