<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>${article.title}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${article.title}">
<meta http-equiv="description" content="${article.summary}">
<link rel="stylesheet" type="text/css" href="${base}/resources/mobile/${cfg_template}/css/article.css">
</head>

<body>
<article>
	<header>
		<h1>${article.title}</h1>
		<fmt:parseDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss" var="postDate" />
		<span class="post-date"><fmt:formatDate value="${postDate}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
	</header>
	<section class="fixed-img" id="img-content">
		<c:if test="${not empty article.cover}"><div class="cover"><img src="${article.cover}" /></div></c:if>
		<c:if test="${not empty article.summary}"><div class="summary"><% request.setAttribute("vEnter", "\n"); %>${fn:replace(fn:replace(article.summary,' ','&nbsp;'),vEnter,'<br>')}</div></c:if>
		<div class="content">${article.content}</div>
	</section>
</article>
<script type="text/javascript">

        //弹出框中图片的切换
        (function(){
            var imgsSrc = [];
            function reviewImage(src) {
                if (typeof window.WeixinJSBridge != 'undefined') {
                    WeixinJSBridge.invoke('imagePreview', {
                        'current' : src,
                        'urls' : imgsSrc
                    });
                }
            }
            function onImgLoad() {
                var imgs = document.getElementById("img-content");
                imgs = imgs ? imgs.getElementsByTagName("img") : [];
                for( var i=0,l=imgs.length; i<l; i++ ){//忽略第一张图 是提前加载的loading图而已
                    var img = imgs.item(i);
                    var src = img.getAttribute('data-src') || img.getAttribute('src');
                    if( src ){
						if(!(src.substr(0,4).toLowerCase()=='http')){src=location.origin+src;}
                        imgsSrc.push(src);
                        (function(src){
                            if (img.addEventListener){
                                img.addEventListener('click', function(){
                                    reviewImage(src);
                                });
                            }else if(img.attachEvent){
                                img.attachEvent('click', function(){
                                    reviewImage(src);
                                });
                            }
                        })(src);
                    }
                }
            }
            if( window.addEventListener ){
                window.addEventListener('load', onImgLoad, false);
            }else if(window.attachEvent){
                window.attachEvent('load', onImgLoad);
                window.attachEvent('onload', onImgLoad);
            }
        })();
</script>
</body>
</html>