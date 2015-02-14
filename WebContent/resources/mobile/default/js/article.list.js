$(function($){
	var $template = juicer($("#J_li").html()); //仅编译模版暂不渲染，返回一个可重用的编译后的函数.
	var $pagination = $(".pagination");
	var $list = $(".article-lst");
	function loadArticles(params){
		$.ajax({
			url: base+"/article/list/json.do",
			type: "POST",
			dataType: "json",
			data: params,
			cache: false,
			success: function (pager) {
				if (pager.status=="success"){
					$(window).scrollTop($('a[name=listTop]').position().top);
					$list.html($template.render(pager));
					$pagination.pager({pagenumber: pager.pageIndex,pagecount: pager.pageCount,buttonClickCallback: pageChange});
				}else{
					alert(json.message);
				}
			}
		});
	}
	function pageChange(page){
		params['page'] = page;
		loadArticles(params);
	}
	//加载数据
	loadArticles(params);

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
		loadArticles(params);
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
		if ($this.hasClass('on')){
			return;
		}
		var prop = $this.closest('ul').data('prop');
		var value = $this.data('id');
		filterSearch(prop,value);
		$this.closest('ul').find('a.on').removeClass('on');
		$this.addClass('on');
		// --
		$this.closest('.filter-ul-li').siblings().find('.on').removeClass('on');
	});
});