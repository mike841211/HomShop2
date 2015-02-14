package com.homlin.module.shop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopAdmin entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_admin")
public class TbShopAdmin implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private String createDate;
	private String modifyDate;
	private String username;
	private String password;
	private String lock;
	private String loginDate;
	private String loginIp;
	private Integer loginCount;
	private Integer loginFailureCount;
	private String loginFailureDate;
	private String name;
	private String email;
	private String department;
	private Set<TbShopRole> tbShopRoles = new HashSet<TbShopRole>(0);

	// Constructors

	/** default constructor */
	public TbShopAdmin() {
	}

	public TbShopAdmin(String id) {
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

	@Column(name = "username", nullable = false, length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "lock", length = 1)
	public String getLock() {
		return this.lock;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	@Column(name = "loginDate", length = 23)
	public String getLoginDate() {
		return this.loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	@Column(name = "loginIP", length = 50)
	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Column(name = "loginCount", nullable = false)
	public Integer getLoginCount() {
		return this.loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	@Column(name = "loginFailureCount", nullable = false)
	public Integer getLoginFailureCount() {
		return this.loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	@Column(name = "loginFailureDate", length = 23)
	public String getLoginFailureDate() {
		return this.loginFailureDate;
	}

	public void setLoginFailureDate(String loginFailureDate) {
		this.loginFailureDate = loginFailureDate;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "department", length = 50)
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_shop_admin_role", joinColumns = {
			@JoinColumn(name = "admin_id", nullable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "role_id", nullable = false, updatable = false) })
	public Set<TbShopRole> getTbShopRoles() {
		return this.tbShopRoles;
	}

	public void setTbShopRoles(Set<TbShopRole> tbShopRoles) {
		this.tbShopRoles = tbShopRoles;
	}

}