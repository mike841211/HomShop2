package com.homlin.module.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TbWxGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_wx_group")
public class TbWxGroup implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private Integer id;
	private String name;
	private Integer count;

	// Constructors

	/** default constructor */
	public TbWxGroup() {
	}

	public TbWxGroup(Integer id) {
		this.id = id;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "count", nullable = false)
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}