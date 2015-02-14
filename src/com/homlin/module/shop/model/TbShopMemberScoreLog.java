package com.homlin.module.shop.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopMemberScoreLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_member_score_log")
public class TbShopMemberScoreLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public enum Valtype {
		// 消费，充值，赠送，退款，签到
		consumption, recharge, present, refund, signin;
	}

	// Fields

	private String id;
	private TbShopMember tbShopMember;
	private String createDate;
	private String modifyDate;
	private BigDecimal val;
	private Valtype valtype;
	private String operator;
	private String remark;

	// Constructors

	/** default constructor */
	public TbShopMemberScoreLog() {
	}

	public TbShopMemberScoreLog(String id) {
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
	@JoinColumn(name = "member_id", nullable = false)
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

	@Column(name = "val", nullable = false, precision = 18, scale = 0)
	public BigDecimal getVal() {
		return this.val;
	}

	public void setVal(BigDecimal val) {
		this.val = val;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "valtype")
	public Valtype getValtype() {
		return this.valtype;
	}

	public void setValtype(Valtype valtype) {
		this.valtype = valtype;
	}

	@Column(name = "operator")
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}