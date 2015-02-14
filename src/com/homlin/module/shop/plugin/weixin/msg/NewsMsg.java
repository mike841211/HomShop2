package com.homlin.module.shop.plugin.weixin.msg;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class NewsMsg extends BaseMsg {

	// ArticleCount 是 图文消息个数，限制为10条以内
	// Articles 是 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
	private List<NewsItem> articles = new ArrayList<NewsItem>();

	public NewsMsg() {
		setMsgType(MsgType.news);
		setCreateTime(String.valueOf(System.currentTimeMillis()));
	}

	public List<NewsItem> getArticles() {
		return articles;
	}

	public void setArticles(List<NewsItem> articles) {
		this.articles = articles;
	}

	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<ToUserName>" + getToUserName() + "</ToUserName>");
		sb.append("<FromUserName>" + getFromUserName() + "</FromUserName>");
		sb.append("<CreateTime>" + getCreateTime() + "</CreateTime>");
		sb.append("<MsgType>" + getMsgType() + "</MsgType>");
		sb.append("<Articles>");
		int ArticleCount = 0;
		for (NewsItem item : articles) {
			sb.append("<item>");
			sb.append("<Title><![CDATA[" + item.getTitle() + "]]></Title>");
			sb.append("<Description><![CDATA[" + item.getDescription() + "]]></Description>");
			if (StringUtils.isNotBlank(item.getPicUrl())) {
				sb.append("<PicUrl><![CDATA[" + item.getPicUrl() + "]]></PicUrl>");
			}
			sb.append("<Url><![CDATA[" + item.getUrl() + "]]></Url>");
			sb.append("</item>");
			ArticleCount++;
			if (ArticleCount == 10) {
				break;
			}
		}
		sb.append("</Articles>");
		sb.append("<ArticleCount>" + ArticleCount + "</ArticleCount>");
		sb.append("<FuncFlag>" + getFuncFlag() + "</FuncFlag>");
		sb.append("</xml>");
		return sb.toString();
	}

}
