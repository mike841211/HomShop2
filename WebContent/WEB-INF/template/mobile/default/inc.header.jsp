<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<header>
	<div class="header-bar" id="J_header_bar">
		<a href="javascript:history.back();" class="header-back"><span>返回</span></a>
		<h2>${header_title}</h2>
		<a href="javascript:void(0)" class="header-menu" id="J_header_menus_key"><span>菜单键</span></a>
	</div>
	<div class="header-nav" id="J_header_menus">
		<div class="header-nav-tbl">
			<a ${header_menu==1 ? 'class="on"' : ''} href="${base}/"><span class="icon"></span><p>首页</p></a>
			<a ${header_menu==2 ? 'class="on"' : ''} href="${base}/category/list.htm"><span class="icon2"></span><p>商品分类</p></a>
			<a ${header_menu==3 ? 'class="on"' : ''} href="${base}/cart/list.htm"><span class="icon3"></span><p>购物车</p></a>
			<a ${header_menu==4 ? 'class="on"' : ''} href="${base}/order/list.htm"><span class="icon4"></span><p>我的订单</p></a>
			<%-- <a ${header_menu==4 ? 'class="on"' : ''} href="${base}/member/index.htm"><span class="icon4"></span><p>会员中心</p></a> --%>
		</div>
	</div>
</header>