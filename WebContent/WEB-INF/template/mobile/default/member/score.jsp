<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>会员中心 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<script type="text/javascript" src="${base}/resources/mobile/default/js/jquery.pager.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/default/js/shop.js"></script>
<script type="text/javascript">
$(function($){
	$(".pagination").pager({pagenumber: ${pager.pageIndex},pagecount: ${pager.pageCount},buttonClickCallback: Shop.pageChange});
});
</script>
<style type="text/css">
	body{background:#f8f8f8}
	section{min-height:400px;}

	.align-r{text-align:right;}
	.remark{color:#555;}
	.dl1 .detail{padding: 5px 8px;line-height: 26px;}
	.dl1 .datetime{color:#999;}
	.dl1 .num1{color:#c00;font-weight:bold;}
	.dl1 .num2{color:#080;font-weight:bold;}
</style>
</head>

<body>
<c:set var="header_title" value="会员积分" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section>
	<div class="dl1">
		<dl>
			<dd><a href="${base}/member/index.htm">返回会员中心</a></dd>
		</dl>
	</div>
	<div class="dl1">
		<dl>
			<dt>积分记录</dt>
			<c:forEach items="${pager.dataList}" var="item">
			<dd class="detail">
				<div class="tbl">
					<div class="tbl-cell datetime">${item.createDate}</div>
					<div class="tbl-cell align-r ${item.val>0 ? 'num1' : 'num2'}"><fmt:formatNumber value="${item.val}" pattern="#,##0"/></div>
				</div>
				<div class="tbl remark">${item.remark}</div>
			</dd>
			</c:forEach>
		</dl>
	</div>
	<div class="pagination"></div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>