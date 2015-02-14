package com.homlin.module.shop.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbPluginSignin entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_plugin_signin")
public class TbPluginSignin implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopMember tbShopMember;
	private String createDate;
	private String modifyDate;
	private Integer dates;
	private Integer datesAddup;
	private BigDecimal scoreAddup;

	// Constructors

	/** default constructor */
	public TbPluginSignin() {
	}

	public TbPluginSignin(String id) {
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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	public TbShopMember getTbShopMember() {
		return this.tbShopMember;
	}

	public void setTbShopMember(TbShopMember tbShopMember) {
		this.tbShopMember = tbShopMember;
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

	@Column(name = "dates", nullable = false)
	public Integer getDates() {
		return this.dates;
	}

	public void setDates(Integer dates) {
		this.dates = dates;
	}

	@Column(name = "datesAddup", nullable = false)
	public Integer getDatesAddup() {
		return this.datesAddup;
	}

	public void setDatesAddup(Integer datesAddup) {
		this.datesAddup = datesAddup;
	}

	@Column(name = "scoreAddup", nullable = false, precision = 18, scale = 0)
	public BigDecimal getScoreAddup() {
		return this.scoreAddup;
	}

	public void setScoreAddup(BigDecimal scoreAddup) {
		this.scoreAddup = scoreAddup;
	}

}