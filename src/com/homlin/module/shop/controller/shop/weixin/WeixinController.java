package com.homlin.module.shop.controller.shop.weixin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.controller.BaseController;
import com.homlin.app.exception.MessageException;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.model.TbPluginSignin;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.module.shop.model.TbWxAutoreply;
import com.homlin.module.shop.model.TbWxMenu;
import com.homlin.module.shop.model.TbWxMenuMsg;
import com.homlin.module.shop.model.TbWxMsg;
import com.homlin.module.shop.model.TbWxUser;
import com.homlin.module.shop.plugin.weixin.EventType;
import com.homlin.module.shop.plugin.weixin.KeywordMappingUtil;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.plugin.weixin.msg.MsgType;
import com.homlin.module.shop.plugin.weixin.msg.NewsItem;
import com.homlin.module.shop.plugin.weixin.msg.NewsMsg;
import com.homlin.module.shop.plugin.weixin.msg.TextMsg;
import com.homlin.module.shop.service.MemberOauth2Service;
import com.homlin.module.shop.service.PluginSigninService;
import com.homlin.module.shop.service.ProductService;
import com.homlin.module.shop.service.WxAutoreplyService;
import com.homlin.module.shop.service.WxMenuMsgService;
import com.homlin.module.shop.service.WxMenuService;
import com.homlin.module.shop.service.WxMsgService;
import com.homlin.module.shop.service.WxUserService;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.JacksonUtil;

@Controller
public class WeixinController extends BaseController {

	@Autowired
	WxMenuService wxMenuService;

	@Autowired
	WxMenuMsgService wxMenuMsgService;

	@Autowired
	WxAutoreplyService wxAutoreplyService;

	@Autowired
	ProductService productService;

	@Autowired
	WxUserService wxUserService;

	@Autowired
	WxMsgService wxMsgService;

	@Autowired
	MemberOauth2Service memberOauth2Service;

	@Autowired
	PluginSigninService pluginSigninService;

	@Autowired
	MemberOauth2Service memberOauth2Service2;

	private String fromUserName; // 取得发送者
	private String toUserName; // 取得接收者
	private String msgType;
	private Element root;

	@ResponseBody
	@RequestMapping(value = "/weixin")
	public String execute(HttpServletRequest request) throws Exception {

		// --php转发测试
		// System.err.println(request.getParameterMap().keySet());
		// String xml = null;
		// for (String key : request.getParameterMap().keySet()) {
		// if (key.startsWith("<xml>")) {
		// xml = key;
		// break;
		// }
		// }
		// --php

		String xml = WeixinUtil.readStreamParameter(request.getInputStream());
		if (xml == null || xml.isEmpty()) {
			String echostr = request.getParameter("echostr");
			// 申请消息接口
			if (StringUtils.isNotBlank(echostr)) {
				return echostr;
			} else {
				return noMatchMsg();
			}
		}

		Document document = DocumentHelper.parseText(xml);
		root = document.getRootElement();
		fromUserName = root.elementText("FromUserName");
		toUserName = root.elementText("ToUserName");
		msgType = root.elementText("MsgType");
		// trace(xml);
		if (MsgType.event.name().equals(msgType)) {
			String event = root.elementText("Event");
			if (EventType.CLICK.name().equals(event)) {
				String eventKey = root.elementText("EventKey");
				return click(eventKey);
			} else if (EventType.LOCATION.name().equals(event)) { // 上报地理位置
				// return LOCATION();
				// 获取用户地理位置的方式有两种，
				// 一种是仅在进入会话时上报一次，
				// 一种是进入会话后每隔5秒上报一次。 todo
				// 公众号可以在公众平台网站中设置。
				// saveLocatin();
				return null;
			} else if (EventType.SCAN.name().equals(event)) { // 扫描
				return null;
			} else if (EventType.subscribe.name().equals(event)) {
				return subscribe();
			} else if (EventType.unsubscribe.name().equals(event)) {
				unsubscribe();
				return null;
			}
			// msgType: "text","image","video","voice","location" ,"link"
		} else if (MsgType.text.name().equals(msgType)) {
			String content = root.elementText("Content");
			if (StringUtils.isNotBlank(content)) {
				return autoreply(content);
			}
		} else if (MsgType.voice.name().equals(msgType)) {
			String Recognition = root.elementText("Recognition");
			if (StringUtils.isNotBlank(Recognition)) {
				return autoreply(Recognition);
			}
		} else {
			saveMsg(); // 其他：暂时只保存信息，不返回
		}
		return noMatchMsg();
	}

