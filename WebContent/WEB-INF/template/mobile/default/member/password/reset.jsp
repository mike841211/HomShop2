<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>修改密码 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../../inc.required.jsp" />
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
</style>
<script type="text/javascript">
$(function($){
	$('#submit').click(function(){
		var strErr=[];

		var password = $.trim($('#password').val());
		if(password=='') {strErr.push("请填写当前密码！\n");}
		var newpassword = $.trim($('#newpassword').val());
		if(newpassword=='') {strErr.push("请填写新密码！\n");}

		if(strErr.length>0) {alert(strErr.join(''));return false;}

		var newpassword1= $.trim($('#newpassword1').val());
		if(newpassword!=newpassword1) {strErr.push("新密码确认错误！\n");}
		if(strErr.length>0) {alert(strErr.join(''));return false;}

		var data = $('#frm_login').serialize();
		$.ajax({
			url: "reset.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					$('#J_tips').html(json.message).show().delay(2000).fadeOut(1000);
				}else{
					alert(json.message);
				}
			}
		});
	});
});
</script>
</head>

<body>
<c:set var="header_title" value="修改密码" scope="request" />
<jsp:include page="../../inc.header.jsp" />
<section class="section-login">
    <div class="new-pay-pw">
        <div class="new-set-info">
            <form id="frm_login">
               <input type="hidden" id="returnurl" value="${returnurl}">
                <div class="new-txt-err" id="err" style="display: none;"></div>
                <span class="new-input-span mg-b15">
                    <input type="password" value="" class="new-input" name="password" id="password" placeholder="当前密码">
                </span>
                <span class="new-input-span mg-b15">
                    <input type="password" value="" class="new-input" name="newpassword" id="newpassword" placeholder="新密码">
                </span>
                <span class="new-input-span mg-b15">
                    <input type="password" value="" class="new-input" name="newpassword1" id="newpassword1" placeholder="再次输入新密码确认">
                </span>
                <a href="javascript:void(0);" id="submit" class="new-abtn-type new-mg-t15">修改密码</a>
            </form>
        </div>
    </div>
</section>
<jsp:include page="../../inc.footer.jsp" />
</body>
</html>