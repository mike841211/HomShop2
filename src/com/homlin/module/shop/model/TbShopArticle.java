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
 * TbShopArticle entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_article")
public class TbShopArticle implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopArticleCategory tbShopArticleCategory;
	private String createDate;
	private String modifyDate;
	private String author;
	private String title;
	private String subtitle;
	private String coverimg;
	private String summary;
	private String content;
	private Integer hits;
	private String metaKeyword;
	private String metaDescription;
	private String attachments;
	private String isShow;
	private Integer topIndex;

	// Constructors

	/** default constructor */
	public TbShopArticle() {
	}

	public TbShopArticle(String id) {
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
	@JoinColumn(name = "category_id")
	public TbShopArticleCategory getTbShopArticleCategory() {
		return this.tbShopArticleCategory;
	}

	public void setTbShopArticleCategory(TbShopArticleCategory tbShopArticleCategory) {
		this.tbShopArticleCategory = tbShopArticleCategory;
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

	@Column(name = "author", length = 50)
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "subtitle")
	public String getSubtitle() {
		return this.subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Column(name = "coverimg")
	public String getCoverimg() {
		return this.coverimg;
	}

	public void setCoverimg(String coverimg) {
		this.coverimg = coverimg;
	}

	@Column(name = "summary", length = 500)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Column(name = "content")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "hits", nullable = false)
	public Integer getHits() {
		return this.hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
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

	@Column(name = "attachments")
	public String getAttachments() {
		return this.attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	@Column(name = "isShow", length = 1)
	public String getIsShow() {
		return this.isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	@Column(name = "topIndex")
	public Integer getTopIndex() {
		return this.topIndex;
	}

	public void setTopIndex(Integer topIndex) {
		this.topIndex = topIndex;
	}

}