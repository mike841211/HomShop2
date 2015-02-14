$(function($){
	function filterSearch(prop,value) {
		if (prop=="cid"){ // 点击类别，刷新已加载分类品牌等属性 todo JSON
			if (value==null){
				delete params[prop];
			}else{
				var $tab = $('#tab_'+value);
				if ($tab.size()>0){
					tabsSwiper.swipeTo($tab.eq(0).index());
					return;
				}
				if (params[prop]==value){
					return;
				}
				params[prop] = value;
			}
			params['page'] = 1;
			////删除绑定分类的属性
			//delete params["bid"];
			//// delete ...
			location.href = base+location.pathname+"?"+$.param(params);
		//}else{
			//loadProducts(params);
		}
	}

	$('.btn-filter').click(function(){
		$('#J_filter').addClass('on');
	});
	$('.filter-box-mask,.filter-box-arrow').click(function(){
		$('#J_filter').removeClass('on');
	});

	var $filterBox = $('.filter-box');
	$('.filter-tab',$filterBox).click(function(){
		var $this = $(this);
		if (!$this.hasClass('on')){
			$('.filter-tab,.filter-ul-lst',$filterBox).removeClass('on');
			$this.addClass('on');
			$('.filter-ul-lst',$filterBox).eq($this.index()).addClass('on');
		}
	});
	$('.filter-ul-li > a',$filterBox).click(function(){
		var $this = $(this);
		$this.parent().toggleClass('on');
		$this.parent().siblings('.on').removeClass('on'); // only expand one
	});
	$('.filter-ul-li > div a',$filterBox).click(function(){
		var $this = $(this);
		//if ($this.hasClass('on')){
			//return;
		//}
		var prop = $this.closest('ul').data('prop');
		var value = $this.data('id');
		filterSearch(prop,value);
		$this.closest('ul').find('a.on').removeClass('on');
		$this.addClass('on');
	});

	// filter
	//=======================
	// swiper

	var $container = $('.swiper-container');
	var minHeight = $(document).height()-$container.position().top-$('.footer').height()-10;//foot:margin-top:10
	if(minHeight<429){minHeight=429;}
	$container.css("min-height",minHeight);

	var $tabs = $(".tabs a[id!=J_abtn_filter]");
	var $list = $(".product-lst",$container);
	var tabsSwiper = new Swiper('.swiper-container',{
		speed:500,
		onSlideChangeStart: slideChange
	});
	$tabs.on('touchstart mousedown',function(e){
		e.preventDefault()
		$tabs.filter(".active").removeClass('active');
		$(this).addClass('active');
		tabsSwiper.swipeTo( $(this).index() );
	});
	$tabs.click(function(e){
		e.preventDefault();
	});

	juicer.register('formatMoney', Util.formatFloat); //注册自定义函数
	var $template = juicer($("#J_li").html()); //仅编译模版暂不渲染，返回一个可重用的编译后的函数.
	var $pagination = $(".pagination",$container);
	function loadProducts(index ,params){
		$('.loader').addClass('visible');
		$.ajax({
			url: base+"/product/list/json.do",
			type: "POST",
			dataType: "json",
			data: params,
			cache: false,
			success: function (pager) {
				if (pager.status=="success"){
					$(window).scrollTop($('a[name=listTop]').position().top);
					$list.eq(index).html($template.render(pager));
					$(".lazyImage").lazyImage();
					$container.height($list.eq(index).height()+68);
					$pagination.eq(index).pager({pagenumber: pager.pageIndex,pagecount: pager.pageCount,buttonClickCallback: pageChange});
				}else{
					alert(json.message);
				}
			},
			complete: function () {
				//console.error('.loader visible');
				setTimeout(function(){$('.loader').removeClass('visible');},618);
				//$('.loader').removeClass('visible');
			}
		});
	}
	function pageChange(page){
		var _params = $.extend({}, params);
		_params['page'] = page;
		_params['cid'] = $tabs.filter(".active").data("cid");
		loadProducts(tabsSwiper.activeIndex ,_params);
	}
	function slideChange(swiper){
		$tabs.filter(".active").removeClass('active');
		$tabs.eq(swiper.activeIndex).addClass('active');
		if ($list.eq(swiper.activeIndex).children().size()==0){
			pageChange(1);
		}else{
			$container.height($list.eq(swiper.activeIndex).height()+68);
		}
	}

	// init active slide
	var _activeIndex = $('#tab_'+params.cid).index();
	if (_activeIndex<=0){
		slideChange(tabsSwiper);
	}else{
		tabsSwiper.swipeTo(_activeIndex);
	}

});