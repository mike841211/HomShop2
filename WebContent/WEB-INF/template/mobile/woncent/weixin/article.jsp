<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<title>${article.title}</title>
<jsp:include page="../inc.required.jsp" />
<meta http-equiv="keywords" content="${article.title}">
<meta http-equiv="description" content="${article.summary}">
<style type="text/css">
	article{
		margin:0 auto;
		border-right: 1px solid #ccc;
		border-left: 1px solid #ccc;
		padding: 10px;
	}
	article header{
		text-align: center;
		font-size: 20px;
		padding-bottom: 10px;
		border-bottom: 1px dotted #ccc;
	}
	article section{
		margin-top: 10px;
	}
	article section .cover{
		text-align: center;
		margin-bottom: 10px;
	}
	article section .summary{
		margin-bottom: 10px;
		border:1px solid #efefef;
		color:#aaa;
		padding:15px 10px;
	}
	article section .content{
		color:#333;
	}
	article footer{
	}
</style>
</head>

<body>
<article>
	<header>${article.title}</header>
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