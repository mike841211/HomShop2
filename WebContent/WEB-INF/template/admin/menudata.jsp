<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="com.homlin.module.AppConstants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%!
	private Set<String> authoritySet;
	private Boolean isSuperAdmin = false;
	private Boolean isAuthorized(String... keys) {
		if (isSuperAdmin) {
			return true;
		}
		if (authoritySet == null) {
			return false;
		}
		for (String key : keys) {
			if (authoritySet.contains(key)) {
				return true;
			}
		}
		return false;
	}
%>
<%
	Object object = session.getAttribute(AppConstants.SESSION_ADMIN_ISSUPER);
	if (object == null) {
		isSuperAdmin = false;
	} else {
		isSuperAdmin = (Boolean) object;
	}
	authoritySet = (Set<String>) session.getAttribute(AppConstants.SESSION_ADMIN_AUTHORITY);
%>
[
	<% String group = "group1"; %>
	<% if(isAuthorized("PRODUCT","PRODUCT_CATEGORY","SPECIFICATION","BRAND","PRODUCT_COMMENT")){%>
	<% group = "group1"; %>
	{ pid: "", id: "<%= group %>", text: "商品管理", iconCls: "icon-add"},
		<% if(isAuthorized("PRODUCT")){%>
		{ pid: "<%= group %>", id: "product", text: "商品列表", iconCls: "icon-node", url: "product/list.htm" },<%}%>
		<% if(isAuthorized("GOODS_CATEGORY")){%>
		{ pid: "<%= group %>", id: "product_category", text: "商品分类", iconCls: "icon-node", url: "product_category/list.htm" },<%}%>
		<% if(isAuthorized("SPECIFICATION")){%>
		{ pid: "<%= group %>", id: "specification", text: "规格管理", iconCls: "icon-node", url: "specification/list.htm" },<%}%>
		<%-- { pid: "<%= group %>", id: "parameter", text: "商品参数", iconCls: "icon-node", url: "parameter/list.htm" }, --%>
		<%-- { pid: "<%= group %>", id: "attribute", text: "商品属性", iconCls: "icon-node", url: "attribute/list.htm" }, --%>
		<% if(isAuthorized("BRAND")){%>
		{ pid: "<%= group %>", id: "brand", text: "品牌管理", iconCls: "icon-node", url: "brand/list.htm" },<%}%>
		<% if(isAuthorized("PRODUCT")){%>
		{ pid: "<%= group %>", id: "product_baseinfo", text: "商品描述模板", iconCls: "icon-node", url: "product_baseinfo/list.htm" },<%}%>
		<%-- <% if(isAuthorized("FREIGHT_TEMPLATE")){%>
		{ pid: "<%= group %>", id: "freight_template", text: "运费模板", iconCls: "icon-node", url: "freight_template/list.htm" },<%}%> --%>
		<% if(isAuthorized("PRODUCT_COMMENT")){%>
		<c:if test="${CacheUtil.getConfig('SHOP_PRODUCT_COMMENT_ENABLED')=='1'}">
		{ pid: "<%= group %>", id: "comment", text: "商品评价", iconCls: "icon-node", url: "product_comment/list.htm" },</c:if><%}%>
	<%}%>

	<% if(isAuthorized("ORDER","SHIPPING_METHOD")){%>
	<% group = "group2"; %>
	{ pid: "", id: "<%= group %>", text: "订单管理", iconCls: "icon-add"},
		<% if(isAuthorized("ORDER")){%>
		{ pid: "<%= group %>", id: "order", text: "订单管理", iconCls: "icon-node", url: "order/list.htm" },<%}%>
		<% if(isAuthorized("SHIPPING_METHOD")){%>
		{ pid: "<%= group %>", id: "shipping_method", text: "配送方式", iconCls: "icon-node", url: "shipping_method/list.htm" },<%}%>
		<%-- // ---
		// { pid: "<%= group %>", id: "_1", text: "零售小票", iconCls: "icon-node", url: "/_/1/list.jsp" },
		// { pid: "<%= group %>", id: "_2", text: "收银台", iconCls: "icon-node", url: "/_/2/list.jsp" },
		// { pid: "<%= group %>", id: "_3", text: "库存查询", iconCls: "icon-node", url: "/_/3/list.jsp" },
		// --- --%>
	<%}%>

	<% if(isAuthorized("ARTICLE","ARTICLE_CATEGORY","ADVERT","FRIENDLINK")){%>
	<% group = "group3"; %>
	{ pid: "", id: "<%= group %>", text: "页面内容", iconCls: "icon-add"},
		<% if(isAuthorized("ARTICLE")){%>
		{ pid: "<%= group %>", id: "article", text: "文章管理", iconCls: "icon-node", url: "article/list.htm" },<%}%>
		<% if(isAuthorized("ARTICLE_CATEGORY")){%>
		{ pid: "<%= group %>", id: "article_category", text: "文章分类", iconCls: "icon-node", url: "article_category/list.htm" },<%}%>
		<% if(isAuthorized("ADVERT")){%>
		{ pid: "<%= group %>", id: "advert", text: "广告位/自定义区", iconCls: "icon-node", url: "ad/list.htm" },<%}%>
		<%-- <% if(isAuthorized("FRIENDLINK")){%>
		{ pid: "<%= group %>", id: "friendLink", text: "友情链接", iconCls: "icon-node", url: "friendLink_list.htm" },<%}%> --%>
		<% if(isAuthorized("NAVIGATION")){%>
		{ pid: "<%= group %>", id: "navigation", text: "导航管理", iconCls: "icon-node", url: "navigation/list.htm" },<%}%>
	<%}%>

	<% if(isAuthorized("MEMBER","MEMBER_GRADE","SENTMESSAGE")){%>
	<% group = "group4"; %>
	{ pid: "", id: "<%= group %>", text: "会员管理", iconCls: "icon-add"},
		<% if(isAuthorized("MEMBER")){%>
		{ pid: "<%= group %>", id: "member", text: "会员管理", iconCls: "icon-node", url: "member/list.htm" },<%}%>
		<% if(isAuthorized("MEMBER_GRADE")){%>
		{ pid: "<%= group %>", id: "member_grade", text: "会员等级", iconCls: "icon-node", url: "member_grade/list.htm" },<%}%>
		<%-- <% if(isAuthorized("SENTMESSAGE")){%>
		{ pid: "<%= group %>", id: "sentmessage", text: "发送手机短信", iconCls: "icon-node", url: "sentMobileMessage.htm" },<%}%> --%>
	<%}%>

	<% if(isAuthorized("WEIXIN_MENU","WEIXIN_MENUMSG","WEIXIN_AUTOREPLY","WEIXIN_GROUP","WEIXIN_USER","WEIXIN_MSG","WEIXIN_PAYFEEDBACK")){%>
	<% group = "group5"; %>
	{ pid: "", id: "<%= group %>", text: "微信管理", iconCls: "icon-add"},
		<% if(isAuthorized("WEIXIN_MENU")){%>
		{ pid: "<%= group %>", id: "menu", text: "菜单设置", iconCls: "icon-node", url: "weixin/menu/list.htm" },<%}%>
		<% if(isAuthorized("WEIXIN_MENUMSG")){%>
		{ pid: "<%= group %>", id: "menu_msg", text: "菜单消息设置", iconCls: "icon-node", url: "weixin/menu_msg/list.htm" },<%}%>
		<% if(isAuthorized("WEIXIN_AUTOREPLY")){%>
		{ pid: "<%= group %>", id: "autoreply", text: "自动回复", iconCls: "icon-node", url: "weixin/autoreply/list.htm" },<%}%>
		<% if(isAuthorized("WEIXIN_GROUP")){%>
		{ pid: "<%= group %>", id: "group", text: "分组管理", iconCls: "icon-node", url: "weixin/group/list.htm" },<%}%>
		<% if(isAuthorized("WEIXIN_USER")){%>
		{ pid: "<%= group %>", id: "user", text: "用户管理", iconCls: "icon-node", url: "weixin/user/list.htm" },<%}%>
		<% if(isAuthorized("WEIXIN_MSG")){%>
		{ pid: "<%= group %>", id: "msg", text: "消息管理", iconCls: "icon-node", url: "weixin/msg/list.htm" },<%}%>
		<% if(isAuthorized("WEIXIN_PAYFEEDBACK")){%>
		{ pid: "<%= group %>", id: "payfeedback", text: "维权仲裁", iconCls: "icon-node", url: "weixin/payfeedback/list.htm" },<%}%>
		{ pid: "<%= group %>", id: "qrcode", text: "二维码工具", iconCls: "icon-node", url: "weixin/qrcode/list.htm" },
	<%}%>
	<%// if(isAuthorized("MEMBER","ADMIN","ROLE","ACTIONLOG","CACHE","CONFIG")){%>
	<% group = "group6"; %>
	{ pid: "", id: "<%= group %>", text: "配置管理", iconCls: "icon-add"},
		<% if(isAuthorized("ADMIN")){%>
		{ pid: "<%= group %>", id: "admin", text: "账号管理", iconCls: "icon-node", url: "admin/list.htm" },<%}%>
		<% if(isAuthorized("ROLE")){%>
		{ pid: "<%= group %>", id: "role", text: "角色管理", iconCls: "icon-node", url: "role/list.htm" },<%}%>
		<% if(isAuthorized("CONFIG")){%>
		{ pid: "<%= group %>", id: "config", text: "系统设置", iconCls: "icon-node", url: "config/list.htm" },<%}%>

		{ pid: "<%= group %>", id: "myInfo", text: "个人信息", iconCls: "icon-node", url: "admin/myInfo.htm" },
	<%//}%>

	<%// if(isAuthorized("MEMBER","ADMIN","ROLE","ACTIONLOG","CACHE","CONFIG")){%>
	<% group = "group7"; %>
	{ pid: "", id: "<%= group %>", text: "系统管理", iconCls: "icon-add"},
		<% if(isAuthorized("ADMIN")){%>
		{ pid: "<%= group %>", id: "admin", text: "商家管理", iconCls: "icon-node", url: "shop/list.htm" },<%}%>
		<% if(isAuthorized("ADMIN")){%>
		{ pid: "<%= group %>", id: "admin", text: "账号管理", iconCls: "icon-node", url: "shop/list.htm" },<%}%>
		
		<% if(isAuthorized("ACTIONLOG")){%>
		{ pid: "<%= group %>", id: "actionlog", text: "操作日志", iconCls: "icon-node", url: "actionlog/list.htm" },<%}%>
		<% if(isAuthorized("CACHE")){%>
		{ pid: "<%= group %>", id: "cache", text: "缓存管理", iconCls: "icon-node", url: "cache/index.htm" },<%}%>
	<%//}%>
]