package com.homlin.module.shop.controller.shop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.AppConstants;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.controller.shop.member.BaseMemberController;
import com.homlin.module.shop.execption.MemberLoginedRequiredException;
import com.homlin.module.shop.model.TbShopCart;
import com.homlin.module.shop.model.TbShopCartItem;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberGrade;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.module.shop.model.TbShopOrder.OrderStatus;
import com.homlin.module.shop.model.TbShopOrder.PaymentMethod;
import com.homlin.module.shop.model.TbShopOrder.PaymentStatus;
import com.homlin.module.shop.model.TbShopOrder.ShippingStatus;
import com.homlin.module.shop.model.TbShopOrderItem;
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.module.shop.model.TbShopProductComment;
import com.homlin.module.shop.model.TbShopShippingMethod;
import com.homlin.module.shop.model.TbShopSku;
import com.homlin.module.shop.plugin.payment.alipay.AlipayConfig;
import com.homlin.module.shop.plugin.payment.tenpay.TenpayConfig;
import com.homlin.module.shop.plugin.payment.unionpay.upmp.conf.UpmpConfig;
import com.homlin.module.shop.plugin.payment.wxpay.WxpayConfig;
import com.homlin.module.shop.service.CartService;
import com.homlin.module.shop.service.OrderService;
import com.homlin.module.shop.service.ProductCommentService;
import com.homlin.module.shop.service.ShippingMethodService;
import com.homlin.module.shop.service.SkuService;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseMemberController {

	private final String RETURN_PATH = "/order";

	@Autowired
	SkuService skuService;

	@Autowired
	OrderService orderService;

	@Autowired
	CartService cartService;

	@Autowired
	ShippingMethodService shippingMethodService;

	@Autowired
	ProductCommentService productCommentService;

	// [[ 直接购买 ]]

	// 填写订单明细
	@RequestMapping(value = "/directorder")
	public String directorder(Model model, String skuid, Integer quantity) throws Exception {

		if (quantity == null || quantity < 1) {
			addActionError("参数错误");
			return getTemplatePath() + "/error";
		}

		TbShopSku sku = skuService.get(skuid);
		if (sku == null) {
			addActionError("没有找到商品");
			return getTemplatePath() + "/error";
		}
		if (sku.getStock() < quantity) {
			addActionError("当前商品库存不足");
			return getTemplatePath() + "/error";
		}
		if (!AppConstants.TRUE.equals(sku.getTbShopProduct().getIsSale())) {
			addActionError("当前商品不可购买");
			return getTemplatePath() + "/error";
		}
		model.addAttribute("sku", sku);

		Integer totalWeight = sku.getWeight() * quantity;
		model.addAttribute("totalWeight", totalWeight);

		BigDecimal totalPrice;
		if (isMemberLogined()) {
			// request.getRequestDispatcher("/member/order/info.htm").forward(request, response);
			// return null;
			// return "redirect:/member/order/info.htm?" + request.getQueryString();

			TbShopMember member = loadMember();
			model.addAttribute("member", member);

			// 会员折扣
			BigDecimal discount = null;
			TbShopMemberGrade grade = member.getTbShopMemberGrade();
			if (grade != null) {
				discount = grade.getDiscount();
			}
			if (discount == null) {
				discount = BigDecimal.ONE;
			}
			model.addAttribute("discount", discount); // 98折.movePointRight(2)
			totalPrice = sku.getPrice().multiply(discount).multiply(new BigDecimal(quantity));
			model.addAttribute("memberScore", member.getScore());
		} else {
			totalPrice = sku.getPrice().multiply(new BigDecimal(quantity));
		}
		model.addAttribute("totalPrice", totalPrice);

		return getTemplatePath(RETURN_PATH) + "/directorder";
	}

	@ResponseBody
	@RequestMapping(value = "/directorder/save", method = RequestMethod.POST)
	public String directorder_save(TbShopOrder order, String skuid, Integer quantity, String shippingMethodId
			, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (quantity == null || quantity < 1) {
			return ajaxJsonErrorMessage("参数错误！");
		}

		TbShopSku sku = skuService.get(skuid);
		if (sku == null) {
			return ajaxJsonErrorMessage("没有找到商品！");
		}
		if (sku.getStock() < quantity) {
			return ajaxJsonErrorMessage("当前商品库存不足！");
		}

		if (StringUtils.isBlank(shippingMethodId)) {
			return ajaxJsonErrorMessage("请选择配送方式！");
		}
		TbShopShippingMethod shippingMethod = shippingMethodService.get(shippingMethodId);
		if (shippingMethod == null) {
			return ajaxJsonErrorMessage("当前不支持您选择配送方式！");
		}

		// 装车
		TbShopCart cart = new TbShopCart();
		TbShopCartItem tbShopCartItem = new TbShopCartItem();
		tbShopCartItem.setTbShopCart(cart);
		tbShopCartItem.setTbShopSku(sku);
		tbShopCartItem.setQuantity(quantity);
		cart.getTbShopCartItems().add(tbShopCartItem);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shippingMethodId", shippingMethodId);
		if (isMemberLogined()) {
			TbShopMember member = loadMember();
			cart.setTbShopMember(member);
		} else {
			params.put("tuid", getTuid(response));
		}

		try {
			order = orderService.create(order, cart, params);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("", order.getId());

	}

	// ]] 直接购买 ]]

	// [[ 通过购物车购买 ]]

	// 没有返回NULL
	private TbShopCart getCart() throws Exception {
		boolean logined = isMemberLogined();
		if (!logined) {
			if (AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.SHOP_CART_NEED_LOGIN))) {
				throw new MemberLoginedRequiredException("请先登入");
			}
		}

		TbShopCart cart = null;
		if (logined) {
			cart = cartService.getCart(getQueryMember());
		} else {
			String tuid = getTuid();
			if (null != tuid) {
				cart = cartService.getCartByTuid(tuid);
			}
		}
		return cart;
	}

	// 填写订单明细
	@RequestMapping(value = "/cartorder")
	public String cartorder(Model model) throws Exception {
		TbShopCart cart = null;
		try {
			cart = getCart();
		} catch (MemberLoginedRequiredException e) {
			return "redirect:../member/login.htm";
		}

		if (cart == null || cart.getTbShopCartItems().size() < 1) {
			addActionError("当前没有选购商品");
			return getTemplatePath() + "/error";
		}

		model.addAttribute("cart", cart);

		if (isMemberLogined()) {
			TbShopMember member = loadMember();
			model.addAttribute("member", member);

			// 会员折扣
			BigDecimal discount = null;
			TbShopMemberGrade grade = member.getTbShopMemberGrade();
			if (grade != null) {
				discount = grade.getDiscount();
			}
			if (discount == null) {
				discount = BigDecimal.ONE;
			}
			model.addAttribute("discount", discount); // 98折.movePointRight(2)
			model.addAttribute("memberScore", member.getScore());
		}

		String token = RandomStringUtils.randomAlphanumeric(32);
		getSession().setAttribute(AppConstants.SESSION_ORDERINFO_TOKEN, token);
		model.addAttribute("token", token);
		return getTemplatePath(RETURN_PATH) + "/cartorder";
	}

	@ResponseBody
	@RequestMapping(value = "/cartorder/save", method = RequestMethod.POST)
	public String cartorder_save(TbShopOrder order, @RequestParam(value = "token", defaultValue = "") String token, String shippingMethodId)
			throws Exception {

		Object SESSION_ORDERINFO_TOKEN = getSession(AppConstants.SESSION_ORDERINFO_TOKEN);
		if (SESSION_ORDERINFO_TOKEN == null || !token.equals(SESSION_ORDERINFO_TOKEN.toString())) {
			return ajaxJsonErrorMessage("您的订单状态信息有变更，请刷新确认！");
		}

		if (StringUtils.isBlank(shippingMethodId)) {
			return ajaxJsonErrorMessage("请选择配送方式！");
		}
		TbShopShippingMethod shippingMethod = shippingMethodService.get(shippingMethodId);
		if (shippingMethod == null) {
			return ajaxJsonErrorMessage("当前不支持您选择配送方式！");
		}

		TbShopCart cart = null;
		try {
			cart = getCart();
		} catch (MemberLoginedRequiredException e) {
			return "redirect:../member/login.htm";
		}
		if (cart == null || cart.getTbShopCartItems().size() == 0) {
			return ajaxJsonErrorMessage("购物车目前没有加入任何商品！");
		}

		// [[ 判断状态 ]]
		Set<TbShopCartItem> cartItems = cart.getTbShopCartItems();
		for (TbShopCartItem tbShopCartItem : cartItems) {
			TbShopSku sku = tbShopCartItem.getTbShopSku();
			// 判断库存
			if (sku.getStock() <= 0) {
				return ajaxJsonErrorMessage("商品[" + sku.getSn() + "]库存不足！");
			}
			// 判断上架
			if (!AppConstants.TRUE.equals(sku.getTbShopProduct().getIsSale())) {
				return ajaxJsonErrorMessage("商品[" + sku.getSn() + "]已下架！");
			}
		}
		// ]] 判断状态 ]]

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shippingMethodId", shippingMethodId);
		if (isNotMemberLogined()) {
			params.put("tuid", getTuid());
		}
		try {
			order = orderService.create(order, cart, params);
			setSession(AppConstants.SESSION_ORDERINFO_TOKEN, null);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("", order.getId());

	}

	// ]] 通过购物车购买 ]]

	@RequestMapping({ "/pay/{id}" })
	public String pay(@PathVariable String id, Model model) throws Exception {

		TbShopOrder order = orderService.get(id);
		if (!isAvailable(order)) {
			addActionError("没有找到匹配信息");
			return getTemplatePath() + "/error";
		}
		model.addAttribute("order", order);

		List<Map<String, String>> payments = new ArrayList<Map<String, String>>();
		if (WxpayConfig.enabled && Util.isWxpayJsApiSupported(getRequest())) {
			Map<String, String> payment = new HashMap<String, String>();
			payment.put("text", "微信支付");
			payment.put("url", "/payment/wxpay.htm?type=order&id=" + id + "&showwxpaytitle=1");
			payment.put("icon", "icon-wx");
			payments.add(payment);
		}
		if (TenpayConfig.enabled) {
			Map<String, String> payment = new HashMap<String, String>();
			payment.put("text", "财付通支付");
			payment.put("url", "/payment/tenpay.do?type=order&id=" + id);
			payment.put("icon", "icon-tenpay");
			payments.add(payment);
		}
		if (AlipayConfig.enabled) {
			Map<String, String> payment = new HashMap<String, String>();
			payment.put("text", "支付宝支付");
			payment.put("url", "/payment/alipay.htm?type=order&way=mobile&id=" + id);
			payment.put("icon", "icon-alipay");
			payments.add(payment);
		}
		if (UpmpConfig.enabled && !Util.isWeixinBrowser(getRequest())) {
			Map<String, String> payment = new HashMap<String, String>();
			payment.put("text", "银联在线支付");
			payment.put("url", "/payment/upmp.do?type=order&id=" + id);
			payment.put("icon", "icon-upmp");
			payments.add(payment);
		}
		model.addAttribute("payments", payments);

		return getTemplatePath(RETURN_PATH) + "/pay";
	}

	@RequestMapping({ "/list" })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
			@RequestParam(value = "pagesize", defaultValue = "10") int pageSize, Model model, HttpServletResponse response)
			throws Exception {

		Map<String, Object> params = getAllQueryParams();
		if (isMemberLogined()) {
			params.put("member_id", getMemberId());
		} else {
			params.put("tuid", getTuid(response));
		}

		Pager pager = new Pager(pageIndex, pageSize, params);
		pager = orderService.getPagedList(pager);
		model.addAttribute("pager", pager);

		return getTemplatePath(RETURN_PATH) + "/list";
	}

	@RequestMapping({ "/view/{id}" })
	public String view(@PathVariable String id, Model model) throws Exception {

		TbShopOrder order = orderService.get(id);
		if (!isAvailable(order)) {
			addActionError("没有找到匹配信息");
			return getTemplatePath() + "/error";
		}
		model.addAttribute("order", order);
		return getTemplatePath(RETURN_PATH) + "/view";
	}

	@ResponseBody
	@RequestMapping(value = "/getShippingFee", method = RequestMethod.POST)
	public String getShippingFee(String shippingMethodId, Integer totalWeight, String areaCode)
			throws Exception {

		// TODO cacheutil 是否全部免运费

		if (StringUtils.isBlank(shippingMethodId)) {
			return ajaxJsonErrorMessage("请选择配送方式！");
		}
		TbShopShippingMethod shippingMethod = shippingMethodService.get(shippingMethodId);
		if (shippingMethod == null) {
			return ajaxJsonErrorMessage("当前不支持您选择配送方式！");
		}

		BigDecimal shippingFee = shippingMethodService.getShippingFee(shippingMethodId, totalWeight, areaCode);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("shippingFee", shippingFee);

		return ajaxJsonSuccessMessage("", result);

	}

	// 是不是我的订单
	private boolean isAvailable(TbShopOrder order) {
		boolean available = false;
		if (order == null) {
			available = false;
		} else {
			if (order.getTbShopMember() == null) {
				available = order.getTuid().equals(getTuid());
			} else {
				available = order.getTbShopMember().getId().equals(getMemberId());
			}
		}
		return available;
	}

	// 取消订单
	@ResponseBody
	@RequestMapping(value = "/cancelled", method = RequestMethod.POST)
	public String cancelled(String id, OrderStatus status) throws Exception {
		if (StringUtils.isBlank(id)) {
			return ajaxJsonErrorMessage("参数错误");
		}

		TbShopOrder order = orderService.get(id);
		if (!isAvailable(order)) {
			addActionError("没有找到匹配信息");
			return getTemplatePath() + "/error";
		}
		if (order.getOrderStatus() != OrderStatus.unconfirmed) {
			return ajaxJsonErrorMessage("当前订单已处理过，请联系客服取消订单！");
		}

		orderService.cancelled(order);
		// todo orderlog("修改订单", "取消订单，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 确认订单已完成
	@ResponseBody
	@RequestMapping(value = "/completed", method = RequestMethod.POST)
	public String completed(String id, OrderStatus status) throws Exception {
		if (StringUtils.isBlank(id)) {
			return ajaxJsonErrorMessage("参数错误");
		}

		TbShopOrder order = orderService.get(id);
		if (!isAvailable(order)) {
			addActionError("没有找到匹配信息");
			return getTemplatePath() + "/error";
		}
		if (order.getOrderStatus() == OrderStatus.cancelled || order.getOrderStatus() == OrderStatus.completed) {
			return ajaxJsonErrorMessage("当前订单不可编辑！");
		}
		if (order.getPaymentStatus() == PaymentStatus.unpaid || order.getPaymentStatus() == PaymentStatus.partialPayment) {
			return ajaxJsonErrorMessage("当前订单有未付款！");
		}
		if (order.getShippingStatus() == ShippingStatus.unshipped || order.getShippingStatus() == ShippingStatus.partialShipment) {
			return ajaxJsonErrorMessage("当前订单未全部发货！");
		}

		orderService.completed(order);
		// todo orderlog("修改订单", "确认订单完成，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 订单支付情况
	@ResponseBody
	@RequestMapping(value = "/getPaymentStatus", method = RequestMethod.POST)
	public String getPaymentStatus(String id) throws Exception {

		TbShopOrder order = orderService.get(id);
		if (null == order) {
			return ajaxJsonErrorMessage("没有找到订单");
		}
		if (order.getPaymentMethod() != PaymentMethod.UNIONPAY) {
			// System.out.println(getMemberId()); // TODO UNIONPAY 付款跳转后得不到 getMemberId()
			if (!isAvailable(order)) {
				return ajaxJsonErrorMessage("没有找到匹配信息");
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("paymentStatus", order.getPaymentStatus());
		result.put("paymentMethod", order.getPaymentMethod());
		result.put("paymentCode", order.getPaymentCode());

		return ajaxJsonSuccessMessage("", result);
	}

	// 购买评价页面
	@RequestMapping(value = { "/comment/{id}" }, method = RequestMethod.GET)
	public String comment(@PathVariable String id, Model model) throws Exception {

		if (!AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.SHOP_PRODUCT_COMMENT_ENABLED))) {
			return errorPage("评价功能已关闭");
		}

		TbShopOrder order = orderService.get(id);
		if (!isAvailable(order)) {
			return errorPage("没有找到匹配信息");
		}
		if (order.getOrderStatus() != OrderStatus.completed) {
			return errorPage("当前订单未完成，不可评价！");
		}
		model.addAttribute("order", order);
		return getTemplatePath(RETURN_PATH) + "/comment";
	}

	// 提交购买评价
	@ResponseBody
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public String comment(String item_id, Integer score, String contents) throws Exception {

		if (!AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.SHOP_PRODUCT_COMMENT_ENABLED))) {
			return ajaxJsonErrorMessage("评价功能已关闭");
		}

		TbShopOrderItem orderItem = orderService.getTbShopOrderItem(item_id);
		if (null == orderItem) {
			return ajaxJsonErrorMessage("参数错误：item_id");
		}
		TbShopOrder tbShopOrder = orderItem.getTbShopOrder();
		if (tbShopOrder.getOrderStatus() != OrderStatus.completed) {
			return ajaxJsonErrorMessage("当前订单未完成，不可评价！");
		}
		if (!isAvailable(tbShopOrder)) {
			return ajaxJsonErrorMessage("参数错误：item_id");
		}
		if (null != orderItem.getTbShopProductComment()) {
			return ajaxJsonErrorMessage("本次购买已评价过");
		}

		TbShopProductComment comment = new TbShopProductComment();
		String datetime = Util.getNowDateTimeString();
		comment.setCreateDate(datetime);
		comment.setModifyDate(datetime);
		comment.setTbShopOrderItem(orderItem);
		if (contents != null) {
			contents = contents.replace("<", "&lt;");
		}
		comment.setContents(contents);
		comment.setScore(score);
		comment.setTbShopOrderItem(orderItem);
		TbShopSku sku = orderItem.getTbShopSku();
		comment.setTbShopSku(sku);
		if (null != sku) {
			comment.setSpecifications(sku.getSpecificationValueName());
			TbShopProduct product = sku.getTbShopProduct();
			comment.setTbShopProduct(product);
			if (null != product) {
				comment.setProductName(product.getName());
			}
		}
		comment.setTbShopMember(orderItem.getTbShopOrder().getTbShopMember());
		comment.setUsername(orderItem.getTbShopOrder().getMemberUsername());
		comment.setIp(getRequest().getRemoteAddr());
		comment.setIsDelete(AppConstants.FALSE);
		if (AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.SHOP_PRODUCT_COMMENT_DEFAULTSHOW))) {
			comment.setIsShow(AppConstants.TRUE);
		} else {
			comment.setIsShow(AppConstants.FALSE);
		}
		try {
			productCommentService.save(comment);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}
}
