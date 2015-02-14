package com.homlin.module.shop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_product")
public class TbShopProduct implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopBrand tbShopBrand;
	private TbShopProductBaseinfo tbShopProductBaseinfo;
	private TbShopProductCategory tbShopProductCategory;
	private String createDate;
	private String modifyDate;
	private String syssn;
	private String sn;
	private String name;
	private String slogan;
	private String barcode;
	private String unit;
	private BigDecimal price;
	private BigDecimal marketPrice;
	private Integer stock;
	private Integer blockedStock;
	private Integer sales;
	private String isSale;
	private String isFreeShipping;
	private String isNew;
	private String isHot;
	private String isRecomend;
	private String isPromotion;
	private String keyword;
	private String introduction;
	private String attachments;
	private String metaKeyword;
	private String metaDescription;
	private String sampleImage;
	private String imageStore;
	private String htmlPath;
	private BigDecimal score;
	private Integer scoreCount;
	private BigDecimal scoreTotal;
	private Set<TbShopSku> tbShopSkus = new HashSet<TbShopSku>(0);
	private List<TbShopSpecification> tbShopSpecifications = new ArrayList<TbShopSpecification>(0);
	private Set<TbShopMember> tbShopMembers = new HashSet<TbShopMember>(0);

	// Constructors

	/** default constructor */
	public TbShopProduct() {
	}

	public TbShopProduct(String id) {
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
	@JoinColumn(name = "brand_id")
	public TbShopBrand getTbShopBrand() {
		return this.tbShopBrand;
	}

	public void setTbShopBrand(TbShopBrand tbShopBrand) {
		this.tbShopBrand = tbShopBrand;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "baseinfo_id")
	public TbShopProductBaseinfo getTbShopProductBaseinfo() {
		return this.tbShopProductBaseinfo;
	}

	public void setTbShopProductBaseinfo(TbShopProductBaseinfo tbShopProductBaseinfo) {
		this.tbShopProductBaseinfo = tbShopProductBaseinfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	public TbShopProductCategory getTbShopProductCategory() {
		return this.tbShopProductCategory;
	}

	public void setTbShopProductCategory(TbShopProductCategory tbShopProductCategory) {
		this.tbShopProductCategory = tbShopProductCategory;
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

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "slogan")
	public String getSlogan() {
		return this.slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	@Column(name = "barcode", length = 20)
	public String getBarcode() {
		return this.barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@Column(name = "unit", length = 50)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	@Column(name = "isSale", length = 1)
	public String getIsSale() {
		return this.isSale;
	}

	public void setIsSale(String isSale) {
		this.isSale = isSale;
	}

	@Column(name = "isFreeShipping", length = 1)
	public String getIsFreeShipping() {
		return this.isFreeShipping;
	}

	public void setIsFreeShipping(String isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}

	@Column(name = "isNew", length = 1)
	public String getIsNew() {
		return this.isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	@Column(name = "isHot", length = 1)
	public String getIsHot() {
		return this.isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	@Column(name = "isRecomend", length = 1)
	public String getIsRecomend() {
		return this.isRecomend;
	}

	public void setIsRecomend(String isRecomend) {
		this.isRecomend = isRecomend;
	}

	@Column(name = "isPromotion", length = 1)
	public String getIsPromotion() {
		return this.isPromotion;
	}

	public void setIsPromotion(String isPromotion) {
		this.isPromotion = isPromotion;
	}

	@Column(name = "keyword")
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Column(name = "introduction")
	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Column(name = "attachments")
	public String getAttachments() {
		return this.attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	@Column(name = "metaKeyword")
	public String getMetaKeyword() {
		return this.metaKeyword;
	}

	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}

	@Column(name = "metaDescription")
	public String getMetaDescription() {
		return this.metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	@Column(name = "sampleImage")
	public String getSampleImage() {
		return this.sampleImage;
	}

	public void setSampleImage(String sampleImage) {
		this.sampleImage = sampleImage;
	}

	@Column(name = "imageStore")
	public String getImageStore() {
		return this.imageStore;
	}

	public void setImageStore(String imageStore) {
		this.imageStore = imageStore;
	}

	@Column(name = "htmlPath")
	public String getHtmlPath() {
		return this.htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	@Column(name = "score", precision = 18)
	public BigDecimal getScore() {
		return this.score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	@Column(name = "scoreCount")
	public Integer getScoreCount() {
		return this.scoreCount;
	}

	public void setScoreCount(Integer scoreCount) {
		this.scoreCount = scoreCount;
	}

	@Column(name = "scoreTotal", precision = 18)
	public BigDecimal getScoreTotal() {
		return this.scoreTotal;
	}

	public void setScoreTotal(BigDecimal scoreTotal) {
		this.scoreTotal = scoreTotal;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopProduct")
	public Set<TbShopSku> getTbShopSkus() {
		return this.tbShopSkus;
	}

	public void setTbShopSkus(Set<TbShopSku> tbShopSkus) {
		this.tbShopSkus = tbShopSkus;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_shop_product_specification", joinColumns = {
			@JoinColumn(name = "product_id", nullable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "specification_id", nullable = false, updatable = false) })
	@OrderBy("displayorder asc")
	public List<TbShopSpecification> getTbShopSpecifications() {
		return this.tbShopSpecifications;
	}

	public void setTbShopSpecifications(List<TbShopSpecification> tbShopSpecifications) {
		this.tbShopSpecifications = tbShopSpecifications;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "favoriteProducts")
	public Set<TbShopMember> getTbShopMembers() {
		return this.tbShopMembers;
	}

	public void setTbShopMembers(Set<TbShopMember> tbShopMembers) {
		this.tbShopMembers = tbShopMembers;
	}

}