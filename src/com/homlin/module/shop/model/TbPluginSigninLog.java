package com.homlin.module.shop.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TbPluginSigninLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_plugin_signin_log")
public class TbPluginSigninLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopMember tbShopMember;
	private String signDate;
	private BigDecimal score;

	// Constructors

	/** default constructor */
	public TbPluginSigninLog() {
	}

	public TbPluginSigninLog(String id) {
		this.id = id;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	public TbShopMember getTbShopMember() {
		return this.tbShopMember;
	}

	public void setTbShopMember(TbShopMember tbShopMember) {
		this.tbShopMember = tbShopMember;
	}

	@Column(name = "signDate", length = 23)
	public String getSignDate() {
		return this.signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	@Column(name = "score", precision = 18, scale = 0)
	public BigDecimal getScore() {
		return this.score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

}