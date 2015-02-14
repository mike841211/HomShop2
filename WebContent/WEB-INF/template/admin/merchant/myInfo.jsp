<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
</head>
<body>
<div class="mini-fit" style="padding:10px;">
  <form id="form1" onsubmit="return false">
    <input name="id" class="mini-hidden" value="${admin.id}" />
    <input name="username" class="mini-hidden" value="${admin.username}" />
    <div style="padding-left:11px;padding-bottom:5px;">
      <table style="table-layout:fixed;">
        <tr>
          <td style="width:80px;">用户名：</td>
          <td>${admin.username}</td>
        </tr>
        <tr>
          <td>姓名：</td>
          <td>${admin.name}</td>
        </tr>
        <tr>
          <td>部门：</td>
          <td>${admin.department}</td>
        </tr>
        <tr>
          <td>当前密码：</td>
          <td><input name="password" class="mini-password" required="true" /></td>
        </tr>
        <tr>
          <td>新密码：</td>
          <td><input name="newPassword" class="mini-password" /></td>
        </tr>
        <tr>
          <td>确认新密码：</td>
          <td><input name="rePassword" class="mini-password" /></td>
        </tr>
        <tr>
          <td>Email：</td>
          <td><input name="email" class="mini-textbox" vtype="email" emailErrorText="Email格式不正确" value="${admin.email}"/></td>
        </tr>
      </table>
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
		if (form.isValid() == false) {return;}
		if (mini.getbyName("newPassword").value != "") {
			if (mini.getbyName("newPassword").value != mini.getbyName("rePassword").value) {alert("两次密码不一致");return;}
		}

		$.post(
			"updateMyInfo.do",
			form.getData(),
			function (json) {
				if (json.status=="success"){
					alert('保存成功');
					//closeWindow();
				}else{
					alert(json.message);
				}
			},"json"
		);
	}

	function closeWindow(action) {
		miniExt.win.close(action);
	}
</script>
</body>
</html>
