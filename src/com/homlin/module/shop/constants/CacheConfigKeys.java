package com.homlin.module.shop.constants;

public interface CacheConfigKeys {
	String EMAIL_ADDRESS = "EMAIL_ADDRESS";// EMAIL发送邮箱
	String EMAIL_HOST = "EMAIL_HOST";// EMAIL发件服务器
	String EMAIL_PASSWORD = "EMAIL_PASSWORD";// EMAIL密码
	String EMAIL_PORT = "EMAIL_PORT";// EMAIL端口
	String EMAIL_USERNAME = "EMAIL_USERNAME";// EMAIL用户名
	String MEMBER_ENABLED = "MEMBER_ENABLED";// 新注册用户默认状态
	String MEMBER_LOGIN_CARDNO = "MEMBER_LOGIN_CARDNO";// 开启会员卡号登入
	String MEMBER_LOGIN_EMAIL = "MEMBER_LOGIN_EMAIL";// 开启电子邮箱登入
	String MEMBER_LOGIN_MOBILE = "MEMBER_LOGIN_MOBILE";// 开启手机登入
	String MEMBER_LOGIN_USERNAME = "MEMBER_LOGIN_USERNAME";// 开启会员用户名登入
	String MEMBER_REQUIRED_EMAIL = "MEMBER_REQUIRED_EMAIL";// 电子邮箱注册必填
	String MEMBER_REQUIRED_MOBILE = "MEMBER_REQUIRED_MOBILE";// 手机注册必填
	String PLUGIN_SIGNIN_BASESCORE = "PLUGIN_SIGNIN_BASESCORE";// 签到基础得分
	String PLUGIN_SIGNIN_DATES = "PLUGIN_SIGNIN_DATES";// 最高加倍天数n，签到基础得分*连续天数n
	String SHOP_CAPTCHA = "SHOP_CAPTCHA";// 默认通用验证码
	String SHOP_CART_NEED_LOGIN = "SHOP_CART_NEED_LOGIN";// 使用购物车要求登入
	String SHOP_CERTTEXT = "SHOP_CERTTEXT";// 备案号
	String SHOP_COPYRIGHT = "SHOP_COPYRIGHT";// 版权信息
	String SHOP_DEFAULTKEY = "SHOP_DEFAULTKEY";// 默认搜索关键词
	String SHOP_HOTKEYS = "SHOP_HOTKEYS";// 热门搜索关键词
	String SHOP_INTRODUCTION_LAZY = "SHOP_INTRODUCTION_LAZY";// 延迟加载商品描述
	String SHOP_ISSHOWMARKETPRICE = "SHOP_ISSHOWMARKETPRICE";// 是否前台显示市场价
	String SHOP_METADESCRIPTION = "SHOP_METADESCRIPTION";// 首页页面描述
	String SHOP_METAKEYWORDS = "SHOP_METAKEYWORDS";// 首页页面关键词
	String SHOP_NAME = "SHOP_NAME";// 网店名称
	String SHOP_ORDER_NEED_LOGIN = "SHOP_ORDER_NEED_LOGIN";// 下单时要求登入
	String SHOP_ORDER_USESCORE = "SHOP_ORDER_USESCORE";// 开启订单积分抵扣
	String SHOP_PRODUCT_COMMENT_DEFAULTSHOW = "SHOP_PRODUCT_COMMENT_DEFAULTSHOW";// 商品评价默认是否通过审核
	String SHOP_PRODUCT_COMMENT_ENABLED = "SHOP_PRODUCT_COMMENT_ENABLED";// 开启商品评价
	String SHOP_PRODUCT_ITEM_IMAGE_MODE = "SHOP_PRODUCT_ITEM_IMAGE_MODE";// 商品详情轮播图模式： ALL 自动缩放,HIDDEN 宽度100% 高度隐藏
	String SHOP_PRODUCT_ITEM_IMAGE_HEIGHT = "SHOP_PRODUCT_ITEM_IMAGE_HEIGHT";// 商品详情轮播图高度
	String SHOP_QQ = "SHOP_QQ";// 客服QQ
	String SHOP_SCORE_ORDERCREDIT_MULTIPLE = "SHOP_SCORE_ORDERCREDIT_MULTIPLE";// 订单消费抵扣积分倍数，1分钱=N积分
	String SHOP_SCORE_ORDERGAIN_MULTIPLE = "SHOP_SCORE_ORDERGAIN_MULTIPLE";// 订单消费获得积分倍数，1分钱=N积分
	String SHOP_SHOWOUTOFSTOCK = "SHOP_SHOWOUTOFSTOCK";// 显示零库存商品
	String SHOP_URL = "SHOP_URL";// 网店网址
	String TEMPLATE_MOBILE = "TEMPLATE_MOBILE";// WAP模板
	String TEMPLATE_MOBILE_CATELIST = "TEMPLATE_MOBILE_CATELIST";// WAP分类列表模板
	String TEMPLATE_MOBILE_INDEX = "TEMPLATE_MOBILE_INDEX";// WAP首页模板
	String TEMPLATE_MOBILE_INDEX_BGIMG = "TEMPLATE_MOBILE_INDEX_BGIMG";// 首页背景图：建议640*960
	String TEMPLATE_MOBILE_PRODLIST = "TEMPLATE_MOBILE_PRODLIST";// WAP商品列表模板
	String TEMPLATE_PC = "TEMPLATE_PC";// PC模板
	String WEIXIN_APPID = "WEIXIN_APPID";// 微信公众号开发者凭据AppId
	String WEIXIN_APPSECRET = "WEIXIN_APPSECRET";// 微信公众号开发者凭据AppSecret
	String WEIXIN_HIDETOOLBAR = "WEIXIN_APPSECRET";// 隐藏微信中网页底部导航栏
	String WEIXIN_MSG_NOMATCH = "WEIXIN_MSG_NOMATCH";// 无匹配回复信息
	String WEIXIN_MSG_SUBSCRIBE = "WEIXIN_MSG_SUBSCRIBE";// 微信关注欢迎信息
	String WEIXIN_QRCODE = "WEIXIN_MSG_SUBSCRIBE";// 微信推广二维码
	String WEIXIN_SUBSCRIBE_MSGTYPE = "WEIXIN_SUBSCRIBE_MSGTYPE";// 关注时回复信息类型：text,news
	String WEIXIN_TOKEN = "WEIXIN_TOKEN";// 微信公众号服务器配置Token
}
