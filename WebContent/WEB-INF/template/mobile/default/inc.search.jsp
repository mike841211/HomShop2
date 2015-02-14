<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<form action="${base}/product/search.htm" id="J_searchForm">
	<div class="cate-srch">
	  <div class="srch-box">
		<input name="s" id="J_keyword" type="text" class="srch-input" value="${empty param.s ? CacheUtil.getConfig('SHOP_DEFAULTKEY') : param.s}" style="color:#999999;">
		<a href="javascript:void(0);" target="_self" onclick="$('#J_keyword').val('')" class="srch-clear"></a>
		<a href="javascript:void(0);" target="_self" onclick="$('#J_searchForm').submit();" class="srch-submit"><span></span></a>
	  </div>
	</div>
</form>