<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="tbl fixed_bottom" id="J_bottom_menus">
	<div class="tbl-cell p33">
		<a class="subcate-a">客户案例</a>
		<div class="subcate">
			<ul>
				<li><a href="${base}/module/case/list.htm">经典案例</a></li>
			</ul>
			<span class="arr"></span>
		</div>
	</div>
	<div class="tbl-cell p34">
		<a class="subcate-a">关于网讯</a>
		<div class="subcate">
			<ul>
				<li><a href="${base}/module/about.htm">公司简介</a></li>
				<li><a href="${base}/module/culture.htm">企业文化</a></li>
				<li><a href="${base}/module/branding_intro.htm">品牌释义</a></li>
			</ul>
			<span class="arr"></span>
		</div>
	</div>
	<div class="tbl-cell p33">
		<a class="subcate-a">产品经验</a>
		<div class="subcate">
			<ul>
				<li><a href="${base}/module/expericent.htm?data=expericent_1">电子商务</a></li>
				<li><a href="${base}/module/expericent.htm?data=expericent_2">网站建设</a></li>
				<li><a href="${base}/module/expericent.htm?data=expericent_3">综合服务</a></li>
				<li><a href="${base}/module/expericent.htm?data=expericent_4">基础项目</a></li>
			</ul>
			<span class="arr"></span>
		</div>
	</div>
</div>
<div class="tips" id="J_tips"></div>
<footer class="footer" id="J_footer">
	<!-- <div class="footer-login">
		<span class="footer-backtop"><a href="#">回到顶部</a></span>
	</div> -->
	<div class="footer-copyright">${CacheUtil.getConfig('SHOP_COPYRIGHT')}</div>
</footer>
