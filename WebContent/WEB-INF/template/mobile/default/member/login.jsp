<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>会员登入 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
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
	.dl1 {margin:0px 0 10px;}

.new-a-txt3 {
display: block;
font-size: 14px;
line-height: 20px;
color: #666;
padding-bottom: 5px;
margin-top: -5px;
}
.new-a-txt3 a {display: inline-block;padding-bottom: 5px;}
.new-chk2 {
display: inline-block;
width: 22px;
height: 22px;
margin-right: 5px;
background: url(${base}/resources/mobile/default/css/icon4.png) -78px -51px no-repeat;
background-size: 100px 100px;
vertical-align: middle;
}
.new-chk2.on {
background-position: -54px -51px;
}
</style>
<script type="text/javascript">
$(function($){
	$('#submit').click(function(){
		var strErr=[];

		var account = $.trim($('#J_account').val());
		if(account.length<4) {strErr.push($('#J_account').attr('placeholder')+"！\n");}
		var password = $.trim($('#password').val());
		if(password=='') {strErr.push("请填写密码！\n");}
		var captcha = $.trim($('#jcaptcha').val());
		if(captcha.length<4) {strErr.push("请正确填写验证码！\n");}

		if(strErr.length>0) {alert(strErr.join(''));return false;}

		var data = $('#frm_login').serialize();
		$.ajax({
			url: "${base}/member/login.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					var returnurl = $('#returnurl').val();
					Shop.loginCallbackRedirect(returnurl,"${base}");
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

	$('.loginType a').click(function(){
		var $this = $(this);
		if ($this.has('.on').size()==0){
			var type = $this.data('type');
			$("#loginType").val(type);
			if (type=='username'){
				$("#J_account").attr({'name':type,'placeholder':'请输入会员账号'});
			} else if (type=='cardno'){
				$("#J_account").attr({'name':type,'placeholder':'请输入会员卡号'});
			} else if (type=='email'){
				$("#J_account").attr({'name':type,'placeholder':'请输入电子邮箱'});
			} else if (type=='mobile'){
				$("#J_account").attr({'name':type,'placeholder':'请输入手机号'});
			}
			$this.siblings().find('span').removeClass('on');
			$this.find('span').addClass('on');
		}
	});
});
</script>
</head>

<body>
<c:set var="header_title" value="会员登入" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="section-login">
    <div class="new-pay-pw">
        <div class="new-set-info">
            <form id="frm_login">
                <input type="hidden" id="loginType" name="loginType" value="username">
                <input type="hidden" id="returnurl" value="${returnurl}">
                <div class="new-txt-err" id="err" style="display: none;"></div>
                <span class="new-input-span mg-b15">
                    <input type="text" class="new-input" name="username" id="J_account" value="" placeholder="请输入用户名">
                </span>
                <c:if test="${CacheUtil.getConfig('MEMBER_LOGIN_CARDNO')=='1' || CacheUtil.getConfig('MEMBER_LOGIN_EMAIL')=='1' || CacheUtil.getConfig('MEMBER_LOGIN_MOBILE')=='1'}">
                <div class="new-a-txt3 loginType">
                    <a data-type="username"><span class="new-chk2 on"></span>会员账号登入</a>
                    <c:if test="${CacheUtil.getConfig('MEMBER_LOGIN_CARDNO')=='1'}">
                    <a data-type="cardno"><span class="new-chk2"></span>会员卡号登入</a>
                    </c:if>
                    <c:if test="${CacheUtil.getConfig('MEMBER_LOGIN_EMAIL')=='1'}">
                    <a data-type="email"><span class="new-chk2"></span>电子邮箱登入</a>
                    </c:if>
                    <c:if test="${CacheUtil.getConfig('MEMBER_LOGIN_MOBILE')=='1'}">
                    <a data-type="mobile"><span class="new-chk2"></span>手机号登入</a>
                    </c:if>
                </div>
                </c:if>
                <span class="new-input-span mg-b15">
                    <input type="password" value="" class="new-input" name="password" id="password" placeholder="请输入密码">
                </span>
                <span class="new-input-span mg-b15 relative">
                    <input type="text" value="" class="new-input" name="jcaptcha" id="jcaptcha" placeholder="请输验证码：点击图片刷新" style="text-transform: uppercase;">
                    <span class="jcaptcha-img-span"><img id="jcaptcha-img" src="${base}/resources/common/images/jcaptcha.gif" style="cursor:pointer;" onclick="this.src='${base}/jcaptcha.jpeg?'+Math.random()" alt="验证码" title="点击换一张" /></span>
                </span>
                <a href="javascript:void(0);" id="submit" class="new-abtn-type new-mg-t15">登录</a>
            </form>
            <div class="login-register">
                <div class="new-tbl-type">
                    <span class="new-tbl-cell">
                        <a rel="nofollow" href="${base}/member/register.htm" class="new-fl">免费注册</a>
                    </span>
                    <span class="new-tbl-cell text-right">
                        <a href="${base}/member/password/forget.htm" class="new-fr">找回密码</a>
                    </span>
                </div>
            </div>
			<c:if test="${ElUtil.isWeixinBrowser(pageContext.request)}">
			<div class="other-login dl1">
				<dl>
					<dt>其他方式登录</dt>
					<c:set var="redirect_uri" value="${CacheUtil.getConfig('SHOP_URL')}/member/login.htm"/>
					<dd class="icon icon-wx"><a href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=${CacheUtil.getConfig('WEIXIN_APPID')}&redirect_uri=${ElUtil.encodeURL(redirect_uri)}&response_type=code&scope=snsapi_base&state=weixin#wechat_redirect">使用微信账号登入</a></dd>
				</dl>
			</div>
			</c:if>
        </div>
    </div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>