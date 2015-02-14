<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>会员信息 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<script type="text/javascript" src="${base}/resources/common/js/ChinaArea.js"></script>
<script type="text/javascript">
$(function($){

	ChinaArea.init({base:"${base}",code:"${member.areaCode}"});

	$('#submit').click(function() {
		if ($('#password').val().trim()==''){
			$('#J_tips').html('请输入当前密码').show().delay(2000).fadeOut(1000);
			return;
		}
		var province = $("#province").find("option:selected").text();
		var city = $("#city").find("option:selected").text();
		var district = $("#district").find("option:selected").text();
		$('#province_val').val(province);
		$('#city_val').val(city);
		$('#district_val').val(district);

		$.post(
			'${base}/member/info/update.do',
			$('#form1').serialize(),
			function(json) {
				if (json.status=="success"){
					$('#J_tips').html('保存成功').show().delay(2000).fadeOut(1000);
				}else{
					$('#J_tips').html(json.message).show().delay(2000).fadeOut(1000);
				}
			},
			"json"
		);
	});
});
</script>
<style type="text/css">
	.input1,.select1{width:100%;border: 1px solid #efefef;padding:2px;}
	.select1{background:#fff;margin-bottom:1px;}
	section{padding:10px;background:#fff;border-bottom:1px solid #cfcfcf;margin-bottom:0px;}
	.tbl-cell{padding:2px;}
	.cell1{width:80px;}
	.cell2{text-align:right;}
	.cell3{text-align:right;width:100px;}
	.cell4{text-align:right;padding-right:20px;}
	.details .tbl{border-bottom: 1px solid gray;padding-bottom: 3px;}
	.item-name{padding-top: 3px;}
	.caption{font-size:1.2em;font-weight:bold; padding-bottom:5px;}
	.new-abtn-type{display:block;padding:8px;border-radius:5px;background-color:#c00;font-size:14px;color:#fff;text-align:center;width:90%;margin:0px auto;}
</style>
</head>

<body>
<c:set var="header_title" value="会员信息" scope="request" />
<jsp:include page="../inc.header.jsp" />
<form id="form1">
<section>
	<div class="tbl">
		<div class="tbl-cell cell1">姓名：</div>
		<div class="tbl-cell"><input type="text" class="input1" name="name" id="name" value="${member.name}"/></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">手　机：</div>
		<div class="tbl-cell"><input type="number" class="input1" name="mobile" id="mobile" value="${member.mobile}"/></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">固定电话：</div>
		<div class="tbl-cell"><input type="number" class="input1" name="phone" id="phone" value="${member.phone}"/></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">EMAIL：</div>
		<div class="tbl-cell"><input type="email" class="input1" name="email" id="email" value="${member.email}"/></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">地　区：</div>
		<div class="tbl-cell"><input type="hidden" name="areaCode" id="areaCode" value="" />
		<select id="province" class="select1"></select><br>
		<select id="city" class="select1"></select><br>
		<select id="district" class="select1"></select>
		<input type="hidden" name="province" id="province_val" value="${member.province}" />
		<input type="hidden" name="city" id="city_val" value="${member.city}" />
		<input type="hidden" name="district" id="district_val" value="${member.district}" />
		</div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">地　址：</div>
		<div class="tbl-cell"><input type="text" class="input1" name="address" id="address" value="${member.address}"/></div>
	</div>
	<div class="tbl">
		<div class="tbl-cell cell1">当前密码：</div>
		<div class="tbl-cell"><input type="password" class="input1" name="password" id="password" value=""/></div>
	</div>
</section>
<section>
	<a href="javascript:void(0)" id="submit" class="new-abtn-type">保存</a>
</section>
</form>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>
