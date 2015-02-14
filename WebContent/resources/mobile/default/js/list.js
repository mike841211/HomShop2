$(function($){
	juicer.register('formatMoney', Util.formatFloat); //注册自定义函数
	var $template = juicer($("#J_li").html()); //仅编译模版暂不渲染，返回一个可重用的编译后的函数.
	var $pagination = $(".pagination");
	var $list = $(".product-lst");
	function loadProducts(params){
		$('#J_tips').html('Loading...').show();
		$.ajax({
			url: base+"/product/list/json.do",
			type: "POST",
			dataType: "json",
			data: params,
			cache: false,
			success: function (pager) {
				$('#J_tips').fadeOut(500);
				if (pager.status=="success"){
					$(window).scrollTop($('a[name=listTop]').position().top);
					$list.html($template.render(pager));
					$(".lazyImage").lazyImage();
					$pagination.pager({pagenumber: pager.pageIndex,pagecount: pager.pageCount,buttonClickCallback: pageChange});
				}else{
					alert(json.message);
				}
			}
		});
	}
	function pageChange(page){
		params['page'] = page;
		loadProducts(params);
	}
	//加载数据
	loadProducts(params);

	function filterSearch(prop,value) {
		if (params[prop]==value){
			return;
		}
		if (value==null){
			delete params[prop];
		}else{
			params[prop] = value;
		}
		params['page'] = 1;
		if (prop=="cid"){ // 点击类别，刷新已加载分类品牌等属性 todo JSON
			//删除绑定分类的属性
			delete params["bid"];
			// delete ...
			location.href = base+location.pathname+"?"+$.param(params);
		}else{
			loadProducts(params);
		}
	}

	/* 可组合
	$('.search-lst-navbar .btn').click(function(){
		var $this = $(this);
		var prop = $this.data('prop');
		var value = null;
		if ($this.hasClass('active')){
			$this.removeClass('active');
		}else{
			$this.addClass('active');
			value = '1';
		}
		filterSearch(prop,value);
	});
	*/
	$('.search-lst-navbar .btn').click(function(){
		var $this = $(this);
		// --
		$this.siblings('.btn').each(function(){
			var $this = $(this);
			var prop = $this.data('prop');
			$this.removeClass('active');
			delete params[prop];
		});
		// --
		var prop = $this.data('prop');
		var value = null;
		if ($this.hasClass('active')){
			$this.removeClass('active');
		}else{
			$this.addClass('active');
			value = '1';
		}
		filterSearch(prop,value);
	});

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
		if ($this.hasClass('on')){
			return;
		}
		var prop = $this.closest('ul').data('prop');
		var value = $this.data('id');
		filterSearch(prop,value);
		$this.closest('ul').find('a.on').removeClass('on');
		$this.addClass('on');
	});
});