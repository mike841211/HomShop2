<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <title>系统提示</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<style type="text/css">
		h1,h2,h3,h4,h5,p {margin:10px 20px;padding:0px;}
	</style>
  </head>

  <body>
	<c:choose>
		<c:when test="${message=='权限不足'}">
		<div style="margin-top:30px;text-align:center;">
			<img src="${base}/resources/common/images/error01.png" width="300" height="90" border="0" alt="权限不足">
		</div>
		</c:when>
		<c:otherwise>
		<div style="border-bottom:3px solid #333333;">
			<img src="${base}/resources/common/images/systip.png" width="140" height="30" border="0" alt="">
		</div>
		<p>${message}</p>
		<p>${exception}</p>
		<p>${exception.message}</p>
		</c:otherwise>
	</c:choose>
  </body>
</html>
