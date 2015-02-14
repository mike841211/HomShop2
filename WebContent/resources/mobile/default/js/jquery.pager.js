/*
*
* Usage: .pager({ pagenumber: 1, pagecount: 15, buttonClickCallback: PagerClickTest });
*
* Where pagenumber is the visible page number
*       pagecount is the total number of pages to display
*       buttonClickCallback is the method to fire when a pager button is clicked.
*
* buttonClickCallback signiture is PagerClickTest = function(pageclickednumber)
* Where pageclickednumber is the number of the page clicked in the control.
* 2014-03-21 by paio
*/
;(function($) {

    $.fn.pager = function(options) {

        var opts = $.extend({}, $.fn.pager.defaults, options);
		if (opts.pagecount==0){ // empty result
			opts.pagenumber = 0;
		}

		return this.each(function() {
			var $this = $(this);
			$(this).empty();
			if (opts.pagecount==0){$this.html('<div class="emptyText">'+opts.emptyText+'</div>');return;}
			var html = [];
			html.push('<div class="tbl">');
			html.push('<div class="tbl-cell">');
			if (opts.pagenumber<=1){
				html.push('<span class="prev"><span>上一页</span></span>');
			}else{
				html.push('<a href="javascript:void(0)" class="prev"><span>上一页</span></a>');
			}
			html.push('</div>');

			html.push('<div class="tbl-cell pages"><div class="page"><span class="page-open">'+opts.pagenumber+'/'+opts.pagecount+'</span></div>');
			if (opts.pagecount>1){
				html.push('<select class="page-select">');
				for (var i=1; i<=opts.pagecount; i++){
					html.push('<option value="'+i+'">第'+i+'页</option>');
				}
				html.push('</select>');
			}
			html.push('</div>');

			html.push('<div class="tbl-cell">');
			if (opts.pagenumber>=opts.pagecount){
				html.push('<span class="next"><span>下一页</span></span>');
			}else{
				html.push('<a href="javascript:void(0)" class="next"><span>下一页</span></a>');
			}
			html.push('</div>');

			$this.html(html.join(''));

			$('a.prev',$this).click(function(){
				var pagenumber = opts.pagenumber-1;
				pagenumber = pagenumber<1 ? 1 : pagenumber;
				opts.buttonClickCallback(pagenumber);
			});
			$('a.next',$this).click(function(){
				var pagenumber = opts.pagenumber+1;
				pagenumber = pagenumber>opts.pagecount ? opts.pagecount : pagenumber;
				opts.buttonClickCallback(pagenumber);
			});
			$('select',$this).val(opts.pagenumber).change(function(){opts.buttonClickCallback(parseInt(this.value));});
        });
    };

    // pager defaults. hardly worth bothering with in this case but used as placeholder for expansion in the next version
    $.fn.pager.defaults = {
        emptyText: "没有数据",
        pagenumber: 1,
        pagecount: 1
    };

})(jQuery);
