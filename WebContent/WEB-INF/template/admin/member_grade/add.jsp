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
          <td style="width:80px;">名称：</td>
          <td><input name="name" class="mini-textbox" required="true" /><label><input type="checkbox" id="continue">连续添加</label></td>
        </tr>
        <tr>
          <td>级别：</td>
          <td><input name="lever" class="mini-spinner" minValue="0" required="true" /></td>
        </tr>
        <tr>
          <td>积分要求：</td>
          <td><input name="score" class="mini-spinner" minValue="0" maxValue="9999999999999" increment="1000" required="true" /></td>
        </tr>
        <tr>
          <td>优惠比例：</td>
          <td><input name="discount" class="mini-spinner" minValue="0" maxValue="1" decimalPlaces="2" increment="0.1" value="1" required="true" /></td>
        </tr>
        <tr>
          <td>备注：</td>
          <td><input name="remark" class="mini-textbox" /></td>
        </tr>
        <tr>
          <td></td>
          <td><input type="checkbox" name="isSpecial" class="mini-checkbox" trueValue="1" falseValue="0" text="特殊等级"> (注：特殊等级不随积分变化)</td>
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

		//var data = form.getData();      //获取表单多个控件的数据
		form.loading("正在保存，请稍后......");
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
			},
			complete: function (json) {
				form.unmask();
			}
		});
	}

	function closeWindow(action) {
		miniExt.win.close(action);
	}
</script>
</body>
</html>
