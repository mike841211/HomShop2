package com.homlin.module.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbWxAutoreply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_wx_autoreply")
public class TbWxAutoreply implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private String createDate;
	private String modifyDate;
	private String replyType;
	private String title;
	private String author;
	private String summary;
	private String content;
	private Integer hits;
	private String attachments;
	private String isShow;
	private Integer topIndex;
	private String cover;
	private String showCover;
	private String keyword;
	private String link;
	private String matching;

	// Constructors

	/** default constructor */
	public TbWxAutoreply() {
	}

	public TbWxAutoreply(String id) {
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

	@Column(name = "replyType", nullable = false)
	public String getReplyType() {
		return this.replyType;
	}

	public void setReplyType(String replyType) {
		this.replyType = replyType;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "author")
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "summary")
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

	@Column(name = "cover")
	public String getCover() {
		return this.cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	@Column(name = "showCover", length = 1)
	public String getShowCover() {
		return this.showCover;
	}

	public void setShowCover(String showCover) {
		this.showCover = showCover;
	}

	@Column(name = "keyword")
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Column(name = "link")
	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	@Column(name = "matching")
	public void setMatching(String matching) {
		this.matching = matching;
	}

	public String getMatching() {
		return matching;
	}

}