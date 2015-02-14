<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="inc.header.jsp" />
<style type="text/css">
div.login {
width: 520px;
height: 302px;
padding: 110px 160px 0px 160px;
margin: 0px auto 0px auto;
overflow: hidden;
background: url(${base}/resources/admin/images/login.png) 0px 0px no-repeat;
position:relative;
}
div.login .powered {
height: 30px;
line-height: 30px;
color: #999;
text-align: center;
}
form {/* width:500px; */height:245px;margin:0 auto;}
.m-form{line-height:29px;color:#555;}
.m-form .formitm{padding:5px 0 0;line-height:30px;}
.m-form .lab{float:left;width:190px;margin-right:-90px;text-align:right;font-weight:bold;}
.m-form .ipt{margin-left:200px;}
.m-form .ipt *{vertical-align:middle;}
.m-form .ipt img{height:32px;margin:0 15px 0 5px;}
.u-ipt{width:180px;padding:5px;height:17px;border:1px solid #D9D9D9;border-top-color:#c0c0c0;line-height:17px;font-size:14px;color:#555;background:#fff;}
.u-ipt-78{width:78px;}
.u-btn{display:inline-block;*display:inline;*zoom:1;*overflow:visible;-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;padding:0 12px;height:28px;line-height:28px;border:1px solid #2d88bf;font-size:12px;letter-spacing:1px;word-spacing:normal;text-align:center;vertical-align:middle;cursor:pointer;background:#54aede;}
button.u-btn{*height:30px;_line-height:25px;}
.u-btn,.u-btn:hover{color:#fff;text-decoration:none;}
.u-btn:hover,.u-btn:focus{background:#399dd8;}

.m-form .lab2 {width:90px;}
.m-form .ipt2 {margin-left: 100px;}
#jcaptcha {text-transform: uppercase;}
</style>
</head>
<body>
<div class="m-form login">
    <form id="form1" onsubmit="return false">
		<div class="formitm">
			<label class="lab">账　号：</label>
			<div class="ipt">
				<input type="text" class="u-ipt" name="username" id="username" />
			</div>
		</div>
		<div class="formitm">
			<label class="lab">密　码：</label>
			<div class="ipt">
				<input type="password" class="u-ipt" name="password" id="password" />
			</div>
		</div>
		<div class="formitm">
			<label class="lab">验证码：</label>
			<div class="ipt">
				<input type="text" class="u-ipt u-ipt-78" name="jcaptcha" id="jcaptcha"/> <img id="jcaptcha-img" src="${base}/jcaptcha.jpeg" style="cursor:pointer;" onclick="this.src='${base}/jcaptcha.jpeg?'+Math.random()" alt="验证码" title="点击换一张" />
			</div>
		</div>
		<div class="formitm">
			<div class="ipt">
				<button class="u-btn" type="button" onclick="login()">登入</button>
			</div>
		</div>
    </form>
	<div class="powered">COPYRIGHT © 2014 ALL RIGHTS RESERVED.</div>
</div>

<script type="text/javascript">
	function login() {
		if ($('#username').val()==''){
			alert("账号不能为空");
			return;
		}
		if ($('#password').val()==''){
			alert("密码不能为空");
			return;
		}
		if ($('#jcaptcha').val()==''){
			alert("验证码不能为空");
			return;
		}

		$.post(
			"${base}/admin/login.do",
			$("#form1").serialize(),
			function (json) {
				if (json.status=="success"){
					var pathname = top.location.pathname.toLowerCase();
					if (pathname.indexOf("/logout.do")==-1 && pathname.indexOf("/login.do")==-1){
						top.location.reload();
					}else{
						top.location.href = "${base}/admin/index.htm";
					}
				}else{
					var jcaptchaImg = document.getElementById('jcaptcha-img');
					jcaptchaImg.src = jcaptchaImg.src;
					var jcaptcha = document.getElementById('jcaptcha');
					jcaptcha.value="";
					jcaptcha.focus();
					alert(json.message);
				}
			},"json"
		);
	}
	$("#jcaptcha").on("keyup",function(e){
		if (e.keyCode==13){
			login();
		}
	});
</script>
</body>
</html>
