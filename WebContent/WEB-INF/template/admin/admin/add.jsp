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
    <input name="id" class="mini-hidden" />
    <div style="padding-left:11px;padding-bottom:5px;">
      <table style="table-layout:fixed;">
        <tr>
          <td style="width:80px;">用户名：</td>
          <td><input name="username" class="mini-textbox" required="true" vtype="rangeLength:4,20" errorMode="none" requiredErrorText="用户名不能为空" rangeLengthErrorText="帐号要求4-20个字符" onvalidation="miniExt.showError"/><label><input type="checkbox" id="continue">连续添加</label></td>
        </tr>
        <tr>
          <td>密码：</td>
          <td><input name="password" class="mini-password" required="true" errorMode="none" requiredErrorText="密码不能为空" onvalidation="miniExt.showError"/></td>
        </tr>
        <tr>
          <td>重复密码：</td>
          <td><input name="repassword" class="mini-password" required="true" errorMode="none" requiredErrorText="请重复密码" onvalidation="miniExt.showError"/></td>
        </tr>
        <tr>
          <td>Email：</td>
          <td><input name="email" class="mini-textbox" vtype="email" emailErrorText="Email格式不正确"/></td>
        </tr>
        <tr>
          <td>姓名：</td>
          <td><input name="name" id="name" class="mini-textbox" /></td>
        </tr>
        <tr>
          <td>部门：</td>
          <td><input name="department" class="mini-textbox" /></td>
        </tr>
        <tr>
          <td>设置：</td>
          <td><input type="checkbox" name="lock" class="mini-checkbox" trueValue="1" falseValue="0" text="禁用"></td>
        </tr>
      </table>
    </div>
    <fieldset style="border:solid 1px #aaa;padding:0 8px 5px;">
		<legend>管理角色</legend>
		<div name="selectroles" class="mini-checkboxlist" textField="text" valueField="value"
		   value="" url="../role/getRolesForSelect.do" >
		</div>
    </fieldset>
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
		if (mini.getbyName("password").value != mini.getbyName("repassword").value) {alert("两次密码不一致");return;}
		if (mini.getbyName("selectroles").value == "") {alert("请选择角色");return;}

		//var data = form.getData();      //获取表单多个控件的数据
		$.ajax({
			url: "save.do",
			type: "POST",
			dataType: "json",
			data: form.getData(),
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					top.miniExt.showMsg("保存成功");
					//form.setChanged(false);
					if($("#continue").is(":checked")){
						form.reset();
						//miniExt.form.reset(form);
					}else{
						miniExt.win.callback();
						closeWindow();
					}
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