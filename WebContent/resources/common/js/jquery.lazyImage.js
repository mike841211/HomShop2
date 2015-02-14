/*
* jQuery lazyImage plugin by paio
* Version 1.0 (2014-04-10)
*/
;(function($) {

    $.fn.lazyImage = function(options) {

        var settings = {
            event    : "scroll",
            selector : this.selector || ".lazyImage",
            selectorBg : ".lazyImageBg",
            clsLoaded : "loaded",
            delay    : 300
        };

        if(options) {
            $.extend(settings, options);
        };

		function belowthefold(element) {
			var fold = $(window).height() + $(window).scrollTop();
			return fold <= $(element).offset().top;
		};

		function abovethetop(element) {
			var top = $(window).scrollTop();
			return top >= $(element).offset().top + $(element).height();
		};

		/*
		*判断元素是否出现在viewport中 依赖于上两个扩展方法
		*/
		function isInViewport(element) {
			return !belowthefold(element) && !abovethetop(element);
		};

		function loadImg(img) {
			img = img.filter('img[_src]');
			if (img.length>0) {
				var src = img.attr('_src');
				img.closest(settings.selectorBg).removeClass(settings.clsLoaded);
				img.attr('src', src).load(function () {
					img.closest(settings.selectorBg).addClass(settings.clsLoaded);
					img.removeAttr('_src');
				});
			}
		};

		function getInViewportList() {
			$(settings.selector).each(function (i) {
				var img = $(this);
				if (isInViewport(img)) {
					loadImg(img);
				}
			});
		};

		var refreshTimer = null;
		$(window).on(settings.event, function () {
			if (refreshTimer) {
				clearTimeout(refreshTimer);
				refreshTimer = null;
			}
			refreshTimer = setTimeout(getInViewportList, settings.delay);
		});

		getInViewportList();
        return this;
        //return this.each(function() {
            //var $this = $(this);
				//if (isInViewport($this)) {
					//loadImg($this);
				//}
		//});
    };
})(jQuery);