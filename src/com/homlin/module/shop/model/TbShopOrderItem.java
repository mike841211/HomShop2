package com.homlin.module.shop.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopOrderItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_order_item")
public class TbShopOrderItem implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopOrder tbShopOrder;
	private TbShopSku tbShopSku;
	private String createDate;
	private String modifyDate;
	private String productId; // 不关联商品，冗余数据方便查询
	private String sn;
	private String name;
	private String specifications;
	private BigDecimal originalPrice;
	private BigDecimal discount;
	private BigDecimal price;
	private Integer quantity;
	private Integer shippedQuantity;
	private Integer returnQuantity;
	private BigDecimal cost;
	private Integer weight;
	private String imagePath;
	private String htmlPath;
	private String isGift;
	private TbShopProductComment tbShopProductComment;

	// Constructors

	/** default constructor */
	public TbShopOrderItem() {
	}

	public TbShopOrderItem(String id) {
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
	@JoinColumn(name = "order_id", nullable = false)
	public TbShopOrder getTbShopOrder() {
		return this.tbShopOrder;
	}

	public void setTbShopOrder(TbShopOrder tbShopOrder) {
		this.tbShopOrder = tbShopOrder;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_id")
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

	@Column(name = "productId")
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Column(name = "sn")
	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "specifications")
	public String getSpecifications() {
		return this.specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	@Column(name = "originalPrice", nullable = false, precision = 18)
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	@Column(name = "discount", nullable = false, precision = 3)
	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Column(name = "price", nullable = false, precision = 18)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "quantity", nullable = false)
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(name = "shippedQuantity", nullable = false)
	public Integer getShippedQuantity() {
		return this.shippedQuantity;
	}

	public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	@Column(name = "returnQuantity", nullable = false)
	public Integer getReturnQuantity() {
		return this.returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	@Column(name = "cost", nullable = false, precision = 18)
	public BigDecimal getCost() {
		return this.cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Column(name = "weight", nullable = false)
	public Integer getWeight() {
		return this.weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Column(name = "imagePath")
	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Column(name = "htmlPath")
	public String getHtmlPath() {
		return this.htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	@Column(name = "isGift", length = 1)
	public String getIsGift() {
		return this.isGift;
	}

	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "tbShopOrderItem")
	public TbShopProductComment getTbShopProductComment() {
		return this.tbShopProductComment;
	}

	public void setTbShopProductComment(TbShopProductComment tbShopProductComment) {
		this.tbShopProductComment = tbShopProductComment;
	}

}