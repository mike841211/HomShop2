package com.homlin.module.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TbShopChina entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_china_area")
public class TbShopChinaArea implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String code;
	private String name;

	// Constructors

	/** default constructor */
	public TbShopChinaArea() {
	}

	public TbShopChinaArea(String code) {
		this.code = code;
	}

	// Property accessors
	@Id
	@Column(name = "CODE", unique = true, nullable = false, length = 6)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}