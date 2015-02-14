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
 * TbShopShippingMethodDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_shipping_method_detail")
public class TbShopShippingMethodDetail implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopShippingMethod tbShopShippingMethod;
	private String createDate;
	private String modifyDate;
	private Integer firstWeight;
	private BigDecimal firstPrice;
	private Integer continueWeight;
	private BigDecimal continuePrice;
	private String areaName;
	private String areaCode;

	// Constructors

	/** default constructor */
	public TbShopShippingMethodDetail() {
	}

	public TbShopShippingMethodDetail(String id) {
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
	@JoinColumn(name = "method_id", nullable = false)
	public TbShopShippingMethod getTbShopShippingMethod() {
		return this.tbShopShippingMethod;
	}

	public void setTbShopShippingMethod(TbShopShippingMethod tbShopShippingMethod) {
		this.tbShopShippingMethod = tbShopShippingMethod;
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

	@Column(name = "areaName")
	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name = "areaCode")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

}