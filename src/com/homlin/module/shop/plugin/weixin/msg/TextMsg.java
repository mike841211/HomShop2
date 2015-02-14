package com.homlin.module.shop.plugin.weixin.msg;

public class TextMsg extends BaseMsg {

	// Content 是 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
	private String content;

	public TextMsg() {
		setMsgType(MsgType.text);
		setCreateTime(String.valueOf(System.currentTimeMillis()));
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String toXmlString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<ToUserName>" + getToUserName() + "</ToUserName>");
		sb.append("<FromUserName>" + getFromUserName() + "</FromUserName>");
		sb.append("<CreateTime>" + getCreateTime() + "</CreateTime>");
		sb.append("<MsgType>" + getMsgType() + "</MsgType>");
		sb.append("<Content><![CDATA[" + getContent() + "]]></Content>");
		sb.append("<FuncFlag>" + getFuncFlag() + "</FuncFlag>");
		sb.append("</xml>");
		return sb.toString();
	}

}
