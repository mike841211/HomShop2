<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
<script src="${base}/resources/admin/js/TEMPLATE_DATA.js" type="text/javascript"></script>
</head>
<body>
<div class="mini-fit">
	<div id="tabs1" class="mini-tabs" activeIndex="0" style="height:100%;">
	  <div name="first" title="基本信息">
		<form id="cfgShop" onsubmit="return false">
		  <table style="table-layout:fixed;">
		    <tr>
		      <td style="width:120px;">网店名称：</td>
		      <td><input class="mini-textbox" name="cfgValue" value="${config['SHOP_NAME'].cfgValue}" required="true" style="width:450px;" />
			  <input class="mini-hidden" name="cfgKey" value="${config['SHOP_NAME'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>网店网址：</td>
		      <td><input class="mini-textbox" name="cfgValue" value="${config['SHOP_URL'].cfgValue}" required="true" style="width:450px;" />
			  <input class="mini-hidden" name="cfgKey" value="${config['SHOP_URL'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>默认搜索词：</td>
		      <td><input class="mini-textbox" name="cfgValue" value="${config['SHOP_DEFAULTKEY'].cfgValue}" />
			  <input class="mini-hidden" name="cfgKey" value="${config['SHOP_DEFAULTKEY'].cfgKey}" /></td>
		    </tr>
		    <%-- <tr>
		      <td>热门搜索关键词：</td>
		      <td><input class="mini-textbox" name="cfgValue" value="${config['SHOP_HOTKEYS'].cfgValue}" style="width:450px;" /> “;”分号分隔
			  <input class="mini-hidden" name="cfgKey" value="${config['SHOP_HOTKEYS'].cfgKey}" /></td>
		    </tr> --%>
			<tr>
			  <td>首页页面关键词：</td>
			  <td><input class="mini-textbox" name="cfgValue" style="width:450px;" value="${config['SHOP_METAKEYWORDS'].cfgValue}" />
			  <input class="mini-hidden" name="cfgKey" value="${config['SHOP_METAKEYWORDS'].cfgKey}" /></td>
			</tr>
			<tr>
			  <td>首页页面描述：</td>
			  <td><input class="mini-textbox" name="cfgValue" style="width:450px;" value="${config['SHOP_METADESCRIPTION'].cfgValue}" />
			  <input class="mini-hidden" name="cfgKey" value="${config['SHOP_METADESCRIPTION'].cfgKey}" /></td>
			</tr>
			<%-- <tr>
			  <td>版权信息：</td>
			  <td><input class="mini-textbox" name="cfgValue" style="width:450px;" value="${config['SHOP_COPYRIGHT'].cfgValue}" />
			  <input class="mini-hidden" name="cfgKey" value="${config['SHOP_COPYRIGHT'].cfgKey}" /></td>
			</tr> --%>
			<tr>
			  <td>客服QQ：</td>
			  <td><input class="mini-textbox" name="cfgValue" value="${config['SHOP_QQ'].cfgValue}" vtype="range:10000,9999999999" errorMode="border" rangeErrorText="格式错误" /><input class="mini-hidden" name="cfgKey" value="${config['SHOP_QQ'].cfgKey}" /> QQ号须要 <a href="http://shang.qq.com/widget/consult.php" target="_blank">开启服务</a></td>
			</tr>
		    <tr>
		      <td>新注册用户默认状态：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['MEMBER_ENABLED'].cfgValue}" data="[{'text':'锁定','id':'0'},{'text':'解锁','id':'1'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['MEMBER_ENABLED'].cfgKey}" /></td>
		    </tr>
		    <%-- <tr>
		      <td>使用购物车要求登入：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['SHOP_CART_NEED_LOGIN'].cfgValue}" data="[{'text':'是','id':'1'},{'text':'否','id':'0'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_CART_NEED_LOGIN'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>下单时要求登入：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['SHOP_ORDER_NEED_LOGIN'].cfgValue}" data="[{'text':'是','id':'1'},{'text':'否','id':'0'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_ORDER_NEED_LOGIN'].cfgKey}" /></td>
		    </tr> --%>
		    <tr>
		      <td>积分获取参数：</td>
		      <td><div name="cfgValue" class="mini-spinner" minValue="0" maxValue="9999" value="${config['SHOP_SCORE_ORDERGAIN_MULTIPLE'].cfgValue}"></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_SCORE_ORDERGAIN_MULTIPLE'].cfgKey}" /> 每消费 <span style="color:red;">1元</span> 获得积分（实付）</td>
		    </tr>
		    <tr>
		      <td>积分抵扣参数：</td>
		      <td><div name="cfgValue" class="mini-spinner" minValue="0" maxValue="9999" value="${config['SHOP_SCORE_ORDERCREDIT_MULTIPLE'].cfgValue}"></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_SCORE_ORDERCREDIT_MULTIPLE'].cfgKey}" /> 每抵扣 <span style="color:red;">1分</span> 钱消耗积分</td>
		    </tr>
		    <tr>
		      <td>订单积分抵扣：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['SHOP_ORDER_USESCORE'].cfgValue}" data="[{'text':'开启','id':'1'},{'text':'关闭','id':'0'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_ORDER_USESCORE'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>签到基础得分：</td>
		      <td><div name="cfgValue" class="mini-spinner" minValue="0" maxValue="99999" value="${config['PLUGIN_SIGNIN_BASESCORE'].cfgValue}"></div><input class="mini-hidden" name="cfgKey" value="${config['PLUGIN_SIGNIN_BASESCORE'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>签到最高加倍天数：</td>
		      <td><div name="cfgValue" class="mini-spinner" minValue="0" maxValue="100" value="${config['PLUGIN_SIGNIN_DATES'].cfgValue}"></div><input class="mini-hidden" name="cfgKey" value="${config['PLUGIN_SIGNIN_DATES'].cfgKey}" /> 最高加倍天数n，签到基础得分*连续天数n</td>
		    </tr>
		    <tr style="display:none;">
		      <td>商品购买评价：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['SHOP_PRODUCT_COMMENT_ENABLED'].cfgValue}" data="[{'text':'开启','id':'1'},{'text':'关闭','id':'0'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_PRODUCT_COMMENT_ENABLED'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td></td>
		      <td><a class="mini-button" onclick="saveConfig('cfgShop')" style="width:60px;margin-right:10px;">保存</a></td>
		    </tr>
		    <tr style="display:none;">
		      <td colspan="2"><a class="mini-button" onclick="initConfig('AlipayConfig')" style="margin-right:10px;">更新支付宝配置文件</a>如果修改了config.properties对应配置请执行此操作</td>
		    </tr>
		    <tr style="display:none;">
		      <td colspan="2"><a class="mini-button" onclick="initConfig('UpmpConfig')" style="margin-right:10px;">更新银联手机支付配置文件</a>如果修改了config.properties对应配置请执行此操作</td>
		    </tr>
		    <tr style="display:none;">
		      <td colspan="2"><a class="mini-button" onclick="initConfig('WxpayConfig')" style="margin-right:10px;">更新微信支付配置文件</a>如果修改了config.properties对应配置请执行此操作</td>
		    </tr>
		    <tr style="display:none;">
		      <td colspan="2"><a class="mini-button" onclick="initConfig('UpopConfig')" style="margin-right:10px;">更新银联支付配置文件</a>如果修改了config.properties对应配置请执行此操作</td>
		    </tr>
		    <tr style="display:none;">
		      <td colspan="2"><a class="mini-button" onclick="initConfig('TenpayConfig')" style="margin-right:10px;">更新财付通支付配置文件</a>如果修改了config.properties对应配置请执行此操作</td>
		    </tr>
		  </table>
		</form>
	  </div>
	  <div title="页面模板">
		<form id="cfgTemplate" onsubmit="return false">
		  <table style="table-layout:fixed;">
		    <tr>
		      <td style="width:120px;">页面模板：</td>
		      <td><input class="mini-hidden" name="cfgValue" value="${config['TEMPLATE_MOBILE'].cfgValue}" id="J_TEMPLATE_MOBILE" />
			  <input class="mini-hidden" name="cfgKey" value="${config['TEMPLATE_MOBILE'].cfgKey}" /><input class="mini-combobox" data="TEMPLATE_MOBILE.styles" onitemclick="onItemClick(e,'J_TEMPLATE_MOBILE')" valueField="value" value="${config['TEMPLATE_MOBILE'].cfgValue}" required="true" /></td>
		    </tr>
		    <tr>
		      <td>首页模板：</td>
		      <td><input class="mini-hidden" name="cfgValue" value="${config['TEMPLATE_MOBILE_INDEX'].cfgValue}" id="J_TEMPLATE_MOBILE_INDEX" />
			  <input class="mini-hidden" name="cfgKey" value="${config['TEMPLATE_MOBILE_INDEX'].cfgKey}" /><input class="mini-combobox" data="TEMPLATE_MOBILE.indexs" onitemclick="onItemClick(e,'J_TEMPLATE_MOBILE_INDEX')" valueField="value" value="${config['TEMPLATE_MOBILE_INDEX'].cfgValue}" required="true" /></td>
		    </tr>
		    <tr>
		      <td></td>
		      <td>建议尺寸：640*960</td>
		    </tr>
		    <tr>
		      <td>首页背景：</td>
		      <td><input class="mini-textbox" name="cfgValue" value="${config['TEMPLATE_MOBILE_INDEX_BGIMG'].cfgValue}" style="width:350px;" id="J_TEMPLATE_MOBILE_INDEX_BGIMG" />
			  <input class="mini-hidden" name="cfgKey" value="${config['TEMPLATE_MOBILE_INDEX_BGIMG'].cfgKey}" /><a class="mini-button" plain="true" iconCls="icon-add" onclick="uploadImages()" >上传</a><a class="mini-button" plain="true" iconCls="icon-add" onclick="window.open(mini.get('J_TEMPLATE_MOBILE_INDEX_BGIMG').getValue())" >查看</a></td>
		    </tr>
		    <tr>
		      <td>商品列表模板：</td>
		      <td><input class="mini-hidden" name="cfgValue" value="${config['TEMPLATE_MOBILE_PRODLIST'].cfgValue}" id="J_TEMPLATE_MOBILE_PRODLIST" />
			  <input class="mini-hidden" name="cfgKey" value="${config['TEMPLATE_MOBILE_PRODLIST'].cfgKey}" /><input class="mini-combobox" data="TEMPLATE_MOBILE.prodlists" onitemclick="onItemClick(e,'J_TEMPLATE_MOBILE_PRODLIST')" valueField="value" value="${config['TEMPLATE_MOBILE_PRODLIST'].cfgValue}" required="true" /></td>
		    </tr>
		    <tr>
		      <td>前台显示市场价：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['SHOP_ISSHOWMARKETPRICE'].cfgValue}" data="[{'text':'显示','id':'1'},{'text':'不显示','id':'0'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_ISSHOWMARKETPRICE'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>前台显示零库存商品：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['SHOP_SHOWOUTOFSTOCK'].cfgValue}" data="[{'text':'显示','id':'1'},{'text':'不显示','id':'0'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_SHOWOUTOFSTOCK'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>延迟加载商品描述：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['SHOP_INTRODUCTION_LAZY'].cfgValue}" data="[{'text':'延迟加载','id':'1'},{'text':'立即加载','id':'0'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_INTRODUCTION_LAZY'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>商品详情轮播图模式：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['SHOP_PRODUCT_ITEM_IMAGE_MODE'].cfgValue}" data="[{'text':'等比自动缩放','id':'ALL'},{'text':'高度超出隐藏','id':'HIDDEN'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_PRODUCT_ITEM_IMAGE_MODE'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>商品详情轮播图高度：</td>
		      <td><div name="cfgValue" class="mini-spinner" minValue="240" maxValue="999" value="${config['SHOP_PRODUCT_ITEM_IMAGE_HEIGHT'].cfgValue}"></div><input class="mini-hidden" name="cfgKey" value="${config['SHOP_PRODUCT_ITEM_IMAGE_HEIGHT'].cfgKey}" /> 默认：320</td>
		    </tr>
		    <tr>
		      <td></td>
		      <td><a class="mini-button" onclick="saveConfig('cfgTemplate')" style="width:60px;margin-right:10px;">保存</a></td>
		    </tr>
		  </table>
		</form>
	  </div>
	  <div title="邮件设置">
		<form id="cfgEmail" onsubmit="return false">
		  <table style="table-layout:fixed;">
		    <tr>
		      <td style="width:110px;">发件人邮箱：</td>
		      <td><input class="mini-textbox" name="cfgValue" id="smtpFromMail" value="${config['EMAIL_ADDRESS'].cfgValue}" required="true" style="width:220px;" /></td>
		      <td><input class="mini-hidden" name="cfgKey" value="${config['EMAIL_ADDRESS'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>SMTP服务器地址：</td>
		      <td><input class="mini-textbox" name="cfgValue" id="smtpHost" value="${config['EMAIL_HOST'].cfgValue}" required="true" style="width:220px;" /></td>
		      <td><input class="mini-hidden" name="cfgKey" value="${config['EMAIL_HOST'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>SMTP服务器端口：</td>
		      <td><input class="mini-textbox" name="cfgValue" id="smtpPort" value="${config['EMAIL_PORT'].cfgValue}" required="true" vtype="int" style="width:220px;" /></td>
		      <td><input class="mini-hidden" name="cfgKey" value="${config['EMAIL_PORT'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>SMTP用户名：</td>
		      <td><input class="mini-textbox" name="cfgValue" id="smtpUsername" value="${config['EMAIL_USERNAME'].cfgValue}" required="true" style="width:220px;" /></td>
		      <td><input class="mini-hidden" name="cfgKey" value="${config['EMAIL_USERNAME'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>SMTP密码：</td>
		      <td><input class="mini-password" name="cfgValue" id="smtpPassword" value="${config['EMAIL_PASSWORD'].cfgValue}" required="true" style="width:220px;" /></td>
		      <td><input class="mini-hidden" name="cfgKey" value="${config['EMAIL_PASSWORD'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td></td>
		      <td><a class="mini-button" onclick="saveConfig('cfgEmail')" style="width:60px;margin-right:10px;">保存</a></td>
		    </tr>
		  </table>
		</form>
		  <table style="table-layout:fixed;">
		    <tr>
		      <td style="width:110px;">测试接收地址：</td>
		      <td><input id="toMail" class="mini-textbox" value="" vtype="email" /></td>
		      <td><a class="mini-button" onclick="testEmailConfig()">发送测试邮件</a></td>
		    </tr>
		  </table>
	  </div>
	  <div title="微信设置">
		<form id="cfgWeixin" onsubmit="return false">
		  <table style="table-layout:fixed;">
		    <tr>
		      <td style="width:120px;"><a href="${base}/common/emoji/index.html" target="_blank">表情符号参考</a></td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['WEIXIN_SUBSCRIBE_MSGTYPE'].cfgValue}" data="[{'text':'关注时回复以下欢迎信息','id':'text'},{'text':'关注时回复图文信息<font color=red> (自动回复须先设置图文信息，关键字：SUBSCRIBE)</font>','id':'news'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['WEIXIN_SUBSCRIBE_MSGTYPE'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>关注欢迎信息：</td>
		      <td><input class="mini-textarea" name="cfgValue" value="${config['WEIXIN_MSG_SUBSCRIBE'].cfgValue}" style="width:450px;height:150px;" />
			  <input class="mini-hidden" name="cfgKey" value="${config['WEIXIN_MSG_SUBSCRIBE'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>无匹配回复信息：</td>
		      <td><input class="mini-textarea" name="cfgValue" value="${config['WEIXIN_MSG_NOMATCH'].cfgValue}" style="width:450px;height:150px;" />
			  <input class="mini-hidden" name="cfgKey" value="${config['WEIXIN_MSG_NOMATCH'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>网页底部导航栏：</td>
		      <td><div name="cfgValue" class="mini-radiobuttonlist" textField="text" valueField="id" value="${config['WEIXIN_HIDETOOLBAR'].cfgValue}" data="[{'text':'隐藏','id':'1'},{'text':'显示 (仅微信中有效)','id':'0'}]" ></div><input class="mini-hidden" name="cfgKey" value="${config['WEIXIN_HIDETOOLBAR'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>公众号基础二维码：</td>
		      <td><input class="mini-textbox" name="cfgValue" value="${config['WEIXIN_QRCODE'].cfgValue}" style="width:450px;" id="WEIXIN_QRCODE" />
			  <input class="mini-hidden" name="cfgKey" value="${config['WEIXIN_QRCODE'].cfgKey}" /><a class="mini-button" onclick="getQrcode()" style="margin-right:10px;">获取二维码</a><a class="mini-button" plain="true" iconCls="icon-add" onclick="window.open(mini.get('WEIXIN_QRCODE').getValue())" >查看二维码</a></td>
		    </tr>
		    <tr>
		      <td>公众号AppId：</td>
		      <td><input class="mini-textbox" name="cfgValue" value="${config['WEIXIN_APPID'].cfgValue}" required="true" style="width:450px;" />
			  <input class="mini-hidden" name="cfgKey" value="${config['WEIXIN_APPID'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>公众号AppSecret：</td>
		      <td><input class="mini-textbox" name="cfgValue" value="${config['WEIXIN_APPSECRET'].cfgValue}" required="true" style="width:450px;" />
			  <input class="mini-hidden" name="cfgKey" value="${config['WEIXIN_APPSECRET'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td>公众号服务器Token：</td>
		      <td><input class="mini-textbox" name="cfgValue" value="${config['WEIXIN_TOKEN'].cfgValue}" required="true" style="width:450px;" />
			  <input class="mini-hidden" name="cfgKey" value="${config['WEIXIN_TOKEN'].cfgKey}" /></td>
		    </tr>
		    <tr>
		      <td></td>
		      <td><a href="https://mp.weixin.qq.com/" target="_blank">微信公众平台:</a> https://mp.weixin.qq.com</td>
		    </tr>
		    <tr>
		      <td></td>
		      <td><a class="mini-button" onclick="saveConfig('cfgWeixin')" style="width:60px;margin-right:10px;">保存</a><a class="mini-button" onclick="initConfig('WeixinConfig')" style="margin-right:10px;">更新微信配置文件</a>如果修改了AppId,AppSecret,Token 保存后须执行此操作</td>
		    </tr>
		  </table>
		</form>
	  </div>
 </div>
</div>

<script type="text/javascript">
	function onItemClick(e,key) {
		mini.get(key).setValue(e.item.value);
	}

	mini.parse();
    var grid2 = mini.get("grid2");

	function saveConfig(formId) {
		var form = new mini.Form(formId);
		form.validate();
		if (form.isValid() == false) {
			return;
		}

		$.ajax({
			url: "update.do",
			type: "POST",
			dataType: "json",
			data: $("#"+formId).serialize(),
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					miniExt.showMsg("保存成功");
				}else{
					alert(json.message);
				}
			}
		});
	}

	function testEmailConfig() {
		var form = new mini.Form("cfgEmail");
		form.validate();
		if (form.isValid() == false) {
			return;
		}

		var toMail = mini.get('toMail');
		if (toMail.getValue()=="" || toMail.validate() == false) {
			alert('测试接收地址不合法');
			return;
		}

		var data = {};
		data["smtpFromMail"] = mini.get('smtpFromMail').getValue();
		data["smtpHost"] = mini.get('smtpHost').getValue();
		data["smtpPort"] = mini.get('smtpPort').getValue();
		data["smtpUsername"] = mini.get('smtpUsername').getValue();
		data["smtpPassword"] = mini.get('smtpPassword').getValue();
		data["toMail"] = mini.get('toMail').getValue();

		$.post(
			"testEmailConfig.do",
			data,
			function (json) {
				if (json.status=="success"){
					alert("配置可用，发送成功！");
				}else{
					alert(json.message);
				}
			},
			"json"
		);
	}

	function uploadImages() {
		miniExt.win.open({
			url: "common/uploadImage.htm",
			title: "上传图片",
			showMaxButton: true,
			width: 800,
			height: 550
		}).callback(function(url){
			mini.get("J_TEMPLATE_MOBILE_INDEX_BGIMG").setValue(url);
		});
	}

	function initConfig(config) {
		$.ajax({
			url: "initConfig.do",
			type: "POST",
			dataType: "json",
			data: {"config":config},
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					miniExt.showMsg("配置参数已更新");
				}else{
					alert(json.message);
				}
			}
		});
	}

	function getQrcode() {
		$.ajax({
			url: "${base}/admin/weixin/qrcode/getBaseQrcode.do",
			type: "POST",
			dataType: "json",
			cache: false,
			success: function (json) {
				try{
					if (json.ticket){
						mini.get('WEIXIN_QRCODE').setValue('https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket='+json.ticket);
						//miniExt.win.showImage('https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket='+json.ticket);
						miniExt.showMsg("已更新");
					}else{
						alert(json.message);
					}
				}catch(e){alert(e);}
			}
		});
	}

</script>
</body>
</html>
