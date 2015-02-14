package com.homlin.module.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopDeliveryTemplate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_delivery_template")
public class TbShopDeliveryTemplate implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private String createDate;
	private String modifyDate;
	private String name;
	private String background;
	private String config;

	// Constructors

	/** default constructor */
	public TbShopDeliveryTemplate() {
	}

	public TbShopDeliveryTemplate(String id) {
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

	@Column(name = "background")
	public String getBackground() {
		return this.background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	@Column(name = "config")
	public String getConfig() {
		return this.config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

}