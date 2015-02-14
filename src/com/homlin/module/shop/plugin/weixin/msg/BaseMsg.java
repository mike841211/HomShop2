package com.homlin.module.shop.plugin.weixin.msg;

public class BaseMsg {
	// ToUserName 是 接收方帐号（收到的OpenID）
	// FromUserName 是 开发者微信号
	// CreateTime 是 消息创建时间 （整型）
	// MsgType 是 text
	// funcFlag
	// FuncFlag 的作用
	// XML 回复中有一个 FuncFlag 节点，通过设置这个节点的内容，可以给微信消息加星标
	// 比如用户如果发送一个请求，而 API 又无法处理，可以将这个节点内容设置为 1，那么用户发送的消息会被加上星标
	// 这样以后在公众平台的后台就可以方便地找到这条消息

	private String toUserName;
	private String fromUserName;
	private String createTime;
	private MsgType msgType;
	private String funcFlag = "0";

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

	public String getFuncFlag() {
		return funcFlag;
	}

	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}

}
