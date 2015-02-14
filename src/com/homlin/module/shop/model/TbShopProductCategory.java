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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopProductCategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_product_category")
public class TbShopProductCategory implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopProductCategory tbShopProductCategory;
	private String createDate;
	private String modifyDate;
	private String code;
	private String name;
	private Integer displayorder;
	private String metaKeyword;
	private String metaDescription;
	private String inuse;
	private String indexpath;
	private List<TbShopProductCategory> tbShopProductCategories = new ArrayList<TbShopProductCategory>(0);
	private Set<TbShopProduct> tbShopProducts = new HashSet<TbShopProduct>(0);
	private List<TbShopBrand> tbShopBrands = new ArrayList<TbShopBrand>(0);

	// Constructors

	/** default constructor */
	public TbShopProductCategory() {
	}

	public TbShopProductCategory(String id) {
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
	@JoinColumn(name = "pid")
	public TbShopProductCategory getTbShopProductCategory() {
		return this.tbShopProductCategory;
	}

	public void setTbShopProductCategory(TbShopProductCategory tbShopProductCategory) {
		this.tbShopProductCategory = tbShopProductCategory;
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

	@Column(name = "code", length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "displayorder")
	public Integer getDisplayorder() {
		return this.displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "metaKeyword")
	public String getMetaKeyword() {
		return this.metaKeyword;
	}

	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}

	@Column(name = "metaDescription")
	public String getMetaDescription() {
		return this.metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	@Column(name = "inuse", length = 1)
	public String getInuse() {
		return this.inuse;
	}

	public void setInuse(String inuse) {
		this.inuse = inuse;
	}

	@Column(name = "indexpath")
	public String getIndexpath() {
		return this.indexpath;
	}

	public void setIndexpath(String indexpath) {
		this.indexpath = indexpath;
	}

	@OrderBy("displayorder asc")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopProductCategory")
	public List<TbShopProductCategory> getTbShopProductCategories() {
		return this.tbShopProductCategories;
	}

	public void setTbShopProductCategories(List<TbShopProductCategory> tbShopProductCategories) {
		this.tbShopProductCategories = tbShopProductCategories;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tbShopProductCategory")
	public Set<TbShopProduct> getTbShopProducts() {
		return this.tbShopProducts;
	}

	public void setTbShopProducts(Set<TbShopProduct> tbShopProducts) {
		this.tbShopProducts = tbShopProducts;
	}

	@OrderBy("displayorder asc")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_shop_product_category_brand", joinColumns = {
			@JoinColumn(name = "category_id", nullable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "brand_id", nullable = false, updatable = false) })
	public List<TbShopBrand> getTbShopBrands() {
		return this.tbShopBrands;
	}

	public void setTbShopBrands(List<TbShopBrand> tbShopBrands) {
		this.tbShopBrands = tbShopBrands;
	}

}