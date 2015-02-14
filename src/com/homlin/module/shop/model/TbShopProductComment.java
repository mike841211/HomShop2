package com.homlin.module.shop.model;

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
 * TbShopProductComment entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_product_comment")
public class TbShopProductComment implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private String createDate;
	private String modifyDate;
	private TbShopProduct tbShopProduct;
	private String productName;
	private TbShopSku tbShopSku;
	private String specifications;
	private String contents;
	private String images;
	private TbShopOrderItem tbShopOrderItem;
	private Integer score;
	private TbShopMember tbShopMember;
	private String username;
	private String contact;
	private String ip;
	private String replyContent;
	private String replyDate;
	private String isShow;
	private String isDelete;

	// Constructors

	/** default constructor */
	public TbShopProductComment() {
	}

	public TbShopProductComment(String id) {
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	public TbShopProduct getTbShopProduct() {
		return this.tbShopProduct;
	}

	public void setTbShopProduct(TbShopProduct tbShopProduct) {
		this.tbShopProduct = tbShopProduct;
	}

	@Column(name = "productName")
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_id")
	public TbShopSku getTbShopSku() {
		return this.tbShopSku;
	}

	public void setTbShopSku(TbShopSku tbShopSku) {
		this.tbShopSku = tbShopSku;
	}

	@Column(name = "specifications")
	public String getSpecifications() {
		return this.specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	@Column(name = "contents", length = 1000)
	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@Column(name = "images", length = 1000)
	public String getImages() {
		return this.images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderItem_id")
	public TbShopOrderItem getTbShopOrderItem() {
		return this.tbShopOrderItem;
	}

	public void setTbShopOrderItem(TbShopOrderItem tbShopOrderItem) {
		this.tbShopOrderItem = tbShopOrderItem;
	}

	@Column(name = "score", nullable = false)
	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	public TbShopMember getTbShopMember() {
		return this.tbShopMember;
	}

	public void setTbShopMember(TbShopMember tbShopMember) {
		this.tbShopMember = tbShopMember;
	}

	@Column(name = "username", length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "contact", length = 50)
	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "ip", length = 50)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "replyContent", length = 1000)
	public String getReplyContent() {
		return this.replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	@Column(name = "replyDate", length = 23)
	public String getReplyDate() {
		return this.replyDate;
	}

	public void setReplyDate(String replyDate) {
		this.replyDate = replyDate;
	}

	@Column(name = "isShow", length = 1)
	public String getIsShow() {
		return this.isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	@Column(name = "isDelete", length = 1)
	public String getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

}