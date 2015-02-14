<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script type="text/javascript" src="${base}/resources/admin/js/freight_template.js"></script>
<!-- <script type="text/javascript" src="${base}/resources/admin/js/jquery.json-2.4.js"></script> -->
<style type="text/css">
	p { margin:0px; padding:0px;}
	#name { width:20em; height:18px; border: 1px solid #7F9DB9;line-height: 15px; padding:1px;}
	.input { width:4em; height:18px; text-align: right;border: 1px solid #7F9DB9;line-height: 15px; padding:1px 5px;}
	.disabled { width:4em; height:18px; text-align: right;border: 1px solid #CCCCCC;background-color: #EEE;line-height: 15px; padding:1px;}
	a {color: #36C; text-decoration:none;}
	.edit {position: absolute;bottom: 3px;right: 10px;}
	.del { color: #36C; cursor:pointer;}
	.area { padding-right:3em;}
	.allarea {text-align: left;display:none;}
	.cell-area { position: relative; padding: 3px 5px;}
	#province {position:absolute;width:400px;border:1px solid red;background-color:#FFFFFF;padding:5px;display:none;}
	 ul{margin:0px; padding:0px;}
	 li{ list-style:none;width:63px;float:left;}
</style>
</head>
<body>
<div class="mini-fit" style="padding:10px 20px 10px 10px;">
	<form id="form1">
		<div> 模板名称：<input type="text" name="name" id="name" value="${template.name}">
		</div>
		<div>
		  <div> 运送方式：<span style="color:gray;">除指定地区外，其余地区的运费采用“默认运费”</span></div>
		  <div style="margin-left:60px;">
			<!-- c:set var="exp_checked" value="${template.expFee>0 || template.expPlusfee>0 || fn:length(expDetails)>0}"/ -->
			<!-- c:set var="exp_checked" value="${not empty template.expFee || not empty template.expPlusfee || fn:length(expDetails)>0}"/ -->
			<c:set var="exp_checked" value="${not empty template.expFee || not empty template.expPlusfee || fn:length(expDetails)>0}"/>
			<c:set var="all_select" value=""/>
			<div style="margin-bottom:10px;">
			  <p><label for="exp"><input type="checkbox" name="exp" id="exp" onclick="selectLogis(this,'exp')" ${exp_checked ? 'checked' : ''}>快递${fn:length(expDetails)}</label></p>
			  <div id="div_exp" style="border:1px solid #999; padding:0px 5px; display:${exp_checked ? '' : 'none'};">
				<div style="padding:5px; margin:8px 0px 5px; border: 1px solid #9FCD75;background: #F1FDE9;">默认运费：
				  <input name="exp_num" id="exp_num" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" />
				  件内，
				  <input name="exp_fee" type="text" class="input" id="exp_fee" maxlength="6" autocomplete="off" value="${exp_checked ? template.expFee : ''}" />
				  元， 每增加
				  <input name="exp_plusnum" id="exp_plusnum" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" />
				  件， 增加运费
				  <input name="exp_plusfee" type="text" class="input" id="exp_plusfee" maxlength="6" autocomplete="off" value="${exp_checked ? template.expPlusfee : ''}" />
				  元
				</div>
				<div id="div_exp_detail" style="display:${fn:length(expDetails)>0 ? '' : 'none'};">
				  <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#CCCCCC" id="tbl_exp">
					<tr bgcolor="#EEEEEE">
					  <td align="center" valign="middle">运送到</td>
					  <td width="80" align="center" valign="middle">首件(件)</td>
					  <td width="80" align="center" valign="middle">首费(元)</td>
					  <td width="80" align="center" valign="middle">续件(件)</td>
					  <td width="80" align="center" valign="middle">续费(元)</td>
					  <td width="60" align="center" valign="middle">操作</td>
					</tr>
					<c:forEach items="${expDetails}" var="item">
					<c:set var="all_select" value="${all_select},${item.area}"/>
					<tr>
					  <td bgcolor="#FFFFFF" class="cell-area"><span class="area">${item.area}</span><span class="allarea"></span><a href="javascript:void(0);" onclick="javascript:showProvince(this)" class="edit">编辑</a>
					  <input type="hidden" name="row_select" id="row_select" value="${item.area}" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="num" id="num" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="fee" type="text" class="input" id="fee" maxlength="6" autocomplete="off" value="${exp_checked ? item.fee : ''}" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="plusnum" id="plusnum" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="plusfee" type="text" class="input" id="plusfee" maxlength="6" autocomplete="off" value="${exp_checked ? item.plusfee : ''}" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><span class="del" onclick="javascript:delRow(this)">删除</span></td>
					</tr>
					</c:forEach>
				  </table>
				</div>
				<div style="padding:5px 3px;"><span class="del" onclick="javascript:addRow(this);">为指定地区城市设置运费</span>
				  <input type="hidden" name="all_select" id="all_select" value="${fn:substringAfter(all_select,',')}" />
				</div>
			  </div>
			</div>
			<c:set var="ems_checked" value="${not empty template.emsFee || not empty template.emsPlusfee || fn:length(emsDetails)>0}"/>
			<c:set var="all_select" value=""/>
			<div style="margin-bottom:10px;">
			  <p><label for="ems"><input type="checkbox" name="ems" id="ems" onclick="selectLogis(this,'ems')" ${ems_checked ? 'checked' : ''}>EMS</label></p>
			  <div id="div_ems" style="border:1px solid #999; padding:0px 5px; display:${ems_checked ? '' : 'none'};">
				<div style="padding:5px; margin:8px 0px 5px; border: 1px solid #9FCD75;background: #F1FDE9;">默认运费：
				  <input name="ems_num" id="ems_num" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" />
				  件内，
				  <input name="ems_fee" type="text" class="input" id="ems_fee" maxlength="6" autocomplete="off" value="${ems_checked ? template.emsFee : ''}" />
				  元， 每增加
				  <input name="ems_plusnum" id="ems_plusnum" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" />
				  件， 增加运费
				  <input name="ems_plusfee" type="text" class="input" id="ems_plusfee" maxlength="6" autocomplete="off" value="${ems_checked ? template.emsPlusfee : ''}" />
				  元
				</div>
				<div id="div_ems_detail" style="display:${fn:length(emsDetails)>0 ? '' : 'none'};">
				  <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#CCCCCC" id="tbl_ems">
					<tr bgcolor="#EEEEEE">
					  <td align="center" valign="middle">运送到</td>
					  <td width="80" align="center" valign="middle">首件(件)</td>
					  <td width="80" align="center" valign="middle">首费(元)</td>
					  <td width="80" align="center" valign="middle">续件(件)</td>
					  <td width="80" align="center" valign="middle">续费(元)</td>
					  <td width="60" align="center" valign="middle">操作</td>
					</tr>
					<c:forEach items="${emsDetails}" var="item">
					<c:set var="all_select" value="${all_select},${item.area}"/>
					<tr>
					  <td bgcolor="#FFFFFF" class="cell-area"><span class="area">${item.area}</span><span class="allarea"></span><a href="javascript:void(0);" onclick="javascript:showProvince(this)" class="edit">编辑</a>
					  <input type="hidden" name="row_select" id="row_select" value="${item.area}" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="num" id="num" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="fee" type="text" class="input" id="fee" maxlength="6" autocomplete="off" value="${ems_checked ? item.fee : ''}" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="plusnum" id="plusnum" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="plusfee" type="text" class="input" id="plusfee" maxlength="6" autocomplete="off" value="${ems_checked ? item.plusfee : ''}" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><span class="del" onclick="javascript:delRow(this)">删除</span></td>
					</tr>
					</c:forEach>
				  </table>
				</div>
				<div style="padding:5px 3px;"><span class="del" onclick="javascript:addRow(this);">为指定地区城市设置运费</span>
				  <input type="hidden" name="all_select" id="all_select" value="${fn:substringAfter(all_select,',')}" />
				</div>
			  </div>
			</div>
			<c:set var="py_checked" value="${not empty template.pyFee || not empty template.pyPlusfee || fn:length(pyDetails)>0}"/>
			<c:set var="all_select" value=""/>
			<div style="margin-bottom:10px;">
			  <p><label for="py"><input type="checkbox" name="py" id="py" onclick="selectLogis(this,'py')" ${py_checked ? 'checked' : ''}>平邮</label></p>
			  <div id="div_py" style="border:1px solid #999; padding:0px 5px; display:${py_checked ? '' : 'none'};">
				<div style="padding:5px; margin:8px 0px 5px; border: 1px solid #9FCD75;background: #F1FDE9;">默认运费：
				  <input name="py_num" id="py_num" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" />
				  件内，
				  <input name="py_fee" type="text" class="input" id="py_fee" maxlength="6" autocomplete="off" value="${py_checked ? template.pyFee : ''}" />
				  元， 每增加
				  <input name="py_plusnum" id="py_plusnum" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" />
				  件， 增加运费
				  <input name="py_plusfee" type="text" class="input" id="py_plusfee" maxlength="6" autocomplete="off" value="${py_checked ? template.pyPlusfee : ''}" />
				  元
				</div>
				<div id="div_py_detail" style="display:${fn:length(pyDetails)>0 ? '' : 'none'};">
				  <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#CCCCCC" id="tbl_py">
					<tr bgcolor="#EEEEEE">
					  <td align="center" valign="middle">运送到</td>
					  <td width="80" align="center" valign="middle">首件(件)</td>
					  <td width="80" align="center" valign="middle">首费(元)</td>
					  <td width="80" align="center" valign="middle">续件(件)</td>
					  <td width="80" align="center" valign="middle">续费(元)</td>
					  <td width="60" align="center" valign="middle">操作</td>
					</tr>
					<c:forEach items="${pyDetails}" var="item">
					<c:set var="all_select" value="${all_select},${item.area}"/>
					<tr>
					  <td bgcolor="#FFFFFF" class="cell-area"><span class="area">${item.area}</span><span class="allarea"></span><a href="javascript:void(0);" onclick="javascript:showProvince(this)" class="edit">编辑</a>
					  <input type="hidden" name="row_select" id="row_select" value="${item.area}" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="num" id="num" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="fee" type="text" class="input" id="fee" maxlength="6" autocomplete="off" value="${py_checked ? item.fee : ''}" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="plusnum" id="plusnum" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="plusfee" type="text" class="input" id="plusfee" maxlength="6" autocomplete="off" value="${py_checked ? item.plusfee : ''}" /></td>
					  <td align="center" valign="middle" bgcolor="#FFFFFF"><span class="del" onclick="javascript:delRow(this)">删除</span></td>
					</tr>
					</c:forEach>
				  </table>
				</div>
				<div style="padding:5px 3px;"><span class="del" onclick="javascript:addRow(this);">为指定地区城市设置运费</span>
				  <input type="hidden" name="all_select" id="all_select" value="${fn:substringAfter(all_select,',')}" />
				</div>
			  </div>
			</div>
		  </div>
		</div>
	</form>
	<div id="div_template" style="display:none;">
	  <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#CCCCCC" id="tbl_template">
		<tr>
		  <td bgcolor="#FFFFFF" class="cell-area"><span class="area">未添加地区</span><span class="allarea"></span><a href="javascript:void(0);" onclick="javascript:showProvince(this)" class="edit">编辑</a>
		  <input type="hidden" name="row_select" id="row_select" /></td>
		  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="num" id="num" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" /></td>
		  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="fee" type="text" class="input" id="fee" maxlength="6" autocomplete="off" /></td>
		  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="plusnum" id="plusnum" type="text" class="disabled" value="1" readonly="readonly" maxlength="6" autocomplete="off" /></td>
		  <td align="center" valign="middle" bgcolor="#FFFFFF"><input name="plusfee" type="text" class="input" id="plusfee" maxlength="6" autocomplete="off" /></td>
		  <td align="center" valign="middle" bgcolor="#FFFFFF"><span class="del" onclick="javascript:delRow(this)">删除</span></td>
		</tr>
	  </table>
	</div>
	<div id="province">
	  <ul></ul>
	  <div style="clear:both;margin-top:5px;"><input value="确定" type="button" onclick="selectProvince();" /><input value="取消" type="button" onclick="hideProvince();" /></div>
	</div>
</div>
<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
	<a class="mini-button" onclick="saveData()" style="width:60px;margin-right:20px;">保存</a>
	<a class="mini-button" onclick="closeWindow('cancel')" style="width:60px;">取消</a>
</div>

<script type="text/javascript">
	mini.parse();

	//var form = new mini.Form("#form1");

	function saveData() {

		var data = getFreightTemplate();
		if (data==null){return;}
		var freightTemplateDetails = mini.encode(data.details);
		//var logisDetailsJson = $.toJSON(data.logisDetailsJson);
		data["freightTemplateDetails"] = freightTemplateDetails; //转换为字符串
		delete data.details;
		data["id"] = "${template.id}";
		data["createDate"] = "${template.createDate}";

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
</script>
</body>
</html>
