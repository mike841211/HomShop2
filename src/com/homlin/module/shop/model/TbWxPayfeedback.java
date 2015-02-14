package com.homlin.module.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TbWxPayfeedback entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_wx_payfeedback")
public class TbWxPayfeedback implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public enum FeedBackStatus {
		unconfirmed("未处理"), confirmed("已解决"), user_unconfirmed("等待用户确认"), reject("用户拒绝");

		private String name;

		public String getName() {
			return this.name;
		}

		private FeedBackStatus(String name) {
			this.name = name;
		}

		public static FeedBackStatus valueOf(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return values()[ordinal];
		}
	}

	// Fields

	private String feedBackId;
	private Integer timeStamp;
	private String openId;
	private String appId;
	private String transId;
	private String reason;
	private String solution;
	private String extInfo;
	private String picUrl0;
	private String picUrl1;
	private String picUrl2;
	private String picUrl3;
	private String picUrl4;
	private String rawXmlData;
	private FeedBackStatus status;

	// Constructors

	/** default constructor */
	public TbWxPayfeedback() {
	}

	public TbWxPayfeedback(String feedBackId) {
		this.feedBackId = feedBackId;
	}

	// Property accessors
	@Id
	@Column(name = "FeedBackId", unique = true, nullable = false)
	public String getFeedBackId() {
		return this.feedBackId;
	}

	public void setFeedBackId(String feedBackId) {
		this.feedBackId = feedBackId;
	}

	@Column(name = "timeStamp")
	public Integer getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(Integer timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Column(name = "OpenId")
	public String getOpenId() {
		return this.openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Column(name = "AppId")
	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "TransId")
	public String getTransId() {
		return this.transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	@Column(name = "Reason")
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "Solution")
	public String getSolution() {
		return this.solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	@Column(name = "ExtInfo")
	public String getExtInfo() {
		return this.extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	@Column(name = "PicUrl0")
	public String getPicUrl0() {
		return this.picUrl0;
	}

	public void setPicUrl0(String picUrl0) {
		this.picUrl0 = picUrl0;
	}

	@Column(name = "PicUrl1")
	public String getPicUrl1() {
		return this.picUrl1;
	}

	public void setPicUrl1(String picUrl1) {
		this.picUrl1 = picUrl1;
	}

	@Column(name = "PicUrl2")
	public String getPicUrl2() {
		return this.picUrl2;
	}

	public void setPicUrl2(String picUrl2) {
		this.picUrl2 = picUrl2;
	}

	@Column(name = "PicUrl3")
	public String getPicUrl3() {
		return this.picUrl3;
	}

	public void setPicUrl3(String picUrl3) {
		this.picUrl3 = picUrl3;
	}

	@Column(name = "PicUrl4")
	public String getPicUrl4() {
		return this.picUrl4;
	}

	public void setPicUrl4(String picUrl4) {
		this.picUrl4 = picUrl4;
	}

	@Column(name = "rawXmlData", length = 4000)
	public String getRawXmlData() {
		return this.rawXmlData;
	}

	public void setRawXmlData(String rawXmlData) {
		this.rawXmlData = rawXmlData;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	public FeedBackStatus getStatus() {
		return this.status;
	}

	public void setStatus(FeedBackStatus status) {
		this.status = status;
	}

}