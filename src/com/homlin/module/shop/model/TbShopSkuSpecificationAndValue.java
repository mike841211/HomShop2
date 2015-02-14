package com.homlin.module.shop.model;

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
 * TbShopSkuSpecificationAndValue entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_sku_specificationAndValue")
public class TbShopSkuSpecificationAndValue implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopSpecificationValue tbShopSpecificationValue;
	private TbShopSpecification tbShopSpecification;
	private TbShopSku tbShopSku;

	// Constructors

	/** default constructor */
	public TbShopSkuSpecificationAndValue() {
	}

	public TbShopSkuSpecificationAndValue(String id) {
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
	@JoinColumn(name = "specification_value_id", nullable = false)
	public TbShopSpecificationValue getTbShopSpecificationValue() {
		return this.tbShopSpecificationValue;
	}

	public void setTbShopSpecificationValue(TbShopSpecificationValue tbShopSpecificationValue) {
		this.tbShopSpecificationValue = tbShopSpecificationValue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specification_id", nullable = false)
	public TbShopSpecification getTbShopSpecification() {
		return this.tbShopSpecification;
	}

	public void setTbShopSpecification(TbShopSpecification tbShopSpecification) {
		this.tbShopSpecification = tbShopSpecification;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_id", nullable = false)
	public TbShopSku getTbShopSku() {
		return this.tbShopSku;
	}

	public void setTbShopSku(TbShopSku tbShopSku) {
		this.tbShopSku = tbShopSku;
	}

}