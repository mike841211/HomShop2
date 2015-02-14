package com.homlin.module.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TbShopMemberOauth2 entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_member_oauth2")
public class TbShopMemberOauth2 implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public enum OAuthType {
		// WEIXIN("微信"), QQ("QQ"), TAOBAO("淘宝网"), SINAWEIBO("新浪微博");
		WEIXIN("微信");

		private String name;

		public String getName() {
			return this.name;
		}

		private OAuthType(String name) {
			this.name = name;
		}

		public static OAuthType valueOf(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return values()[ordinal];
		}
	}

	// Fields

	private String openid;
	private TbShopMember tbShopMember;
	private OAuthType oAuthType;
	private String createDate;
	private String modifyDate;
	private String accessToken;
	private String refreshToken;
	private String nickname;
	private String sex;
	private String headimgurl;

	// Constructors

	/** default constructor */
	public TbShopMemberOauth2() {
	}

	public TbShopMemberOauth2(String openid) {
		this.openid = openid;
	}

	// Property accessors
	@Id
	@Column(name = "openid", unique = true, nullable = false)
	public String getOpenid() {
		return this.openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	public TbShopMember getTbShopMember() {
		return this.tbShopMember;
	}

	public void setTbShopMember(TbShopMember tbShopMember) {
		this.tbShopMember = tbShopMember;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "oAuthType", length = 50)
	public OAuthType getOAuthType() {
		return this.oAuthType;
	}

	public void setOAuthType(OAuthType oAuthType) {
		this.oAuthType = oAuthType;
	}

	@Column(name = "createDate", length = 23)
	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Column(name = "modifyDate", length = 23)
	public String getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "accessToken")
	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Column(name = "refreshToken")
	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Column(name = "nickname")
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "sex", length = 1)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "headimgurl")
	public String getHeadimgurl() {
		return this.headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

}