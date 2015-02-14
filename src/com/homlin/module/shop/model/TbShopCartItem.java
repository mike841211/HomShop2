package com.homlin.module.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopCartItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_cart_item")
public class TbShopCartItem implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopCart tbShopCart;
	private TbShopSku tbShopSku;
	private String createDate;
	private String modifyDate;
	private Integer quantity;

	// Constructors

	/** default constructor */
	public TbShopCartItem() {
	}

	public TbShopCartItem(String id) {
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
	@JoinColumn(name = "cart_id", nullable = false)
	public TbShopCart getTbShopCart() {
		return this.tbShopCart;
	}

	public void setTbShopCart(TbShopCart tbShopCart) {
		this.tbShopCart = tbShopCart;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_id", nullable = false)
	public TbShopSku getTbShopSku() {
		return this.tbShopSku;
	}

	public void setTbShopSku(TbShopSku tbShopSku) {
		this.tbShopSku = tbShopSku;
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

	@Column(name = "quantity", nullable = false)
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}