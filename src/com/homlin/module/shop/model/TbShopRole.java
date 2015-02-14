package com.homlin.module.shop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_role")
public class TbShopRole implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private String createDate;
	private String modifyDate;
	private String name;
	private String description;
	private String authority;
	private Set<TbShopAdmin> tbShopAdmins = new HashSet<TbShopAdmin>(0);

	// Constructors

	/** default constructor */
	public TbShopRole() {
	}

	public TbShopRole(String id) {
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

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "authority")
	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "tbShopRoles")
	public Set<TbShopAdmin> getTbShopAdmins() {
		return this.tbShopAdmins;
	}

	public void setTbShopAdmins(Set<TbShopAdmin> tbShopAdmins) {
		this.tbShopAdmins = tbShopAdmins;
	}

}