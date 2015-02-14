package com.homlin.module.shop.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopCart entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_cart")
public class TbShopCart implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopMember tbShopMember;
	private String createDate;
	private String modifyDate;
	private String cartKey;
	private Set<TbShopCartItem> tbShopCartItems = new HashSet<TbShopCartItem>(0);

	// Constructors

	/** default constructor */
	public TbShopCart() {
	}

	public TbShopCart(String id) {
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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	public TbShopMember getTbShopMember() {
		return this.tbShopMember;
	}

	public void setTbShopMember(TbShopMember tbShopMember) {
		this.tbShopMember = tbShopMember;
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

	@Column(name = "cart_key", length = 36)
	public String getCartKey() {
		return this.cartKey;
	}

	public void setCartKey(String cartKey) {
		this.cartKey = cartKey;
	}

	@OrderBy("createDate asc")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopCart")
	public Set<TbShopCartItem> getTbShopCartItems() {
		return this.tbShopCartItems;
	}

	public void setTbShopCartItems(Set<TbShopCartItem> tbShopCartItems) {
		this.tbShopCartItems = tbShopCartItems;
	}

	@Transient
	public TbShopCartItem getCartItem(TbShopSku sku) {
		if (sku != null && getTbShopCartItems() != null) {
			Iterator<TbShopCartItem> localIterator = getTbShopCartItems().iterator();
			while (localIterator.hasNext()) {
				TbShopCartItem localCartItem = localIterator.next();
				if (localCartItem != null && localCartItem.getTbShopSku() == sku) {
					return localCartItem;
				}
			}
		}
		return null;
	}

	@Transient
	public boolean contains(TbShopSku sku) {
		if (sku != null && getTbShopCartItems() != null) {
			Iterator<TbShopCartItem> localIterator = getTbShopCartItems().iterator();
			while (localIterator.hasNext()) {
				TbShopCartItem localCartItem = localIterator.next();
				if (localCartItem != null && localCartItem.getTbShopSku() == sku) {
					return true;
				}
			}
		}
		return false;
	}

}