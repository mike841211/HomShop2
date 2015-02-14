// 系统配置：页面模板
var URLDATA = [
	{value:'/',text:'首页'},
	{value:'/cart/list.htm',text:'购物车'},
	{value:'/member/index.htm',text:'会员中心'},
	{value:'/order/list.htm',text:'我的订单'},
	{value:'/category/list.htm',text:'商品分类'},
	{value:'/product/list.htm',text:'全部商品列表'},
	{value:'/product/list.htm?cid={分类ID}',text:'商品列表：指定分类'},
	{value:'/product/search.htm?s={关键字}',text:'商品搜索'},
	{value:'/product/search.htm?cid={分类ID}',text:'商品搜索：指定分类'},
	{value:'/product/search.htm?bid={品牌ID}',text:'商品搜索：指定品牌'},
	{value:'/product/search.htm?isNew=1',text:'商品搜索：新品'},
	{value:'/product/search.htm?isHot=1',text:'商品搜索：热销'},
	{value:'/product/search.htm?isPromotion=1',text:'商品搜索：促销'},
	{value:'/product/search.htm?isRecomend=1',text:'商品搜索：推荐'},
	{value:'/article/list.htm',text:'新闻资讯'},
	{value:'/article/list.htm?cid={分类ID}',text:'新闻资讯：指定分类'},
	{value:'/article/view/{文章ID}.htm',text:'新闻资讯：指定文章'},
	{value:'/page/{页面标志}.htm',text:'广告自定义页面'},
	{value:'https://mp.weixin.qq.com/payfb/payfeedbackindex?appid={公众号AppId}#wechat_webview_type=1&wechat_redirect',text:'微信维权 (替换{公众号AppId})'},
	{value:'',text:'其他'}
];
