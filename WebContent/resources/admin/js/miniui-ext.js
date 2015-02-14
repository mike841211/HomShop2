/**
* jQuery MiniUI 3.0
*
* 封装重用
*
*/
miniExt = {
	showError : function(e) {
		var span = $(e.sender.el).siblings('.errorText');
		if (span.length==0){
			if(e.errorText==""){return;}
			span = $('<span class="errorText"></span>').insertAfter(e.sender.el);
		}else{
			span = span.eq(0);
		}
		span.html(e.errorText);
	},

	/**
	 * 提示消息
	 * @param {String} [title='系统提示'] 消息标题
	 * @param {String} 消息内容
	 * @param {Boolean} [hold=false] 是否手动关闭
	 * @method
	 */
	showMsg : function(title, content, hold){
		switch (arguments.length){
			case 1 :
				content = title;
				title = '<b>系统提示</b>';
				break;
			case 2 :
				if (typeof content=='boolean'){
				hold = content;
				content = title;
				title = '<b>系统提示</b>';
				}
				break;
		}
		if(!miniExt.showMsgBox){
			miniExt.showMsgBox = $('<div id="msg-div"><div>').insertAfter($('body'));
		}
		//var box = ['<div class="msg">',
		//		'<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
		//		'<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', title, '</h3>', content, '</div></div></div>',
		//		'<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
		//		'</div>'].join('');
		var btnClose = true==hold ? '<span class="close"></span>' : '';
		var box = '<div class="msg">'+btnClose+'<h3>' + title + '</h3><p>' + content + '</p></div>';
		var m = $(box).prependTo(miniExt.showMsgBox);
		m.hide();
		if (true==hold){
			m.fadeIn();
			m.on('click',function(){
				//if (this.dom.className=='close'){
					m.slideUp();
				//}
			});
		}else{
			m.fadeIn().delay(1000).slideUp();
		}
	},

	form : {
		reset : function(form) {
			form.reset();
			//var id = form.el.attributes.id.value;
			//document.getElementById(id).reset();
		}
	},

	win : {
		open : function(url,config,callback){
			if ($.type(url)=="string"){ //art.dialog
				return this.artDialogOpen(url,config,callback);
			}

			if ($.type(url)=="object"){ //mini
				if ($.isFunction(config)){
					callback = config;
				}
				config = url;
				return this.miniOpen(config,callback);
			}
		},

		//先不用
		artDialogOpen : function(url,config,callback){
			if ($.isFunction(config)){
				callback = config;
				config = {};
			}else{
				config = config || {};
				callback = callback || function(){};
			}
			var win = art.dialog.open(url, config);
			win.iframe.callback = callback;
			win.iframe.winClose = function(){win.close();};
			win.getIFrameEl = function(){return win.iframe;};
			win.callback = function(fn){win.iframe.callback = fn;};
			return win;
		},

		miniOpen : function(config,callback){
			var win = mini.open(config);
			if ($.isFunction(callback)){
				win.getIFrameEl().callback = callback;
			}
			win.getIFrameEl().winClose = function(){win.destroy();};
			win.callback = function(fn){win.getIFrameEl().callback = fn;};
			return win;
		},

		callback : function(){
			try{
				//frameElement.callback();
				frameElement.callback.apply(this, arguments);
			}catch(e){}
		},

		close : function(action){
			try{
				frameElement.winClose();
			}catch(e){}
			//if (window.CloseOwnerWindow) {return window.CloseOwnerWindow(action);}
			//else {window.close();}
		},

		showImage : function(src,options){
			options = options || {};
			this.open({
				url: src,
				title: "图片",
				bodyStyle:"padding:10px;",
				showMaxButton: true,
				width: options.width || 500,
				height: options.height || 500
			});
		}
	},

	grid : {
		/**
		 * 取得当前Grid选中项，单选
		 * @param 目标grid
		 * @param [field='id'] 目标列
		 * @return
		 * @method
		 */
		getValue : function(grid, field){
			field = field || "id";
			var rows = grid.getSelecteds();
			if (rows.length>0){
				return rows[rows.length-1][field];
			}
			return null;
		},
		/**
		 * 取得当前Grid选中项，多选
		 * @param 目标grid
		 * @param [field='id'] 目标列
		 * @return {Array}
		 * @method
		 */
		getValues : function(grid,field){
			field = field || "id";
			var rows = grid.getSelecteds(),
				values = [];
			for (var i = 0, l = rows.length; i < l; i++) {
				values.push(rows[i][field]);
			}
			return values;
		}
	},

	tree : {
		/**
		 * Tree异步加载数据默认展开状态
		 * @param 目标tree
		 * @method
		 */
		setExpandOnLoad : function(tree){
			tree.on('preload',function(e){
				var expandOnLoad = e.sender.expandOnLoad;
				var nodes = e.data;
				for (var i=0; i<nodes.length; i++){
					nodes[i].expanded = expandOnLoad;
				}
			});
		}
	},

	lastModify : "2013-05-23"
}


$(function(){
	$(document).ajaxError(function(event, jqXHR, ajaxOptions, thrownError){
		alert(thrownError);
	});
	//$(document).ajaxSuccess(function(event, jqXHR, ajaxOptions){
		//console.log("ajaxSuccess");
	//});
	$(document).ajaxComplete(function(event, jqXHR, ajaxOptions){
		//console.log("ajaxComplete");
		//console.log(arguments);
		var message = {
			//UNAUTHORIZED : "没有权限！",
			NEEDLOGIN : "未登入或登入超时，请重新登入！"
		}
		var StatusCode = jqXHR.getResponseHeader("STATUSCODE");
		//console.log(sessionStatus);
		//if(typeof(sessionStatus) != "undefined"){
		//if(StatusCode != null){
			if(StatusCode == "NEEDLOGIN"){
				top.mini.alert(message[StatusCode]);
				/*
				top.art.dialog({
					content: message[StatusCode],
					ok: function () {
						this.close();
					},
					close: function () {
						//top.location.reload();
					}
				});
				*/
			//}else{
			//	alert(message[StatusCode]);
			}
		//}
	});
});

/**
 * 修正Number.toFixed四舍五入不准确问题，>>五舍六入
 * @member window.Number
 * @param {Number} fractionDigits 小数位
 * @return {String}
 */
Number.prototype.toFixed = function(fractionDigits)
{
	//没有对fractionDigits做任何处理，假设它是合法输入
	//return (Math.round(this*Math.pow(10,fractionDigits))/Math.pow(10,fractionDigits)).toString();
	var v = Math.round(this*Math.pow(10,fractionDigits))/Math.pow(10,fractionDigits).toString();
		v = (v+".").split('.');
	for (var i=0,len=fractionDigits-v[1].length;i<len ;i++ ){
		v[1] += '0';
	}
	v = v[0]+'.'+v[1];
	return v;
};
