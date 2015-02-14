package com.homlin.module.shop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbWxMenu entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_wx_menu")
public class TbWxMenu implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbWxMenu tbWxMenu;
	private String createDate;
	private String modifyDate;
	private String name;
	private String url;
	private String menuType;
	private String messageType;
	private Integer displayorder;
	private String remark;
	private String inuse;
	private String functionKey;
	private Set<TbWxMenu> tbWxMenus = new HashSet<TbWxMenu>(0);
	private Set<TbWxMenuMsg> tbWxMenuMsgs = new HashSet<TbWxMenuMsg>(0);

	// Constructors

	/** default constructor */
	public TbWxMenu() {
	}

	public TbWxMenu(String id) {
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
	public TbWxMenu getTbWxMenu() {
		return this.tbWxMenu;
	}

	public void setTbWxMenu(TbWxMenu tbWxMenu) {
		this.tbWxMenu = tbWxMenu;
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

	@Column(name = "url")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "menuType", length = 50)
	public String getMenuType() {
		return this.menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	@Column(name = "messageType", length = 50)
	public String getMessageType() {
		return this.messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Column(name = "displayorder")
	public Integer getDisplayorder() {
		return this.displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "inuse", length = 1)
	public String getInuse() {
		return this.inuse;
	}

	public void setInuse(String inuse) {
		this.inuse = inuse;
	}

	@Column(name = "functionKey")
	public String getFunctionKey() {
		return this.functionKey;
	}

	public void setFunctionKey(String functionKey) {
		this.functionKey = functionKey;
	}

	@OrderBy("displayorder")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbWxMenu")
	public Set<TbWxMenu> getTbWxMenus() {
		return this.tbWxMenus;
	}

	public void setTbWxMenus(Set<TbWxMenu> tbWxMenus) {
		this.tbWxMenus = tbWxMenus;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbWxMenu")
	public Set<TbWxMenuMsg> getTbWxMenuMsgs() {
		return this.tbWxMenuMsgs;
	}

	public void setTbWxMenuMsgs(Set<TbWxMenuMsg> tbWxMenuMsgs) {
		this.tbWxMenuMsgs = tbWxMenuMsgs;
	}

}