package com.homlin.module.shop.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TbWxMsg entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_wx_msg")
public class TbWxMsg implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String msgId;
	private String toUserName;
	private String fromUserName;
	private Integer createTime;
	private String msgType;
	private String content;
	private String mediaId;
	private String picUrl;
	private String format;
	private String recognition;
	private String thumbMediaId;
	private BigDecimal locationX;
	private BigDecimal locationY;
	private Integer scale;
	private String label;
	private String title;
	private String description;
	private String url;
	private String lastreply;

	// Constructors

	/** default constructor */
	public TbWxMsg() {
	}

	public TbWxMsg(String msgId) {
		this.msgId = msgId;
	}

	// Property accessors
	@Id
	@Column(name = "MsgId", unique = true, nullable = false)
	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Column(name = "ToUserName", nullable = false)
	public String getToUserName() {
		return this.toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	@Column(name = "FromUserName", nullable = false)
	public String getFromUserName() {
		return this.fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	@Column(name = "CreateTime", nullable = false)
	public Integer getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MsgType", nullable = false)
	public String getMsgType() {
		return this.msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@Column(name = "Content", length = 4000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "MediaId")
	public String getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "PicUrl")
	public String getPicUrl() {
		return this.picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@Column(name = "Format")
	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name = "Recognition", length = 4000)
	public String getRecognition() {
		return this.recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	@Column(name = "ThumbMediaId")
	public String getThumbMediaId() {
		return this.thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	@Column(name = "Location_X", precision = 9, scale = 6)
	public BigDecimal getLocationX() {
		return this.locationX;
	}

	public void setLocationX(BigDecimal locationX) {
		this.locationX = locationX;
	}

	@Column(name = "Location_Y", precision = 9, scale = 6)
	public BigDecimal getLocationY() {
		return this.locationY;
	}

	public void setLocationY(BigDecimal locationY) {
		this.locationY = locationY;
	}

	@Column(name = "Scale")
	public Integer getScale() {
		return this.scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	@Column(name = "Label")
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name = "Title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "Description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "Url")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "Lastreply", length = 4000)
	public String getLastreply() {
		return this.lastreply;
	}

	public void setLastreply(String lastreply) {
		this.lastreply = lastreply;
	}

}