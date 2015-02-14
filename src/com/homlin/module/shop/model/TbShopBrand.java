package com.homlin.module.shop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopBrand entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_brand")
public class TbShopBrand implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private String createDate;
	private String modifyDate;
	private String name;
	private String enname;
	private String logo;
	private String url;
	private Integer displayorder;
	private String introduction;
	private Set<TbShopProduct> tbShopProducts = new HashSet<TbShopProduct>(0);
	private Set<TbShopProductCategory> tbShopProductCategories = new HashSet<TbShopProductCategory>(0);

	// Constructors

	/** default constructor */
	public TbShopBrand() {
	}

	public TbShopBrand(String id) {
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

	@Column(name = "name", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "enname", length = 100)
	public String getEnname() {
		return this.enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	@Column(name = "logo")
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Column(name = "url")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "displayorder")
	public Integer getDisplayorder() {
		return this.displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "introduction")
	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tbShopBrand")
	public Set<TbShopProduct> getTbShopProducts() {
		return this.tbShopProducts;
	}

	public void setTbShopProducts(Set<TbShopProduct> tbShopProducts) {
		this.tbShopProducts = tbShopProducts;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "tbShopBrands")
	public Set<TbShopProductCategory> getTbShopProductCategories() {
		return this.tbShopProductCategories;
	}

	public void setTbShopProductCategories(Set<TbShopProductCategory> tbShopProductCategories) {
		this.tbShopProductCategories = tbShopProductCategories;
	}

}