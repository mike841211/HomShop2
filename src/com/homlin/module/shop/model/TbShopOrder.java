package com.homlin.module.shop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopOrder entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_order")
public class TbShopOrder implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public enum OrderStatus {
		unconfirmed("未确认"), confirmed("已确认"), completed("已完成"), cancelled("已取消");

		private String name;

		public String getName() {
			return this.name;
		}

		private OrderStatus(String name) {
			this.name = name;
		}

		public static OrderStatus valueOf(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return values()[ordinal];
		}
	}

	public enum PaymentStatus {
		unpaid("未支付"), partialPayment("部分支付"), paid("已支付"), partialRefunds("部分退款"), refunded("全额退款");

		private String name;

		public String getName() {
			return this.name;
		}

		private PaymentStatus(String name) {
			this.name = name;
		}

		public static PaymentStatus valueOf(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return values()[ordinal];
		}
	}

	public enum ShippingStatus {
		unshipped("未发货"), partialShipment("部分发货"), shipped("已发货"), partialReturns("部分退货"), returned("全部退货");

		private String name;

		public String getName() {
			return this.name;
		}

		private ShippingStatus(String name) {
			this.name = name;
		}

		public static ShippingStatus valueOf(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return values()[ordinal];
		}
	}

	public enum PaymentMethod {
		OTHER("其他"), ALIPAY("支付宝"), UNIONPAY("银联在线"), TENPAY("财付通"), WXPAY("微信支付"), JFPAY("积分抵扣");

		private String name;

		public String getName() {
			return this.name;
		}

		private PaymentMethod(String name) {
			this.name = name;
		}

		public static PaymentMethod valueOf(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return values()[ordinal];
		}
	}

	// Fields

	private String id;
	private TbShopMember tbShopMember;
	private String createDate;
	private String modifyDate;
	private String memberUsername;
	private String sn;
	private String name;
	private String mobile;
	private String phone;
	private String address;
	private String postcode;
	private String areaCode;
	private OrderStatus orderStatus;
	private ShippingStatus shippingStatus;
	private PaymentStatus paymentStatus;
	private PaymentMethod paymentMethod;
	private BigDecimal paymentFee; // 支付手续费
	private String paymentCode; // 支付单号
	private String paymentData; // 支付通知信息
	private String freightCollect; // 货到付款
	private String shippingMethod; // 配送方式
	private String shippingCompany; // 快递公司
	private String shippingCode; // 运单号
	private Integer totalQuantity; // 订单总数量
	private Integer totalWeight; // 商品总重量
	private BigDecimal totalPrice; // 商品总金额
	private BigDecimal adjustAmount; // 卖家金额调整
	private BigDecimal shippingFee; // 运费
	private BigDecimal totalAmount; // 订单合计金额
	private BigDecimal paidAmount; // 已支付金额
	private BigDecimal couponAmount; // 优惠券抵扣金额合计
	private BigDecimal useScoreAmount; // 积分抵扣金额合计
	private BigDecimal useScore; // 使用积分
	private BigDecimal gainScore; // 获得积分
	private BigDecimal gainScoreMutiple; // 获得积分转换参数
	private String invoice; // 发票信息
	private String buyerRemark;
	private String salerRemark;
	private String tuid; // 临时客户ID
	private List<TbShopOrderLog> tbShopOrderLogs = new ArrayList<TbShopOrderLog>();
	private Set<TbShopOrderItem> tbShopOrderItems = new HashSet<TbShopOrderItem>(0);

	// Constructors

	/** default constructor */
	public TbShopOrder() {
	}

	public TbShopOrder(String id) {
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

	@Column(name = "memberUsername")
	public String getMemberUsername() {
		return this.memberUsername;
	}

	public void setMemberUsername(String memberUsername) {
		this.memberUsername = memberUsername;
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

	@Column(name = "mobile")
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "postcode")
	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Column(name = "areaCode")
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "orderStatus")
	public OrderStatus getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "shippingStatus")
	public ShippingStatus getShippingStatus() {
		return this.shippingStatus;
	}

	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "paymentStatus")
	public PaymentStatus getPaymentStatus() {
		return this.paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "paymentMethod")
	public PaymentMethod getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Column(name = "paymentFee", nullable = false, precision = 18)
	public BigDecimal getPaymentFee() {
		return this.paymentFee;
	}

	public void setPaymentFee(BigDecimal paymentFee) {
		this.paymentFee = paymentFee;
	}

	@Column(name = "paymentCode")
	public String getPaymentCode() {
		return this.paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	@Column(name = "paymentData")
	public String getPaymentData() {
		return this.paymentData;
	}

	public void setPaymentData(String paymentData) {
		this.paymentData = paymentData;
	}

	@Column(name = "freightCollect", length = 1)
	public String getFreightCollect() {
		return this.freightCollect;
	}

	public void setFreightCollect(String freightCollect) {
		this.freightCollect = freightCollect;
	}

	@Column(name = "shippingMethod")
	public String getShippingMethod() {
		return this.shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	@Column(name = "shippingFee", precision = 18)
	public BigDecimal getShippingFee() {
		return this.shippingFee;
	}

	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	@Column(name = "shippingCompany")
	public String getShippingCompany() {
		return this.shippingCompany;
	}

	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}

	@Column(name = "shippingCode")
	public String getShippingCode() {
		return this.shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	@Column(name = "totalQuantity", nullable = false)
	public Integer getTotalQuantity() {
		return this.totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	@Column(name = "totalPrice", nullable = false, precision = 18)
	public BigDecimal getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Column(name = "totalWeight", nullable = false)
	public Integer getTotalWeight() {
		return this.totalWeight;
	}

	public void setTotalWeight(Integer totalWeight) {
		this.totalWeight = totalWeight;
	}

	@Column(name = "adjustAmount", nullable = false, precision = 18)
	public BigDecimal getAdjustAmount() {
		return this.adjustAmount;
	}

	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	@Column(name = "totalAmount", nullable = false, precision = 18)
	public BigDecimal getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "paidAmount", nullable = false, precision = 18)
	public BigDecimal getPaidAmount() {
		return this.paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	@Column(name = "couponAmount", nullable = false, precision = 18)
	public BigDecimal getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}

	@Column(name = "useScoreAmount", nullable = false, precision = 18)
	public BigDecimal getUseScoreAmount() {
		return useScoreAmount;
	}

	public void setUseScoreAmount(BigDecimal useScoreAmount) {
		this.useScoreAmount = useScoreAmount;
	}

	@Column(name = "useScore", nullable = false, precision = 18, scale = 0)
	public BigDecimal getUseScore() {
		return useScore;
	}

	public void setUseScore(BigDecimal useScore) {
		this.useScore = useScore;
	}

	@Column(name = "gainScore", nullable = false, precision = 18, scale = 0)
	public BigDecimal getGainScore() {
		return gainScore;
	}

	public void setGainScore(BigDecimal gainScore) {
		this.gainScore = gainScore;
	}

	@Column(name = "gainScoreMutiple", nullable = false, precision = 18, scale = 0)
	public BigDecimal getGainScoreMutiple() {
		return gainScoreMutiple;
	}

	public void setGainScoreMutiple(BigDecimal gainScoreMutiple) {
		this.gainScoreMutiple = gainScoreMutiple;
	}

	@Column(name = "invoice")
	public String getInvoice() {
		return this.invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	@Column(name = "buyerRemark")
	public String getBuyerRemark() {
		return this.buyerRemark;
	}

	public void setBuyerRemark(String buyerRemark) {
		this.buyerRemark = buyerRemark;
	}

	@Column(name = "salerRemark")
	public String getSalerRemark() {
		return this.salerRemark;
	}

	public void setSalerRemark(String salerRemark) {
		this.salerRemark = salerRemark;
	}

	@Column(name = "tuid")
	public String getTuid() {
		return this.tuid;
	}

	public void setTuid(String tuid) {
		this.tuid = tuid;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopOrder")
	@OrderBy("createDate asc")
	public List<TbShopOrderLog> getTbShopOrderLogs() {
		return this.tbShopOrderLogs;
	}

	public void setTbShopOrderLogs(List<TbShopOrderLog> tbShopOrderLogs) {
		this.tbShopOrderLogs = tbShopOrderLogs;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopOrder")
	@OrderBy("id asc")
	public Set<TbShopOrderItem> getTbShopOrderItems() {
		return this.tbShopOrderItems;
	}

	public void setTbShopOrderItems(Set<TbShopOrderItem> tbShopOrderItems) {
		this.tbShopOrderItems = tbShopOrderItems;
	}

	// /**
	// * 计算并赋值总金额
	// */
	// @Transient
	// public void setTotalAmount() {
	// this.setTotalAmount(this.getTotalPrice().add(this.getAdjustAmount()).add(this.getShippingFee())); // .add(this.getPaymentFee())
	// }

	/**
	 * 计算应付金额<br>
	 * 应付金额 unpaidAmount = 商品总金额 + 卖家金额调整 + 运费 - 优惠券抵扣金额合计 - 积分扣金额合计 - 已支付金额
	 * 
	 * @return
	 */
	@Transient
	public BigDecimal getUnpaidAmount() {
		return this.getTotalPrice().add(this.getAdjustAmount()).add(this.getShippingFee())
				.subtract(this.getCouponAmount()).subtract(this.getUseScoreAmount()).subtract(this.getPaidAmount());
	}

	/**
	 * 计算可获得积分金额
	 * 
	 * @return
	 */
	@Transient
	public BigDecimal getGainScoreAmount() {
		return this.getTotalPrice().add(this.getAdjustAmount())// .add(this.getShippingFee())
				.subtract(this.getCouponAmount()).subtract(this.getUseScoreAmount());
	}

}