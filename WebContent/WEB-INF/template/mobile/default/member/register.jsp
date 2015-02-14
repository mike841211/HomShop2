<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>会员注册 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${CacheUtil.getConfig('SHOP_METAKEYWORDS')}">
<meta http-equiv="description" content="${CacheUtil.getConfig('SHOP_METADESCRIPTION')}">
<script type="text/javascript" src="${base}/resources/mobile/default/js/shop.js"></script>
<style type="text/css">
	.section-login {
		min-height:400px;
		background:#fafafa;
		padding:20px 10px;
	}
	.new-input-span {
		display:block;
		height:30px;
		border:1px solid #ccc;
	}
	.new-input-span {
		height:40px;
		border-top:1px solid #9f9f9f;
		background:#fff;
	}
	.mg-b15 {
		margin-bottom:15px;
	}
	.new-input {
		width:100%;
		height:40px;
		border:0;
		border-radius:0;
		background:#fff;
		font-size:12px;
		line-height:24px;
		font-weight:normal;
		color:#bdbdbd;
		text-indent:10px;
		vertical-align:top;
		-webkit-appearance:none;
	}
	.new-abtn-type,.new-abtn-type2,.new-abtn-type3,.new-abtn-type4 {
		display:block;
		padding:8px;
		border-radius:2px;
		background-color:#c00;
		font-size:14px;
		color:#fff;
		text-align:center;
	}
	.login-register {
		padding:10px;
	}
	.new-tbl-type {
		display:table;
		width:100%;
	}
	.new-tbl-cell {
		display:table-cell;
	}
	.new-set-info .new-tbl-cell {
		vertical-align:top;
	}
	.login-register .new-tbl-cell {
		padding:3px 0;
	}
	.login-register .text-right {
		text-align:right;
	}
	.login-register a {
		font-size:14px;
		color:#333;
		text-decoration:underline;
	}
	.jcaptcha-img-span{
		position:absolute;
		top:6px;
		right:6px;
	}
	.relative{
		position:relative;
	}

	.moreinfo-a {
		color:#666;
		padding-left:5px;
		display:block;
	}
</style>
<script type="text/javascript">
$(function($){
	var ip = "<%= request.getRemoteAddr() %>";
	$.getScript("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip="+ip,function(){
		if(remote_ip_info.ret!=1){return;}
		$("#registIparea").val(remote_ip_info.province + remote_ip_info.city + remote_ip_info.district + remote_ip_info.isp);
	});

	$('#submit').click(function save() {
		var strErr=[];

		var username = $.trim($('#username').val());
		if(username.length<4) {strErr.push("请正确填写用户名！\n");}
		var password = $.trim($('#password').val());
		var repassword = $.trim($('#repassword').val());
		if(password=='') {strErr.push("请填写密码！\n");}
		else if(password!=repassword) {strErr.push("两次密码输入不一致！\n");}

		var mobile = $.trim($('#mobile').val());
		if(mobile=='') {strErr.push("请填写手机号码！\n");}
		if (mobile!=''){
			if(!Util.isMobile(mobile)){strErr.push("手机号码格式不正确！\n");}
		}

		var email = $.trim($('#email').val());
		if (email!=''){
			if(!Util.isEmail(email)) {strErr.push("电子邮箱格式不正确！\n");}
		}

		var captcha = $.trim($('#jcaptcha').val());
		if(captcha.length<4) {strErr.push("请正确填写验证码！\n");}

		if(strErr.length>0) {alert(strErr.join(''));return false;}

		var data = $('#frm_login').serialize();
		$.ajax({
			url: "${base}/member/register.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					location.href = $('#returnurl').val();
				}else{
					alert(json.message);
					var img = document.getElementById('jcaptcha-img');
					img.src = img.src;
					var jcaptcha = document.getElementById('jcaptcha');
					jcaptcha.value='';
					jcaptcha.focus();
				}
			}
		});
	});
	$('.moreinfo-a').click(function() {
		$('#moreinfo-c').toggle();
	});

});
</script>
</head>

<body>
<c:set var="header_title" value="会员注册" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="section-login">
    <div class="new-pay-pw">
        <div class="new-set-info">
            <form id="frm_login">
                <input type="hidden" id="returnurl" value="${returnurl}">
                <input type="hidden" id="registIparea" name="registIparea" value="">
                <div class="new-txt-err" id="err" style="display: none;"></div>
                <span class="new-input-span mg-b15">
                    <input type="text" class="new-input" name="username" id="username" value="" placeholder="请输入用户名，4-20个数字或字符">
                </span>
                <span class="new-input-span mg-b15">
                    <input type="password" value="" class="new-input" name="password" id="password" placeholder="请输入密码">
                </span>
                <span class="new-input-span mg-b15">
                    <input type="password" value="" class="new-input" name="repassword" id="repassword" placeholder="请再次输入密码确认">
                </span>
                <span class="new-input-span mg-b15">
                    <input type="text" class="new-input" name="mobile" id="mobile" value="" placeholder="您的手机号码">
                </span>
                <div class="mg-b15"><a href="javascript:void(0);" class="moreinfo-a">>>更多可选信息...</a></div>
                <div id="moreinfo-c" style="display:none;">
                <span class="new-input-span mg-b15">
                    <input type="text" class="new-input" name="name" id="name" value="" placeholder="您的真实姓名，建议填写">
                </span>
                <span class="new-input-span mg-b15">
                    <input type="email" class="new-input" name="email" id="email" value="" placeholder="您的电子邮箱，建议填写">
                </span>
                </div>
                <span class="new-input-span mg-b15 relative">
                    <input type="text" value="" class="new-input" name="jcaptcha" id="jcaptcha" placeholder="请输验证码：点击图片刷新" style="text-transform: uppercase;">
                    <span class="jcaptcha-img-span"><img id="jcaptcha-img" src="${base}/resources/common/images/jcaptcha.gif" style="cursor:pointer;" onclick="this.src='${base}/jcaptcha.jpeg?'+Math.random()" alt="验证码" title="点击换一张" /></span>
                </span>
                <a href="javascript:void(0);" id="submit" class="new-abtn-type new-mg-t15">注册</a>
            </form>
        </div>
    </div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>