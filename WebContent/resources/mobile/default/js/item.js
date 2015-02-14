// 商品详情页
var skuid=""; // 当前选中值
$(function($){

	// 图片滑动lazyload
	function loadImg(swiper){
		var img = $(swiper.slides[swiper.activeIndex]).find('img[_src]');
		if (img.length>0) {
			var src = img.attr('_src');
			img.parent().removeClass('loaded');
			img.load(function () {
				img.parent().addClass('loaded');
				img.removeAttr('_src');
			}).attr('src', src);
		}
	}
	// 图片滑动
	var mySwiper = new Swiper('.swiper-container',{
		pagination: '.pagination',
		//centeredSlides: true,
		//slidesPerView: 'auto',
		//autoplay: 3000,
		//loop:true,
		onInit: function(swiper){loadImg(swiper);},
		onSlideChangeStart: function(swiper){loadImg(swiper);},
		grabCursor: true,
		paginationClickable: true
	});
	// focus
	var $swiperCont = $('.swiper-container');
	var $swiperWrapper = $('.swiper-wrapper',$swiperCont);
	var $swiperSlides = $('.swiper-slide',$swiperWrapper);
	var $swiperMask = $('.mask');
	var _ww = $(window).width();
	var _wh = $(window).height();
	var top = (_wh-_ww)/2; //img.width=100%
	if(top<=0){top=0;}
	$(window).on('resize',function(){
		_ww = $(window).width();
		_wh = $(window).height();
		if (_ww>_wh){ // 横向
			if ($swiperCont.hasClass('focus')){
				$swiperMask.hide();
				$swiperCont.removeClass('focus').css({"height":"auto","top":"auto"});
				return;
			}
		}
		top = (_wh-_ww)/2;
		if(top<=0){top=0;}
	});
	$swiperCont.on('click', function(e){
		if (_ww>_wh){return;} // 横向
		$swiperMask.show();
		$swiperCont.addClass('focus').css("height",_ww).css("top",top);
		$swiperWrapper.css("height",_ww);
		$swiperSlides.css("height",_ww);
	})
	$swiperMask.on('click', function(e){
		$swiperMask.hide();
		$swiperCont.removeClass('focus').css({"height":"auto","top":"auto"});
		$swiperWrapper.css("height",mySwiper.height);
		$swiperSlides.css("height",mySwiper.height);
	})

	// loadIntroduction
	$('#J_loadIntroduction').one("click", function() {
		var target = $('#J_introduction');
		//if (target.html()==''){
			target
				.html('<div style="width:30px;margin:10px auto;"><img src="'+base+'/resources/common/images/loading.gif" /></div>')
				.load(base+"/product/item_introduction.do?id="+product.id);
		//}
	});

	// 收藏
	$('.add_favorite').click(function() {
		$.ajax({
			url: base+"/member/favorite/add.do",
			type: "POST",
			dataType: "json",
			data: {id:product.id},
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					$('#J_tips').html(json.message).show().delay(2000).fadeOut(1000);
				}else if (json.code=="unlogin"){
					location.href = base+"/member/login.htm"
				}else{
					alert(json.message);
				}
			}
		});
	});

	// 规格值选择
	var $specification = $("#J_specification dl");
	if ($specification.size()==0){ // 没有规格属性的商品
		var sku = skus[0];
		skuid = sku.id;
		$("#J_price").html('￥'+sku.price.toFixed(2));
		$("#J_stock").html(sku.stock);
		$("#J_sku_sn").html(sku.sn);
	}else{
		var $specificationValue = $("#J_specification a");
		$specificationValue.click(function() {
			var $this = $(this);
			if ($this.hasClass("locked")) {
				return false;
			}
			$this.toggleClass("selected").parent().siblings().children("a").removeClass("selected");
			var selectedValues = new Array();
			$specificationValue.filter(".selected").each(function(i) {
				selectedValues[i] =$(this).data("value").toString();
			});

			// 匹配SKU
			skuid="";
			for (var i in skus){
				var specs = skus[i].specs;
				var l = specs.length;
				if (selectedValues.length!=l){
					continue;
				}
				var match = true;
				for (var j=0; j<l; j++){
					if ($.inArray(specs[j].value_id, selectedValues) < 0) {
						match = false;
						break;
					}
				}
				if (match){
					skuid = skus[i].id;
					$("#J_price").html('￥'+skus[i].price.toFixed(2));
					$("#J_stock").html(skus[i].stock);
					$("#J_sku_sn").html(skus[i].sn);
					break;
				}
			}
			if (skuid==""){
				$("#J_price").html('￥'+product.price.toFixed(2));
				$("#J_stock").html(product.stock);
				$("#J_sku_sn").html(product.sn);
			}
			modifyNumber();

			//console.debug("skuid:"+skuid);

			// [[ locked
			var spec_id = $this.closest('dl').data("value").toString();
			$specification.each(function() {
				var $this = $(this);
				if ($this.data("value").toString()==spec_id){
					return;
				}
				var selectedValue = $this.find("a.selected").data("value");//.toString();
				var otherValues = $.grep(selectedValues, function(n, i) {
					return n != selectedValue;
				});
				//console.debug(selectedValues,selectedValue,otherValues);
				$this.find("a").each(function() {
					var $this = $(this);
					//console.debug($(this).text());
					var value = $this.data("value").toString();
					otherValues.push(value);
					var l = otherValues.length;
					var locked = true;
					$.each(skus, function(i, sku) {
						var specs = sku.specs;
						var skuValues = [];
						for (var i in specs){
							skuValues.push(specs[i].value_id);
						}
						var match = sku.stock>0 && $.inArray(value, skuValues)>-1;
						if (match){
							for (var j=0; j<l; j++){
								if ($.inArray(otherValues[j], skuValues)<0) {
									match = false;
									break;
								}
							}
							if (match){
								locked = false;
								return false;
							}
						}
					});
					if (locked) {
						$this.addClass("locked");
					} else {
						$this.removeClass("locked");
					}
					otherValues.pop();
				});
			});
			// ]] locked
		});
		// 初始化规格选项锁定状态
		$specificationValue.each(function(){
			var $this = $(this);
			var value = $this.data("value").toString();
			var locked = true;
			for (var i in skus){
				if (skus[i].stock<1){
					continue;
				}
				var specs = skus[i].specs;
				var l = specs.length;
				for (var j=0; j<l; j++){
					if (specs[j].value_id==value) {
						locked = false;
						break;
					}
				}
				if (!locked){
					break;
				}
			}
			if (locked){
				$this.addClass("locked");
			}
		});
	}

	// 立即购买
	$('#J_directorder').click(function() {
		if (!skuid && skus.length==1 && skus[0].specs.length==0){ // 没有规格属性的商品
			skuid = skus[0].id;
		}
		if (!skuid){
			$('#J_tips').html('请选择规格').show().delay(2000).fadeOut(1000);
			location.hash="";
			location.hash="select_specification";
			return false;
		}
		var stock = parseInt($('#J_stock').html());
		if (stock<=0){
			$('#J_tips').html('库存不足').show().delay(2000).fadeOut(1000);
			return false;
		}
		var number = parseInt($('#J_number').val());
		if (number>stock){
			$('#J_tips').html('库存不足').show().delay(2000).fadeOut(1000);
			return false;
		}
		location.href = base+"/order/directorder.htm?skuid="+ skuid +"&quantity="+ number;
	});

	// 加入购物车
	$('#J_add_cart').click(function() {
		if (!skuid && skus.length==1 && skus[0].specs.length==0){ // 没有规格属性的商品
			skuid = skus[0].id;
		}
		if (!skuid){
			$('#J_tips').html('请选择规格').show().delay(2000).fadeOut(1000);
			location.hash="";
			location.hash="select_specification";
			return false;
		}
		var stock = parseInt($('#J_stock').html());
		if (stock<=0){
			$('#J_tips').html('库存不足').show().delay(2000).fadeOut(1000);
			return false;
		}
		var number = parseInt($('#J_number').val());
		if (number>stock){
			$('#J_tips').html('库存不足').show().delay(2000).fadeOut(1000);
			return false;
		}
		$.ajax({
			url: base+"/cart/add.do",
			type: "POST",
			dataType: "json",
			data: {skuid:skuid,quantity:number},
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					//$('#J_tips').html('添加成功').show().delay(2000).fadeOut(1000);
					$('#J_btn_tips_message').html('添加成功').parent().parent().show();
				}else if (json.code=="unlogin"){
					location.href = base+"/member/login.htm"
				}else{
					alert(json.message);
				}
			}
		});
	});

	$('#J_btnAdd').click(function() {
		var number = parseInt($('#J_number').val())+1;
		var stock = parseInt($('#J_stock').html());
		number = number>stock ? stock : number;
		number = number>999 ? 999 : number;
		$('#J_number').val(number);
	});
	$('#J_btnDel').click(function() {
		var number = parseInt($('#J_number').val())-1;
		number = number<1 ? 1 : number;
		$('#J_number').val(number);
	});
	$('#J_number').change(function() {
		modifyNumber();
	});
	function modifyNumber(){
		var number = parseInt($('#J_number').val());
		var stock = parseInt($('#J_stock').html());
		if (number>stock){
			$('#J_number').val(stock);
		}
	}

	// 评论
	if (window.SHOP_PRODUCT_COMMENT_ENABLED===true){
		var $pagination = $("#J_comments .pagination");
		if (product.scoreCount<1){
			$pagination.html('<div class="emptyText">暂无评价</div>');
			return;
		}
		var $template = juicer($("#J_comments_tmpl").html()); //仅编译模版暂不渲染，返回一个可重用的编译后的函数.
		var $list = $("#J_comments_ul");
		function loadComments(page){
			page = page || 1;
			$list.html('<img src="'+base+'/resources/common/images/loading.gif" alt="loading">载入中...');
			$.getJSON(base+'/product/comments/json.do',
				{page:page,product_id:product.id},
				function(data){
					$list.html($template.render(data));
					$pagination.pager({ pagenumber: data.pageIndex, pagecount: data.pageCount, buttonClickCallback: function(page){loadComments(page);}, emptyText:'暂无评价' });
				}
			);
		}
		loadComments(1);
	}
});
