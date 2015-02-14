<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
</head>
<body>
<div class="mini-fit" style="padding:10px;">
<form id="form1" onsubmit="return false" style="height:100%;">
<div class="mini-fit">
	<input class="mini-hidden" name="id" id="id" value="${member.id}" />
	<div id="tabs1" class="mini-tabs" activeIndex="0" style="height:100%;">
		<div name="first" title="基本信息">
		  <table style="table-layout:fixed;">
		    <tr>
		      <td style="width:80px;">登录帐号：</td>
			  <td style="width:150px;">${member.username}</td>
		      <td style="width:80px;">新密码：</td>
		      <td><input name="password" class="mini-password" value="" /></td>
		    </tr>
		    <tr>
		      <td>会员等级：</td>
			  <td><input class="mini-combobox" textField="name" valueField="id" url="../member_grade/getAllForSelect.do" name="tbShopMemberGrade.id" value="${member.tbShopMemberGrade.id}" /></td>
		      <td></td>
			  <td></td>
		    </tr>
		    <tr>
		      <td>姓名：</td>
		      <td><input name="name" id="name" class="mini-textbox" value="${member.name}" /></td>
		      <td>设置：</td>
			  <td><input class="mini-checkbox" trueValue="1" falseValue="0" name="enabled" text="启用" value="${member.enabled}" checked="${member.enabled=='1'}" ></td>
		    </tr>
		    <tr>
		      <td>手机：</td>
		      <td><input name="mobile" class="mini-textbox" value="${member.mobile}" /></td>
		      <td>电话：</td>
		      <td><input name="phone" class="mini-textbox" value="${member.phone}" /></td>
		    </tr>
		    <tr>
		      <td>EMAIL：</td>
		      <td><input name="email" class="mini-textbox" value="${member.email}" vtype="email" /></td>
		      <td>传真：</td>
		      <td><input name="fax" class="mini-textbox" value="${member.fax}" /></td>
		    </tr>
		    <tr>
		      <td>QQ：</td>
		      <td><input name="qq" class="mini-textbox" value="${member.qq}" vtype="int;rangeLength:5,10" /></td>
		      <td>旺旺：</td>
		      <td><input name="wangwang" class="mini-textbox" value="${member.wangwang}" /></td>
		    </tr>
		    <tr>
		      <td>出生日期：</td>
		      <td><input name="birthday" class="mini-datepicker" value="${member.birthday}" /></td>
		      <td>性别：</td>
		      <td><div name="gender" class="mini-radiobuttonlist" repeatItems="2" textField="text" valueField="id" value="${member.gender}" data="[{id:'M',text:'男'},{id:'F',text:'女'}]" ></div></td>
		    </tr>
			<tr>
			  <td>省市区：</td>
			  <td colspan="3">
			  <input class="mini-combobox" textField="name" valueField="name" onvaluechanged="onProvinceChanged" url="${base}/china/provinces.do" id="province" name="province" value="${member.province}" />
			  <input class="mini-combobox" textField="name" valueField="name" onvaluechanged="onCityChanged" id="city" name="city" value="${member.city}" />
			  <input class="mini-combobox" textField="name" valueField="name" onvaluechanged="onDistrictChanged" id="district" name="district" value="${member.district}" />
			  <input class="mini-hidden" name="areaCode" id="areaCode" value="${member.areaCode}" /></td>
			</tr>
			<tr>
			  <td>联系地址：</td>
			  <td colspan="3"><input name="address" class="mini-textbox" style="width:383px;" value="${member.address}" /></td>
			</tr>
		    <tr>
		      <td>注册时间：</td>
			  <td>${member.registDate}</td>
		      <td>最后登录：</td>
			  <td>${member.loginDate}</td>
		    </tr>
		    <tr>
		      <td>注册IP：</td>
			  <td colspan="3">${member.registIp}  ${member.registIparea}</td>
		    </tr>
		  </table>
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

	var form = new mini.Form("#form1");

	function saveData() {
		form.validate();
		if (form.isValid() == false) {
			//var tabs = mini.get("tabs1");
			////console.log(tabs);
			//tabs.activeTab(tabs.getTab(0));
			return;
		}

		//editor.sync();
		// 保存调整后的图片
		var data = form.getData(true,false);

		$.ajax({
			url: "update.do",
			type: "POST",
			dataType: "json",
			data: data,
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					miniExt.win.callback();
					closeWindow();
				}else{
					alert(json.message);
				}
			}
		});
	}

	function closeWindow(action) {
		miniExt.win.close(action);
	}

	// [[ 省市区联动 ]]
	var province = mini.get("province");
	var city = mini.get("city");
	var district = mini.get("district");
	var areaCode = mini.get("areaCode");
	function onProvinceChanged(e) {
		city.setValue("");
		district.setValue("");
		var code = e.selected.code;
		areaCode.setValue(code);
		var url = "${base}/china/cities/"+ code +".do";
		city.setUrl(url);
		//city.select(0);
		district.setData([]);
	}
	function onCityChanged(e) {
		district.setValue("");
		var code = e.selected.code;
		areaCode.setValue(code);
		var url = "${base}/china/districts/"+ code +".do";
		district.setUrl(url);
		//district.select(0);
	}
	function onDistrictChanged(e) {
		var code = e.selected.code;
		areaCode.setValue(code);
	}
	<c:if test="${not empty member.areaCode}">
	city.setUrl("${base}/china/cities/${member.areaCode}.do");
	if("${member.areaCode}".substr(2,2)>"00"){
	district.setUrl("${base}/china/districts/${member.areaCode}.do");
	}
	</c:if>
	// ]] 省市区联动 ]]
</script>
</body>
</html>
