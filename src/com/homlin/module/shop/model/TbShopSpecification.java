package com.homlin.module.shop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopSpecification entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_specification")
public class TbShopSpecification implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private String createDate;
	private String modifyDate;
	private String name;
	private String remark;
	private Integer displayorder;
	private Set<TbShopProduct> tbShopProducts = new HashSet<TbShopProduct>(0);
	private List<TbShopSpecificationValue> tbShopSpecificationValues = new ArrayList<TbShopSpecificationValue>();
	private Set<TbShopSkuSpecificationAndValue> tbShopSkuSpecificationAndValues = new HashSet<TbShopSkuSpecificationAndValue>(0);

	// Constructors

	/** default constructor */
	public TbShopSpecification() {
	}

	public TbShopSpecification(String id) {
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

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "displayorder")
	public Integer getDisplayorder() {
		return this.displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "tbShopSpecifications")
	public Set<TbShopProduct> getTbShopProducts() {
		return this.tbShopProducts;
	}

	public void setTbShopProducts(Set<TbShopProduct> tbShopProducts) {
		this.tbShopProducts = tbShopProducts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopSpecification")
	@OrderBy("displayorder asc")
	public List<TbShopSpecificationValue> getTbShopSpecificationValues() {
		return this.tbShopSpecificationValues;
	}

	public void setTbShopSpecificationValues(List<TbShopSpecificationValue> tbShopSpecificationValues) {
		this.tbShopSpecificationValues = tbShopSpecificationValues;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopSpecification")
	public Set<TbShopSkuSpecificationAndValue> getTbShopSkuSpecificationAndValues() {
		return this.tbShopSkuSpecificationAndValues;
	}

	public void setTbShopSkuSpecificationAndValues(Set<TbShopSkuSpecificationAndValue> tbShopSkuSpecificationAndValues) {
		this.tbShopSkuSpecificationAndValues = tbShopSkuSpecificationAndValues;
	}

}