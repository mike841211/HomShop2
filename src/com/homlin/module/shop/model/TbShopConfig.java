package com.homlin.module.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopConfig entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_config")
public class TbShopConfig implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String cfgKey;
	private String cfgValue;
	private String cfgGroup;
	private String cfgName;
	private String cfgCaption;

	// Constructors

	/** default constructor */
	public TbShopConfig() {
	}

	public TbShopConfig(String cfgKey) {
		this.cfgKey = cfgKey;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "cfgKey", unique = true, nullable = false)
	public String getCfgKey() {
		return this.cfgKey;
	}

	public void setCfgKey(String cfgKey) {
		this.cfgKey = cfgKey;
	}

	@Column(name = "cfgValue", nullable = false)
	public String getCfgValue() {
		return this.cfgValue;
	}

	public void setCfgValue(String cfgValue) {
		this.cfgValue = cfgValue;
	}

	@Column(name = "cfgGroup")
	public String getCfgGroup() {
		return this.cfgGroup;
	}

	public void setCfgGroup(String cfgGroup) {
		this.cfgGroup = cfgGroup;
	}

	@Column(name = "cfgName")
	public String getCfgName() {
		return this.cfgName;
	}

	public void setCfgName(String cfgName) {
		this.cfgName = cfgName;
	}

	@Column(name = "cfgCaption")
	public String getCfgCaption() {
		return this.cfgCaption;
	}

	public void setCfgCaption(String cfgCaption) {
		this.cfgCaption = cfgCaption;
	}

}