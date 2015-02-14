
Util = {
	getUrlParam : function (key){
		var s = location.search.substr(1).split("&");
		for (var i=0,len=s.length; i<len; i++){
			var a = s[i].split('=');
			if(a[1] && a[0].toLowerCase()==key.toLowerCase()){
				return a[1];
			}
		}
		return null;
	},
	//getUrlParam : function (key){
	//	var reg = new RegExp("(^|&)"+ key +"=([^&]*)(&|$)");
	//	var m = window.location.search.substr(1).match(reg);
	//	if (m!=null){return unescape(m[2]);}
	//	return null;
	//},
	// 逗号格式化数字;
	formatInt : function (num){
		num = num.toString();
		var re=/(-?\d+)(\d{3})/;
		while(re.test(num)){
			num = num.replace(re,"$1,$2");
		}
		return num;
	},
	//逗号格式化数字;
	formatFloat : function (num){
		num = (num+".").split('.');
		var n1 = num[0],n2 = num[1];
		var re=/(-?\d+)(\d{3})/
		while(re.test(n1)){
			n1 = n1.replace(re,"$1,$2")
		}
		//if (n2!=""){n2="."+n2;}//还原小数点
		//n2 = n2==""?".00":"."+n2;
		if (n2==""){n2=".00";}
		else if(n2.length<2){n2="."+n2+"0";}
		else {n2="."+n2;}
		return n1+n2;
	},
	//验证Email格式;
	isEmail : function (s){
		//s = trim(s);
		var re = /^[_\.0-9a-z-]+@([0-9a-z][0-9a-z-]+\.){1,4}[a-z]{2,3}$/i;
		return re.test(s);
	},
	//验证手机号码格式;
	isMobile : function (s){
		var re = /^(0)?1[3|5|8]\d{9}$/;
		return re.test(s);
	},
	//isDate;
	isDate : function (s){
		var re = /^((\d{2}(([02468][048])|([13579][26]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|([1-2][0-9])))))|(\d{2}(([02468][1235679])|([13579][01345789]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\s((([01]?[0-9])|(2[0-3]))\:([0-5]?[0-9])((\s)|(\:([0-5]?[0-9])))))?$/;
		return re.test(s);
	},
	//只能是[A-Za-z0-9_];
	isAlphanumeric : function (s){
		var re = /\W+/;
		return !re.test(s);
	},
	//isDouble;
	isDouble : function (s){
		var re = /^[-\+]?\d+(\.\d+)?$/;
		return re.test(s);
	},
	//isInteger;
	isInteger : function (s){
		var re = /^[-\+]?\d+$/;
		return re.test(s);
	},
	//过滤出整数;
	filterInt : function (s){
		return s.replace(/[^\d]/g,'');
	},
	//过滤出浮点数;
	filterDouble : function (s){
		return s.replace(/([^\d.])|([.]+[\d]*[.])/g,'');
	},
	//电话手机格式;
	isTel : function (s){
		var re = /^(\d{3,4})?(-)?\d{7,8}$/;
		return re.test(s);
	},
	//身份证号;
	isIDCard : function (s){
		var re = /^(\d{15}|\d{17}[\d|x|X])$/;
		return re.test(s);
	},
	//网址;
	isUrl : function (s){
		var re = /^(((http|https):(\/\/|\\\\))?(([A-Za-z0-9_-])+[.])+([A-Za-z]){2,3}((\/|\\)(.)*)?)$/;
		return re.test(s);
	},
	//isQQ;
	isQQ : function (s){
		var re = /^(\d{5,10})$/;
		return re.test(s);
	}
}

Shop = {
//	// 高亮搜索词
//	search_hightlight : function (keys, selector){
//		if ($.trim(keys)!=''){
//			var values = keys.split(" ");
//			var re = [];
//			for (var i=0; i<values.length; i++){
//				if ($.trim(values[i])!=''){
//					re.push('('+values[i]+')');
//				}
//			}
//			if (re.length>0){
//				re = eval('/('+re.join('|')+')/ig');
//				selector = selector || '.goods_grid .pro_list_name a';
//				$(selector).each(function(i,dom){
//					$(dom).html(dom.innerText.replace(re,'<em>$1</em>'));
//				});
//			}
//		}
//	},
//	//设置jQuery.cookie默认值
//	cookie : function (key, value, options){
//		if (arguments.length > 1 && String(value) !== "[object Object]") {
//			options = jQuery.extend({domain:Shop.domain,path:"/"}, options);
//			return jQuery.cookie(key, value, options);
//		}else{
//			return jQuery.cookie(key);
//		}
//	},
//	// Cookie购物车数量
//	getCookieCartTotalQuantity : function (){
//		var COOKIE_CART_TOTALQUANTITY = parseInt(Shop.cookie('CART_TOTALQUANTITY'));
//		if (isNaN(COOKIE_CART_TOTALQUANTITY) || COOKIE_CART_TOTALQUANTITY<0){ COOKIE_CART_TOTALQUANTITY = -1; }
//		if (COOKIE_CART_TOTALQUANTITY==-1){
//			$.getJSON("/cartSimpleData.do",function(json){
//				if (json.status=="success"){
//					Shop.cookie('CART_TOTALQUANTITY',json.totalQuantity)
//				}else{
//					alert('error');
//				}
//			});
//		}
//		return COOKIE_CART_TOTALQUANTITY; // 正在读取返回-1
//	},
//	// 购物车数量变化
//	cartQuantityChange : function (change){
//		var totalQuantity;
//		if (change==0){//清空购物车
//			totalQuantity = 0;
//		}else{//变化数量
//			totalQuantity = this.getCookieCartTotalQuantity();
//			if (totalQuantity==-1){
//				setTimeout(function(){Shop.cartQuantityChange(change);},500);
//				return;
//			}
//			totalQuantity += change;
//		}
//		Shop.cookie('CART_TOTALQUANTITY',totalQuantity);
//		this.showCartTotalQuantity();//更新显示
//	},
//	// 显示购物车数量
//	showCartTotalQuantity : function (){
//		var totalQuantity;
//		totalQuantity = this.getCookieCartTotalQuantity();
//		if (totalQuantity==-1){
//			setTimeout(function(){Shop.showCartTotalQuantity();},500);
//			return;
//		}
//		$("#mycart-amount").html(Util.formatInt(totalQuantity));
//	},
//	// 清空浏览历史
//	clearHistory : function (){
//		if (confirm('您确定要清空浏览记录吗？')){
//			Shop.cookie('history', null, { expires: -1, path: '/' });
//			$('#history_list').html('<li style="text-align:center;margin-bottom:10px;">您已清空最近浏览过的商品</li>');
//		}
//	},
	buildLocationParam : function (key,value){
		var a = [];
		var s = location.search.substr(1).split("&");
		for (var i=0,len=s.length; i<len; i++){
			var _key = s[i].split('=')[0].toLowerCase();
			if(_key!="" && _key!=key && _key!="page"){
				a.push(s[i]);
			}
		}
		if (value && value!=""){
			a.push(key+"="+value);
		}
		var search = a.join("&");
		search = search=="" ? "" : '?'+search;
		return location.pathname + search + location.hash;
	},
	buildPagerLocation : function (page){
		page = parseInt(page) || 1;
		return Shop.buildLocationParam("page",page);
	},
	pageChange : function (page){
		location.href = Shop.buildPagerLocation(page);
	},
	// 登入后跳转
	loginCallbackRedirect : function (returnurl,base){
		var reg = /.*\/(login||logout||register)(\.(d\o||htm))$/ig;
		if (returnurl=='' || reg.test(returnurl)){
			if (reg.test(top.location.pathname)){
				location.href = base;
			}else{
				location.reload();
			}
		}else{
			location.href = returnurl;
		}


//	// 查询IP来源信息，并保存到COOKIES
//	getIpInfo : function (ip){
//		ip = ip || "";
//		var IP = Shop.cookie("IP");
//		if (ip=="" || ip!=IP){
//			$.getScript("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip="+ip,function(){
//				//if(remote_ip_info.ret!=1){return;}
//				if (ip!=""){
//				Shop.cookie("IP", ip, { expires: 7 });
//				}
//				Shop.cookie("IP_Country", remote_ip_info.country, { expires: 7 });
//				Shop.cookie("IP_Province", remote_ip_info.province, { expires: 7 });
//				Shop.cookie("IP_City", remote_ip_info.city, { expires: 7 });
//				Shop.cookie("IP_District", remote_ip_info.district, { expires: 7 });
//			});
//			return false;
//		}else{
//			return true;
//		}
//	},
//	// IP来源省份 - deprecated
//	getIpProvince : function (ip){
//		var IP_Province = Shop.cookie("IP_Province");
//		if (!IP_Province){
//			Shop.getIpInfo(ip);
//			return null;
//		}else{
//			return IP_Province;
//		}
	}
}

//function search() {
//    var s = document.getElementById("search_key").value;
//    if (s == "") {return;}
//    s = s.replace(/#/g, "%23").replace(/\+/g, "%2b");
//    var st = document.getElementById("search_type").value;
//	//var hostname = location.hostname;
//    var url = Shop.url+"/goods-list.htm";
//	//if (hostname==Shop.domain || hostname=='www.'+Shop.domain){
//	//	url = "/goods-list.htm";
//	//}else{
//	//	url = "/brand-goods-list.htm";
//	//}
//    url += "?st=" + st + "&s=" + s;
//    setTimeout(function() {
//        window.location.href = url;
//    }, 10);
//}
//function login(){location.href=Shop.url+"/login.htm?from="+escape(location.href);return false;}
////function login(){
////	var dialog = art.dialog({id: 'N3690',title: false});
////	// jQuery ajax
////	$.ajax({
////		url: '/shop/login.html',
////		success: function (data) {
////			dialog.content(data);
////		},
////		cache: false
////	});
////	//art.dialog.open("login.htm");
////	return false;
////}
//function regist(){location.href=Shop.url+"/register.htm?from="+escape(location.href);return false;}
//function addToFavorite(a,b){if(document.all){window.external.AddFavorite(a,b)}else if(window.sidebar){window.sidebar.addPanel(b,a,"")}else{alert("对不起，您的浏览器不支持此操作!\n请您使用菜单栏或Ctrl+D收藏本站。")}}

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
