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
  <form id="form1" onsubmit="return false">
    <input name="id" class="mini-hidden" value="${role.id}" />
    <input name="createDate" class="mini-hidden" value="${role.createDate}" />
    <input name="authority" class="mini-hidden" value="${role.authority}" />
    <div style="padding-left:11px;padding-bottom:5px;">
      <table style="table-layout:fixed;">
        <tr>
          <td style="width:70px;">角色名称：</td>
          <td><input name="name" class="mini-textbox" required="true" value="${role.name}" requiredErrorText="名称不能为空" errorMode="none" onvalidation="miniExt.showError"/></td>
        </tr>
        <tr>
          <td>描述：</td>
          <td><input name="description" class="mini-textarea" style="width:590px;" value="${role.description}" /></td>
        </tr>
      </table>
	</div>
    <c:forEach items="${authority}" var="item" varStatus="varStatus">
    <fieldset style="border:solid 1px #aaa;padding:0 8px 5px;">
		<legend>${item.key}</legend>
		<div class="mini-checkboxlist" textField="text" valueField="value"
		   data='${item.value}' name="" id="authority${varStatus.index}" value="${role.authority}" >
		</div>
    </fieldset>
    </c:forEach>
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
		if (form.isValid() == false) {return;}

		var authority = [];
		$('.mini-checkboxlist input[type=checkbox]:checked').each(function(i,item){
			authority.push(item.value);
		});
		$('[name=authority]').val(authority.join(','));

		//var data = form.getData();      //获取表单多个控件的数据
		$.ajax({
			url: "update.do",
			type: "POST",
			dataType: "json",
			data: $("form").serialize(),
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
</script>
</body>
</html>
