<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>会员中心 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<style type="text/css">
	body{background:#f8f8f8}
	section{min-height:400px;}
	.abtn {padding:10px 8px 8px 8px;font-size:18px;}
</style>
</head>

<body>
<c:set var="header_title" value="会员中心" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section>
	<div class="dl1">
		<dl>
			<dd><a href="${base}/member/info.htm">会员资料</a></dd>
		</dl>
	</div>
	<div class="dl1">
		<dl>
			<dd><a href="${base}/order/list.htm">我的订单</a></dd>
			<dd><a href="${base}/member/score.htm">我的积分</a></dd>
			<dd><a href="${base}/member/favorite/list.htm">我的收藏</a></dd>
		</dl>
	</div>
	<div class="dl1">
		<dl>
			<c:if test="${ElUtil.isWeixinBrowser(pageContext.request)}"><%-- 暂时只支持微信 --%>
			<dd><a href="${base}/member/bind.htm">绑定账号</a></dd>
			</c:if>
			<dd><a href="${base}/member/password/reset.htm">修改密码</a></dd>
		</dl>
	</div>
	<c:if test="${cookie['SIGNED'].value=='1'}">
	<a href="javascript:void(0);" class="abtn bc-grayd" id="J_signin">今日已签到</a>
	</c:if>
	<c:if test="${cookie['SIGNED'].value!='1'}">
	<a href="javascript:signin();" class="abtn bc-green" id="J_signin">每日签到</a>
	<script type="text/javascript">
	function signin(){
		$.ajax({
			url: base+"/member/signin.do",
			type: "POST",
			dataType: "json",
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					$('#J_tips').html("恭喜，获得 "+json.result+" 积分").show().delay(2000).fadeOut(1000);
					$('#J_signin').addClass('bc-grayd').attr('href','javascript:void(0);').html('今日已签到');
				}else if (json.status=="error"){
					$('#J_tips').html(json.message).show().delay(2000).fadeOut(1000);
				}else if (json.code=="unlogin"){
					$('#J_tips').html("登入超时").show().delay(2000).fadeOut(1000);
				}else{
					alert(json.message);
				}
			}
		});
	}
	</script>
	</c:if>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>