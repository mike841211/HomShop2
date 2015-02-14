package com.homlin.module.shop.plugin.weixin.msg;

public class NewsItem {
	// Title 否 图文消息标题
	// Description 否 图文消息描述
	// PicUrl 否 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	// Url 否 点击图文消息跳转链接
	private String title;
	private String description;
	private String picUrl;
	private String url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
