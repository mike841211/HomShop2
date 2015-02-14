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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TbShopMember entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_shop_member")
public class TbShopMember implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private String id;
	private TbShopMemberGrade tbShopMemberGrade;
	private String createDate;
	private String modifyDate;
	private String cardno;
	private String username;
	private String password;
	private BigDecimal deposit;
	private BigDecimal depositAddup;
	private BigDecimal score;
	private BigDecimal scoreAddup;
	private String enabled;
	private Integer loginCount;
	private String loginDate;
	private String loginIp;
	private Integer loginFailureCount;
	private String loginFailureDate;
	private String registDate;
	private String registIp;
	private String registIparea;
	private String name;
	private String gender;
	private String birthday;
	private String email;
	private String mobile;
	private String phone;
	private String fax;
	private String qq;
	private String wangwang;
	private String safeQuestion;
	private String safeAnswer;
	private String areaCode;
	private String province;
	private String city;
	private String district;
	private String address;
	private String postcode;
	private String remark;
	private String passwordRecoverKey;
	private String passwordRecoverDate;
	private Set<TbShopOrder> tbShopOrders = new HashSet<TbShopOrder>(0);
	private Set<TbShopMemberDepositLog> tbShopMemberDepositLogs = new HashSet<TbShopMemberDepositLog>(0);
	private Set<TbShopMemberScoreLog> tbShopMemberScoreLogs = new HashSet<TbShopMemberScoreLog>(0);
	private Set<TbShopProduct> favoriteProducts = new HashSet<TbShopProduct>(0);
	private TbShopCart tbShopCart;
	private Set<TbShopMemberReceiver> tbShopMemberReceivers = new HashSet<TbShopMemberReceiver>(0);
	private Set<TbShopMemberOauth2> tbShopMemberOauth2s = new HashSet<TbShopMemberOauth2>(0);
	private TbPluginSignin tbPluginSignin;
	private List<TbPluginSigninLog> tbPluginSigninLogs = new ArrayList<TbPluginSigninLog>();

	// Constructors

	/** default constructor */
	public TbShopMember() {
	}

	public TbShopMember(String id) {
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
	@JoinColumn(name = "grade_id")
	public TbShopMemberGrade getTbShopMemberGrade() {
		return this.tbShopMemberGrade;
	}

	public void setTbShopMemberGrade(TbShopMemberGrade tbShopMemberGrade) {
		this.tbShopMemberGrade = tbShopMemberGrade;
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

	@Column(name = "cardno")
	public String getCardno() {
		return this.cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	@Column(name = "username", nullable = false)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "deposit", nullable = false, precision = 18)
	public BigDecimal getDeposit() {
		return this.deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	@Column(name = "depositAddup", nullable = false, precision = 18)
	public BigDecimal getDepositAddup() {
		return this.depositAddup;
	}

	public void setDepositAddup(BigDecimal depositAddup) {
		this.depositAddup = depositAddup;
	}

	@Column(name = "score", nullable = false, precision = 18, scale = 0)
	public BigDecimal getScore() {
		return this.score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	@Column(name = "scoreAddup", nullable = false, precision = 18, scale = 0)
	public BigDecimal getScoreAddup() {
		return this.scoreAddup;
	}

	public void setScoreAddup(BigDecimal scoreAddup) {
		this.scoreAddup = scoreAddup;
	}

	@Column(name = "enabled", length = 1)
	public String getEnabled() {
		return this.enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "loginCount", nullable = false)
	public Integer getLoginCount() {
		return this.loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	@Column(name = "loginDate", length = 23)
	public String getLoginDate() {
		return this.loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	@Column(name = "loginIP", length = 50)
	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Column(name = "loginFailureCount", nullable = false)
	public Integer getLoginFailureCount() {
		return this.loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	@Column(name = "loginFailureDate", length = 23)
	public String getLoginFailureDate() {
		return this.loginFailureDate;
	}

	public void setLoginFailureDate(String loginFailureDate) {
		this.loginFailureDate = loginFailureDate;
	}

	@Column(name = "registDate", length = 23)
	public String getRegistDate() {
		return this.registDate;
	}

	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}

	@Column(name = "registIP", length = 50)
	public String getRegistIp() {
		return this.registIp;
	}

	public void setRegistIp(String registIp) {
		this.registIp = registIp;
	}

	@Column(name = "registIPArea")
	public String getRegistIparea() {
		return this.registIparea;
	}

	public void setRegistIparea(String registIparea) {
		this.registIparea = registIparea;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "gender", length = 1)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "birthday", length = 23)
	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Column(name = "fax")
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "qq")
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "wangwang")
	public String getWangwang() {
		return this.wangwang;
	}

	public void setWangwang(String wangwang) {
		this.wangwang = wangwang;
	}

	@Column(name = "safeQuestion")
	public String getSafeQuestion() {
		return this.safeQuestion;
	}

	public void setSafeQuestion(String safeQuestion) {
		this.safeQuestion = safeQuestion;
	}

	@Column(name = "safeAnswer")
	public String getSafeAnswer() {
		return this.safeAnswer;
	}

	public void setSafeAnswer(String safeAnswer) {
		this.safeAnswer = safeAnswer;
	}

	@Column(name = "areaCode", length = 6)
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "province")
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "city")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "district")
	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
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

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "passwordRecoverKey")
	public String getPasswordRecoverKey() {
		return this.passwordRecoverKey;
	}

	public void setPasswordRecoverKey(String passwordRecoverKey) {
		this.passwordRecoverKey = passwordRecoverKey;
	}

	@Column(name = "passwordRecoverDate", length = 23)
	public String getPasswordRecoverDate() {
		return this.passwordRecoverDate;
	}

	public void setPasswordRecoverDate(String passwordRecoverDate) {
		this.passwordRecoverDate = passwordRecoverDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tbShopMember")
	public Set<TbShopOrder> getTbShopOrders() {
		return this.tbShopOrders;
	}

	public void setTbShopOrders(Set<TbShopOrder> tbShopOrders) {
		this.tbShopOrders = tbShopOrders;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopMember")
	public Set<TbShopMemberDepositLog> getTbShopMemberDepositLogs() {
		return this.tbShopMemberDepositLogs;
	}

	public void setTbShopMemberDepositLogs(Set<TbShopMemberDepositLog> tbShopMemberDepositLogs) {
		this.tbShopMemberDepositLogs = tbShopMemberDepositLogs;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopMember")
	public Set<TbShopMemberScoreLog> getTbShopMemberScoreLogs() {
		return this.tbShopMemberScoreLogs;
	}

	public void setTbShopMemberScoreLogs(Set<TbShopMemberScoreLog> tbShopMemberScoreLogs) {
		this.tbShopMemberScoreLogs = tbShopMemberScoreLogs;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "tb_shop_member_favorite", joinColumns = {
			@JoinColumn(name = "member_id", nullable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "product_id", nullable = false, updatable = false) })
	public Set<TbShopProduct> getFavoriteProducts() {
		return this.favoriteProducts;
	}

	public void setFavoriteProducts(Set<TbShopProduct> favoriteProducts) {
		this.favoriteProducts = favoriteProducts;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopMember")
	public TbShopCart getTbShopCart() {
		return this.tbShopCart;
	}

	public void setTbShopCart(TbShopCart tbShopCart) {
		this.tbShopCart = tbShopCart;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopMember")
	public Set<TbShopMemberReceiver> getTbShopMemberReceivers() {
		return this.tbShopMemberReceivers;
	}

	public void setTbShopMemberReceivers(Set<TbShopMemberReceiver> tbShopMemberReceivers) {
		this.tbShopMemberReceivers = tbShopMemberReceivers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopMember")
	public Set<TbShopMemberOauth2> getTbShopMemberOauth2s() {
		return this.tbShopMemberOauth2s;
	}

	public void setTbShopMemberOauth2s(Set<TbShopMemberOauth2> tbShopMemberOauth2s) {
		this.tbShopMemberOauth2s = tbShopMemberOauth2s;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopMember")
	public TbPluginSignin getTbPluginSignin() {
		return this.tbPluginSignin;
	}

	public void setTbPluginSignin(TbPluginSignin tbPluginSignin) {
		this.tbPluginSignin = tbPluginSignin;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tbShopMember")
	@OrderBy("signDate asc")
	public List<TbPluginSigninLog> getTbPluginSigninLogs() {
		return this.tbPluginSigninLogs;
	}

	public void setTbPluginSigninLogs(List<TbPluginSigninLog> tbPluginSigninLogs) {
		this.tbPluginSigninLogs = tbPluginSigninLogs;
	}

}