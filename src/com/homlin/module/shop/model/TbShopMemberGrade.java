package com.homlin.module.shop.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopMemberGrade entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_member_grade")
public class TbShopMemberGrade implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private String createDate;
	private String modifyDate;
	private Integer lever;
	private String name;
	private BigDecimal score;
	private BigDecimal discount;
	private String isSpecial;
	private String remark;
	private Set<TbShopMember> tbShopMembers = new HashSet<TbShopMember>(0);

	// Constructors

	/** default constructor */
	public TbShopMemberGrade() {
	}

	public TbShopMemberGrade(String id) {
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

	@Column(name = "lever", nullable = false)
	public Integer getLever() {
		return this.lever;
	}

	public void setLever(Integer lever) {
		this.lever = lever;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "score", nullable = false, precision = 18, scale = 0)
	public BigDecimal getScore() {
		return this.score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	@Column(name = "discount", nullable = false, precision = 3)
	public BigDecimal getDiscount() {
		return this.discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Column(name = "isSpecial", length = 1)
	public String getIsSpecial() {
		return this.isSpecial;
	}

	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tbShopMemberGrade")
	public Set<TbShopMember> getTbShopMembers() {
		return this.tbShopMembers;
	}

	public void setTbShopMembers(Set<TbShopMember> tbShopMembers) {
		this.tbShopMembers = tbShopMembers;
	}

}