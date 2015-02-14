<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>商品评价 - ${CacheUtil.getConfig('SHOP_NAME')}</title>
<jsp:include page="../inc.required.jsp" />
<script type="text/javascript" src="${base}/resources/common/js/ChinaArea.js"></script>
<style type="text/css">
	section{padding:10px;background:#fff;border-bottom:1px solid #cfcfcf;margin-bottom:0px;}
	.details {min-height:320px;}
	.caption{font-size:1.2em;font-weight:bold; padding-bottom:5px;}
	.search-lst {padding: 0;}
	.product-name{color: #333;}
	.status{background:#efefef;border-bottom: 1px solid #cfcfcf;}
	.product-lst .product-li:last-child {border-bottom: 0;}

	.comment-box {width:98%;margin:10px 1%;}
	.comment-box .comment-score .text {width: 40px;height: 40px;padding:0;line-height: 44px;}
	.comment-box .comment-score .star {background: url(${base}/resources/mobile/default/css/star.png) 0 0px no-repeat;width: 40px;height: 40px;background-size:40px 80px;padding:0;}
	.comment-box .comment-score .star-active {background-position-y:-40px;}
	.comment-box .comment-contents {border:1px solid #cfcfcf;padding:2%;width:96%;line-height: 0;}
	.comment-box .comment-contents textarea {width:100%;color: #999;border:0;}
	.comment-box .new-abtn-type{display:block;padding:8px;border-radius:5px;background-color:#c00;font-size:14px;color:#fff;text-align:center;width:90%;margin:10px auto 0;}
</style>
<script type="text/javascript">
	$(function($){
		$('.product-img img').load(function(){
			$(this).closest('.product-img').addClass('loaded');
		});

		$('.uncomment .star').click(function(){
			var $this = $(this);
			var score = $this.data("score");
			if ($this.hasClass('star-active')){
				$this.removeClass('star-active');
				$this.nextAll('.star').removeClass('star-active');
			}else{
				$this.addClass('star-active');
				$this.prevAll('.star').addClass('star-active');
			}
		});
		$('.uncomment .submit').click(function(){
			var $this = $(this);
			var item_id = $this.data("item-id");
			var score = $this.siblings('.comment-score').find('.star-active').size();
			if (score<1){
				$('#J_tips').html("请评分！").show().delay(1000).fadeOut(500);
				return;
			}
			if (score>5){score=5;}
			var $textarea = $this.siblings('.comment-contents').find('textarea');
			$.ajax({
				url: "${base}/order/comment.do",
				data: {item_id:item_id, score:score, contents:$textarea.val()},
				type: "POST",
				dataType: "json",
				cache: false,
				success: function (json) {
					if (json.status=="success"){
						//$("#total_quantity").html(json.result.total_quantity);
						$('#J_tips').html("提交成功！").show().delay(1000).fadeOut(500);
						$this.siblings('.comment-score').find('.star').unbind('click');
						$textarea.attr('readonly','true');
						$this.remove();
					}else{
						alert(json.message);
					}
				}
			});
		});
	});
</script>
</head>

<body>
<c:set var="header_title" value="商品评价" scope="request" />
<jsp:include page="../inc.header.jsp" />
<section class="status">
	<div class="tbl">
		<div class="tbl-cell">订单号：${order.sn}</div>
	</div>
</section>
<section class="details">
	<div class="search-lst">
		<ul class="product-lst">
			<c:forEach items="${order.tbShopOrderItems}" var="item">
            <li class="product-li">
                <a href="${base}/product/item/${item.productId}.htm" class="product-a">
                    <span class="product-img"><img src="${empty item.imagePath ? noimage : item.imagePath}" alt="${item.name}"></span>
                    <span class="product-info">
                        <strong class="l2h product-name">${item.name}</strong>
						<span class="l2h"><span class="sn">${item.sn}</span> <span class="red2">${item.specifications}</span></span>
                        <span><strong class="red2">￥${item.price}</strong> x ${item.quantity}<%-- <c:if test="${item.discount<1}"> x ${item.discount}(会员折扣)</c:if> --%></span>
					</span>
                </a>
                <div class="comment-box ${empty item.tbShopProductComment ? 'uncomment' : ''}">
					<div class="tbl comment-score">
						<div class="tbl-cell text">评分</div>
						<div class="tbl-cell star ${item.tbShopProductComment.score>=1 ? 'star-active' : ''}" data-score="1"></div>
						<div class="tbl-cell star ${item.tbShopProductComment.score>=2 ? 'star-active' : ''}" data-score="2"></div>
						<div class="tbl-cell star ${item.tbShopProductComment.score>=3 ? 'star-active' : ''}" data-score="3"></div>
						<div class="tbl-cell star ${item.tbShopProductComment.score>=4 ? 'star-active' : ''}" data-score="4"></div>
						<div class="tbl-cell star ${item.tbShopProductComment.score>=5 ? 'star-active' : ''}" data-score="5"></div>
						<div class="tbl-cell">&nbsp;</div>
					</div>
					<div class="tbl comment-contents">
					<textarea name="contents" rows="3" cols="" placeholder="评价" ${empty item.tbShopProductComment ? '' : 'readonly'}>${item.tbShopProductComment.contents}</textarea>
					</div>
					<c:if test="${empty item.tbShopProductComment}">
					<a class="new-abtn-type submit" data-item-id="${item.id}">提交评价</a>
					</c:if>
                </div>
            </li>
			</c:forEach>
		</ul>
    </div>
</section>
<jsp:include page="../inc.footer.jsp" />
</body>
</html>