	// 无匹配回复信息
	private String noMatchMsg() {
		String content = CacheUtil.getConfig(CacheConfigKeys.WEIXIN_MSG_NOMATCH);
		if (StringUtils.isNotBlank(content)) {
			TextMsg msg = new TextMsg();
			msg.setToUserName(fromUserName);
			msg.setFromUserName(toUserName);
			msg.setContent(content);
			return msg.toXmlString();
		}
		return null;
	}

	// 关注
	@SuppressWarnings("unchecked")
	private String subscribe() throws Exception {
		try {
			// 关注时绑定账号
			String result = WeixinUtil.getUserInfo(fromUserName);
			TbWxUser user = JacksonUtil.toObject(result, TbWxUser.class);
			// Map<String, Object> map = JacksonUtil.toObject(result, Map.class);
			// user.setSubscribeTime(Integer.valueOf(map.get("subscribe_time").toString()));
			user.setSubscribeTime(Integer.valueOf(root.elementText("CreateTime")));
			// 查询用户组
			String data = "{\"openid\":\"" + fromUserName + "\"}";
			String result2 = WeixinUtil.getUserGroupId(data);
			Map<String, Integer> map2 = JacksonUtil.toObject(result2, Map.class);
			user.setGroupId(map2.get("groupid"));
			wxUserService.update(user);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String WEIXIN_SUBSCRIBE_MSGTYPE = CacheUtil.getConfig(CacheConfigKeys.WEIXIN_SUBSCRIBE_MSGTYPE);
		if (MsgType.news.name().equals(WEIXIN_SUBSCRIBE_MSGTYPE)) {
			List<TbWxAutoreply> autoreplies = wxAutoreplyService.findByKeyword("SUBSCRIBE", 10);
			if (autoreplies == null || autoreplies.size() == 0) {
			} else {
				return createAutoreplyMessage(autoreplies);
			}
		}
		String content = CacheUtil.getConfig(CacheConfigKeys.WEIXIN_MSG_SUBSCRIBE);
		if (StringUtils.isNotBlank(content)) {
			TextMsg msg = new TextMsg();
			msg.setToUserName(fromUserName);
			msg.setFromUserName(toUserName);
			msg.setContent(content);
			return msg.toXmlString();
		}
		return null;
	}

	// 取消关注
	private void unsubscribe() throws Exception {
		// 取消关注
		TbWxUser user = wxUserService.get(fromUserName);
		if (user != null) {
			user.setSubscribe(0);
			wxUserService.update(user);
		}
	}

	// 点击按钮
	private String click(String eventKey) {
		TbWxMenu menu = wxMenuService.get(eventKey); // toco cache
		if (menu == null) {
			return noMatchMsg();
		}
		String functionKey = menu.getFunctionKey();
		if (StringUtils.isNotBlank(functionKey)) {
			// 特殊按钮
			return specialClick(functionKey);
		}
		if (MsgType.text.name().equals(menu.getMessageType())) {
			String content = wxMenuMsgService.getTextContent(menu.getId());
			if (content != null) {
				TextMsg msg = new TextMsg();
				msg.setToUserName(fromUserName);
				msg.setFromUserName(toUserName);
				msg.setContent(content);
				return msg.toXmlString();
			}
		} else if (MsgType.news.name().equals(menu.getMessageType())) {
			List<TbWxMenuMsg> menuMsgs = wxMenuMsgService.getNews(menu.getId(), 10);
			if (menuMsgs.size() > 0) {
				String url = CacheUtil.getConfig(CacheConfigKeys.SHOP_URL);
				String contextPath = getRequest().getContextPath();
				NewsMsg msg = new NewsMsg();
				msg.setToUserName(fromUserName);
				msg.setFromUserName(toUserName);
				List<NewsItem> items = msg.getArticles();
				for (TbWxMenuMsg menuMsg : menuMsgs) {
					NewsItem item = new NewsItem();
					item.setTitle(menuMsg.getTitle());
					item.setDescription(menuMsg.getSummary());
					if (AppConstants.TRUE.equals(menuMsg.getShowCover())) {
						if (StringUtils.isNotBlank(menuMsg.getCover())) {
							item.setPicUrl(url + menuMsg.getCover());
						}
					}
					if (StringUtils.isEmpty(menuMsg.getLink())) {
						item.setUrl(url + contextPath + "/weixin/menu_msg/" + menuMsg.getId() + ".htm");
					} else {
						item.setUrl(menuMsg.getLink());
					}
					items.add(item);
				}
				msg.setArticles(items);
				return msg.toXmlString();
			}
		}
		return noMatchMsg();
	}

	// 特殊按钮
	private String specialClick(String eventKey) {
		return specialReply(eventKey);
	}

	// 回复信息
	private String autoreply(String keyword) throws Exception {

		// [[ 特殊回复格式
		if (StringUtils.startsWith(keyword, "Q:")) {
			String _keyword = KeywordMappingUtil.get(keyword);
			// trace(keyword, _keyword);
			if (_keyword != null) {
				return specialReply(_keyword);
			}
		}
		// ]]

		// 记录信息
		saveMsg();

		List<TbWxAutoreply> autoreplies = wxAutoreplyService.findByKeyword(keyword, 10);
		if (autoreplies == null || autoreplies.size() == 0) {
			// todo cache null
			return noMatchMsg();
		}
		return createAutoreplyMessage(autoreplies);
	}

	private String createAutoreplyMessage(List<TbWxAutoreply> autoreplies) throws Exception {
		if (autoreplies == null || autoreplies.size() == 0) {
			return null;
		}
		if (autoreplies.size() == 1 && MsgType.text.name().equals(autoreplies.get(0).getReplyType())) {
			// 只有一条并且为text
			TextMsg msg = new TextMsg();
			msg.setToUserName(fromUserName);
			msg.setFromUserName(toUserName);
			msg.setContent(autoreplies.get(0).getSummary());
			return msg.toXmlString();
		} else {
			String url = CacheUtil.getConfig(CacheConfigKeys.SHOP_URL);
			String contextPath = getRequest().getContextPath();
			NewsMsg msg = new NewsMsg();
			msg.setToUserName(fromUserName);
			msg.setFromUserName(toUserName);
			List<NewsItem> items = msg.getArticles();
			for (TbWxAutoreply tbWxAutoreply : autoreplies) {
				NewsItem item = new NewsItem();
				item.setTitle(tbWxAutoreply.getTitle());
				item.setDescription(tbWxAutoreply.getSummary());
				if (AppConstants.TRUE.equals(tbWxAutoreply.getShowCover())) {
					if (StringUtils.isNotBlank(tbWxAutoreply.getCover())) {
						item.setPicUrl(url + tbWxAutoreply.getCover());
					}
				}
				if (StringUtils.isEmpty(tbWxAutoreply.getLink())) {
					item.setUrl(url + contextPath + "/weixin/autoreply/" + tbWxAutoreply.getId() + ".htm");
				} else {
					item.setUrl(tbWxAutoreply.getLink());
				}
				items.add(item);
			}
			msg.setArticles(items);
			return msg.toXmlString();
		}
	}

	// 特殊回复格式
	private String specialReply(String keyword) {

		keyword = keyword.toLowerCase();

		if (StringUtils.startsWithIgnoreCase(keyword, "query_member_score")) { // 查询积分
			return query_member_score(fromUserName);
		} else if (StringUtils.startsWithIgnoreCase(keyword, "query_member_deposit")) { // 查询余额
			return query_member_deposit(fromUserName);
		} else if (StringUtils.startsWithIgnoreCase(keyword, "member_signin")) { // 每日签到
			return member_signin(fromUserName);
		} else if (StringUtils.startsWithIgnoreCase(keyword, "transfer_customer_service")) {
			// 开发模式下，多客服接入指南
			// 在新的微信协议中，开发模式也可以接入客服系统。 开发者如果需要使用客服系统，需要在接收到用户发送的消息时，
			// 返回一个MsgType为transfer_customer_service的消息，微信服务器在收到这条消息时，会把用户这次发送的和以后一段时间内发送的消息转发客服系统。
			StringBuilder sb = new StringBuilder();
			sb.append("<xml>");
			sb.append("<ToUserName>" + fromUserName + "</ToUserName>");
			sb.append("<FromUserName>" + toUserName + "</FromUserName>");
			sb.append("<CreateTime>" + (new Date().getTime() / 1000) + "</CreateTime>");
			sb.append("<MsgType><![CDATA[transfer_customer_service]]></MsgType>");
			sb.append("</xml>");
			return sb.toString();
		} else if (StringUtils.startsWithIgnoreCase(keyword, "query_product_")) {
			// 查询商品:
			String _keyword = null;
			if (keyword.equals("query_product_new")) {
				_keyword = "isNew";
			} else if (keyword.equals("query_product_hot")) {
				_keyword = "isHot";
			} else if (keyword.equals("query_product_promotion")) {
				_keyword = "isPromotion";
			} else if (keyword.equals("query_product_recomend")) {
				_keyword = "isRecomend";
			}
			if (_keyword == null) {
				return null;
			}
			return specialProduct(_keyword);
		}

		return null;
	}

	// 记录信息
	private void saveMsg() throws Exception {
		TbWxMsg tbWxMsg = new TbWxMsg();
		// String msgId = root.elementText("MsgId");
		// if (msgId == null) {
		// msgId = UUID.randomUUID().toString();
		// }
		// tbWxMsg.setMsgId(msgId);
		tbWxMsg.setMsgId(root.elementText("MsgId"));
		tbWxMsg.setFromUserName(fromUserName);
		tbWxMsg.setToUserName(toUserName);
		tbWxMsg.setCreateTime(Integer.valueOf(root.elementText("CreateTime")));
		tbWxMsg.setMsgType(msgType);
		if (MsgType.text.name().equals(msgType)) {
			tbWxMsg.setContent(root.elementTextTrim("Content"));
		} else if (MsgType.image.name().equals(msgType)) {
			tbWxMsg.setMediaId(root.elementTextTrim("MediaId"));
			tbWxMsg.setPicUrl(root.elementTextTrim("PicUrl"));
		} else if (MsgType.voice.name().equals(msgType)) {
			tbWxMsg.setMediaId(root.elementTextTrim("MediaId"));
			tbWxMsg.setFormat(root.elementTextTrim("Format"));
			tbWxMsg.setRecognition(root.elementTextTrim("Recognition"));
		} else if (MsgType.video.name().equals(msgType)) {
			tbWxMsg.setMediaId(root.elementTextTrim("MediaId"));
			tbWxMsg.setThumbMediaId(root.elementTextTrim("ThumbMediaId"));
		} else if (MsgType.location.name().equals(msgType)) {
			tbWxMsg.setMediaId(root.elementTextTrim("MediaId"));
			tbWxMsg.setLocationX(new BigDecimal(root.elementTextTrim("Location_X")));
			tbWxMsg.setLocationY(new BigDecimal(root.elementTextTrim("Location_Y")));
			tbWxMsg.setScale(Integer.valueOf(root.elementTextTrim("Scale")));
			tbWxMsg.setLabel(root.elementTextTrim("Label"));
		} else if (MsgType.link.name().equals(msgType)) {
			tbWxMsg.setTitle(root.elementTextTrim("Title"));
			tbWxMsg.setDescription(root.elementTextTrim("Description"));
			tbWxMsg.setUrl(root.elementTextTrim("Url"));
			// } else if (MsgType.event.name().equals(msgType)) {
			// String event = root.elementText("Event");
			// if (EventType.LOCATION.name().equals(event)) { // 上报地理位置
			// tbWxMsg.setMediaId(root.elementTextTrim("MediaId"));
			// tbWxMsg.setLocationX(new BigDecimal(root.elementTextTrim("Location_X")));
			// tbWxMsg.setLocationY(new BigDecimal(root.elementTextTrim("Location_Y")));
			// tbWxMsg.setScale(Integer.valueOf(root.elementTextTrim("Scale")));
			// tbWxMsg.setLabel(root.elementTextTrim("Label"));
			// }
		}

		try {
			wxMsgService.save(tbWxMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ===

	// 返回特殊商品信息
	private String specialProduct(String keyword) {
		List<TbShopProduct> products = productService.getProductForWeixinReply(keyword, 10); // todo cache
		if (products == null || products.size() < 1) {
			TextMsg msg = new TextMsg();
			msg.setToUserName(fromUserName);
			msg.setFromUserName(toUserName);
			msg.setContent("对不起，没有匹配信息");
			return msg.toXmlString();
		}

		String url = CacheUtil.getConfig(CacheConfigKeys.SHOP_URL);
		String contextPath = getRequest().getContextPath();
		NewsMsg msg = new NewsMsg();
		msg.setToUserName(fromUserName);
		msg.setFromUserName(toUserName);
		List<NewsItem> items = msg.getArticles();
		for (TbShopProduct product : products) {
			NewsItem item = new NewsItem();
			item.setTitle(product.getName());
			item.setDescription(product.getSn() + "\n" + product.getSlogan());
			item.setPicUrl(url + product.getSampleImage());
			item.setUrl(url + contextPath + "/product/item/" + product.getId() + ".htm");
			items.add(item);
		}
		msg.setArticles(items);
		return msg.toXmlString();
	}

	// 查询积分
	private String query_member_score(String openid) {
		TextMsg msg = new TextMsg();
		msg.setToUserName(fromUserName);
		msg.setFromUserName(toUserName);
		TbShopMember tbShopMember = memberOauth2Service.getTbShopMember(openid);
		if (null == tbShopMember) {
			msg.setContent("查询失败：当前微信号未绑定会员");
			return msg.toXmlString();
		} else {
			String content = "您的账号累计积分\ue12f%1$,.0f\n当前可用积分为\ue12f%2$,.0f";
			content = String.format(content, tbShopMember.getScoreAddup(), tbShopMember.getScore());
			msg.setContent(content);
		}

		return msg.toXmlString();
	}

	// 查询余额
	private String query_member_deposit(String openid) {
		TextMsg msg = new TextMsg();
		msg.setToUserName(fromUserName);
		msg.setFromUserName(toUserName);
		TbShopMember tbShopMember = memberOauth2Service.getTbShopMember(openid);
		if (null == tbShopMember) {
			msg.setContent("查询失败：当前微信号未绑定会员");
			return msg.toXmlString();
		} else {
			String content = "您的账号累计存款\ue12f%1$,.2f元\n当前可用余额为\ue12f%2$,.2f元";
			content = String.format(content, tbShopMember.getDepositAddup(), tbShopMember.getDeposit());
			msg.setContent(content);
		}

		return msg.toXmlString();
	}

	// 微信签到
	private String member_signin(String openid) {
		TextMsg msg = new TextMsg();
		msg.setToUserName(fromUserName);
		msg.setFromUserName(toUserName);
		TbShopMember tbShopMember = memberOauth2Service.getTbShopMember(openid);
		if (null == tbShopMember) {
			msg.setContent("签到失败：当前微信号未绑定会员");
			return msg.toXmlString();
		} else if (!AppConstants.TRUE.equals(tbShopMember.getEnabled())) {
			msg.setContent("签到失败：当前账号帐号已禁用");
			return msg.toXmlString();
		}
		try {
			Map<String, Object> map = pluginSigninService.signin(tbShopMember);
			BigDecimal score = (BigDecimal) map.get("score");
			TbPluginSignin tbPluginSignin = tbShopMember.getTbPluginSignin();
			String content = "恭喜，签到成功！\n本次签到获得 %1$,.0f 积分，\n" +
					"您已连续签到 %2$d 天，\n您的账号累计积分\ue12f%4$,.0f" +
					"\n当前可用积分为\ue12f%5$,.0f";
			content = String
					.format(content, score, tbPluginSignin.getDates(), tbPluginSignin.getDatesAddup()
						, tbShopMember.getScoreAddup(), tbShopMember.getScore());
			msg.setContent(content);
			return msg.toXmlString();
		} catch (MessageException e) {
			msg.setContent("签到失败：" + e.getMessage());
			return msg.toXmlString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
