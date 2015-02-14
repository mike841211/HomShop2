package com.homlin.module.shop.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopShippingMethod entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_shipping_method")
public class TbShopShippingMethod implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private String createDate;
	private String modifyDate;
	private String name;
	private Integer firstWeight;
	private BigDecimal firstPrice;
	private Integer continueWeight;
	private BigDecimal continuePrice;
	private Integer displayorder;
	private String remark;
	private String enabled;
	private Set<TbShopShippingMethodDetail> tbShopShippingMethodDetails = new HashSet<TbShopShippingMethodDetail>(0);

	// Constructors

	/** default constructor */
	public TbShopShippingMethod() {
	}

	public TbShopShippingMethod(String id) {
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

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "first_weight", nullable = false)
	public Integer getFirstWeight() {
		return this.firstWeight;
	}

	public void setFirstWeight(Integer firstWeight) {
		this.firstWeight = firstWeight;
	}

	@Column(name = "first_price", nullable = false, precision = 18)
	public BigDecimal getFirstPrice() {
		return this.firstPrice;
	}

	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	@Column(name = "continue_weight", nullable = false)
	public Integer getContinueWeight() {
		return this.continueWeight;
	}

	public void setContinueWeight(Integer continueWeight) {
		this.continueWeight = continueWeight;
	}

	@Column(name = "continue_price", nullable = false, precision = 18)
	public BigDecimal getContinuePrice() {
		return this.continuePrice;
	}

	public void setContinuePrice(BigDecimal continuePrice) {
		this.continuePrice = continuePrice;
	}

	@Column(name = "displayorder")
	public Integer getDisplayorder() {
		return this.displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "enabled", length = 1)
	public String getEnabled() {
		return this.enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@OrderBy("id")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopShippingMethod", orphanRemoval = true)
	public Set<TbShopShippingMethodDetail> getTbShopShippingMethodDetails() {
		return this.tbShopShippingMethodDetails;
	}

	public void setTbShopShippingMethodDetails(Set<TbShopShippingMethodDetail> tbShopShippingMethodDetails) {
		this.tbShopShippingMethodDetails = tbShopShippingMethodDetails;
	}

}