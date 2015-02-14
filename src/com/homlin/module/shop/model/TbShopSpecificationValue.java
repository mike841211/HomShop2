package com.homlin.module.shop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopSpecificationValue entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_specification_value")
public class TbShopSpecificationValue implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopSpecification tbShopSpecification;
	private String createDate;
	private String modifyDate;
	private String name;
	private String image;
	private Integer displayorder;
	private Set<TbShopSkuSpecificationAndValue> tbShopSkuSpecificationAndValues = new HashSet<TbShopSkuSpecificationAndValue>(0);

	// Constructors

	/** default constructor */
	public TbShopSpecificationValue() {
	}

	public TbShopSpecificationValue(String id) {
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
	@JoinColumn(name = "specification_id", nullable = false)
	public TbShopSpecification getTbShopSpecification() {
		return this.tbShopSpecification;
	}

	public void setTbShopSpecification(TbShopSpecification tbShopSpecification) {
		this.tbShopSpecification = tbShopSpecification;
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

	@Column(name = "image")
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "displayorder")
	public Integer getDisplayorder() {
		return this.displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tbShopSpecificationValue")
	public Set<TbShopSkuSpecificationAndValue> getTbShopSkuSpecificationAndValues() {
		return this.tbShopSkuSpecificationAndValues;
	}

	public void setTbShopSkuSpecificationAndValues(Set<TbShopSkuSpecificationAndValue> tbShopSkuSpecificationAndValues) {
		this.tbShopSkuSpecificationAndValues = tbShopSkuSpecificationAndValues;
	}

}