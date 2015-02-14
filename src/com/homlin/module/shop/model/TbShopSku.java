package com.homlin.module.shop.model;

import java.math.BigDecimal;
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
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopSku entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_sku")
public class TbShopSku implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopProduct tbShopProduct;
	private String createDate;
	private String modifyDate;
	private String specificationValueName;
	private String specificationJson;
	private String syssn;
	private String sn;
	private BigDecimal cost;
	private BigDecimal price;
	private BigDecimal marketPrice;
	private Integer stock;
	private Integer blockedStock;
	private Integer sales;
	private String storeLocation;
	private Integer weight;
	private Integer length;
	private Integer width;
	private Integer height;
	private String barcode;
	private String sampleImage;
	private Set<TbShopSkuSpecificationAndValue> tbShopSkuSpecificationAndValues = new HashSet<TbShopSkuSpecificationAndValue>(0);
	private Set<TbShopOrderItem> tbShopOrderItems = new HashSet<TbShopOrderItem>(0);
	private Set<TbShopCartItem> tbShopCartItems = new HashSet<TbShopCartItem>(0);

	// Constructors

	/** default constructor */
	public TbShopSku() {
	}

	public TbShopSku(String id) {
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
	@JoinColumn(name = "product_id", nullable = false)
	public TbShopProduct getTbShopProduct() {
		return this.tbShopProduct;
	}

	public void setTbShopProduct(TbShopProduct tbShopProduct) {
		this.tbShopProduct = tbShopProduct;
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

	@Column(name = "specificationValueName")
	public String getSpecificationValueName() {
		return this.specificationValueName;
	}

	public void setSpecificationValueName(String specificationValueName) {
		this.specificationValueName = specificationValueName;
	}

	@Column(name = "specificationJson")
	public String getSpecificationJson() {
		return this.specificationJson;
	}

	public void setSpecificationJson(String specificationJson) {
		this.specificationJson = specificationJson;
	}

	@Column(name = "syssn")
	public String getSyssn() {
		return this.syssn;
	}

	public void setSyssn(String syssn) {
		this.syssn = syssn;
	}

	@Column(name = "sn")
	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "cost", precision = 18)
	public BigDecimal getCost() {
		return this.cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Column(name = "price", precision = 18)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "marketPrice", precision = 18)
	public BigDecimal getMarketPrice() {
		return this.marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	@Column(name = "stock")
	public Integer getStock() {
		return this.stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	@Column(name = "blockedStock")
	public Integer getBlockedStock() {
		return this.blockedStock;
	}

	public void setBlockedStock(Integer blockedStock) {
		this.blockedStock = blockedStock;
	}

	@Column(name = "sales")
	public Integer getSales() {
		return this.sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
	}

	@Column(name = "storeLocation")
	public String getStoreLocation() {
		return this.storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	@Column(name = "weight")
	public Integer getWeight() {
		return this.weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Column(name = "length")
	public Integer getLength() {
		return this.length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Column(name = "width")
	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Column(name = "height")
	public Integer getHeight() {
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Column(name = "barcode", length = 20)
	public String getBarcode() {
		return this.barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@Column(name = "sampleImage")
	public String getSampleImage() {
		return this.sampleImage;
	}

	public void setSampleImage(String sampleImage) {
		this.sampleImage = sampleImage;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopSku")
	public Set<TbShopSkuSpecificationAndValue> getTbShopSkuSpecificationAndValues() {
		return this.tbShopSkuSpecificationAndValues;
	}

	public void setTbShopSkuSpecificationAndValues(Set<TbShopSkuSpecificationAndValue> tbShopSkuSpecificationAndValues) {
		this.tbShopSkuSpecificationAndValues = tbShopSkuSpecificationAndValues;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tbShopSku")
	public Set<TbShopOrderItem> getTbShopOrderItems() {
		return this.tbShopOrderItems;
	}

	public void setTbShopOrderItems(Set<TbShopOrderItem> tbShopOrderItems) {
		this.tbShopOrderItems = tbShopOrderItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopSku")
	public Set<TbShopCartItem> getTbShopCartItems() {
		return this.tbShopCartItems;
	}

	public void setTbShopCartItems(Set<TbShopCartItem> tbShopCartItems) {
		this.tbShopCartItems = tbShopCartItems;
	}

}