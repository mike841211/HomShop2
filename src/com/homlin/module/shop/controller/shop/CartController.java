package com.homlin.module.shop.controller.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.AppConstants;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.controller.shop.member.BaseMemberController;
import com.homlin.module.shop.execption.MemberLoginedRequiredException;
import com.homlin.module.shop.model.TbShopCart;
import com.homlin.module.shop.model.TbShopCartItem;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberGrade;
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.module.shop.model.TbShopSku;
import com.homlin.module.shop.service.CartItemService;
import com.homlin.module.shop.service.CartService;
import com.homlin.module.shop.service.ProductService;
import com.homlin.module.shop.service.SkuService;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.Util;

@Controller
@RequestMapping({ "/cart" })
public class CartController extends BaseMemberController {

	// TODO 登入会员后是否合并购物车

	@Autowired
	CartService cartService;

	@Autowired
	CartItemService cartItemService;

	@Autowired
	ProductService productService;

	@Autowired
	SkuService skuService;

	// 没有则创建
	private TbShopCart getCart(HttpServletResponse response) throws Exception {
		boolean logined = isMemberLogined();
		if (!logined) {
			if (AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.SHOP_CART_NEED_LOGIN))) {
				throw new MemberLoginedRequiredException("请先登入");
			}
		}

		TbShopCart cart = null;
		String tuid = null;
		if (logined) {
			cart = cartService.getCart(getQueryMember());
		} else {
			tuid = getTuid(response);
			cart = cartService.getCartByTuid(tuid);
		}
		if (null == cart) {
			cart = new TbShopCart();
			if (logined) {
				cart.setTbShopMember(getQueryMember());
			} else {
				cart.setCartKey(tuid);
			}
			String datetime = Util.getNowDateTimeString();
			cart.setCreateDate(datetime);
			cart.setModifyDate(datetime);
			cartService.save(cart);
		}
		return cart;
	}

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

	/**
	 * 重置订单信息TOKEN
	 * 
	 */
	private void resetOrderInfoToken() {
		setSession(AppConstants.SESSION_ORDERINFO_TOKEN, null);
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(String skuid, Integer quantity, HttpServletResponse response) throws Exception {
		TbShopCart cart = null;
		try {
			cart = getCart(response);
		} catch (MemberLoginedRequiredException e) {
			return ajaxJsonErrorUnloginMessage("请先登入");
		}

		TbShopSku sku = skuService.get(skuid);
		if (sku == null) {
			return ajaxJsonErrorMessage("商品不存在");
		}
		TbShopProduct product = sku.getTbShopProduct();
		if (!AppConstants.TRUE.equals(product.getIsSale())) {
			return ajaxJsonErrorMessage("商品已下架");
		}

		String datetime = Util.getNowDateTimeString();
		TbShopCartItem cartItem = cart.getCartItem(sku);
		if (cartItem != null) {
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartItem.setModifyDate(datetime);
		} else {
			cartItem = new TbShopCartItem();
			cartItem.setTbShopCart(cart);
			cartItem.setTbShopSku(sku);
			cartItem.setQuantity(quantity);
			cartItem.setCreateDate(datetime);
			cartItem.setModifyDate(datetime);
			cart.getTbShopCartItems().add(cartItem);
		}

		cart.setModifyDate(datetime);
		cartService.update(cart);

		resetOrderInfoToken();
		return ajaxJsonSuccessMessage();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, HttpServletResponse response) throws Exception {
		TbShopCart cart = null;
		try {
			cart = getCart(response);
		} catch (MemberLoginedRequiredException e) {
			return "redirect:../member/login.htm";
		}

		if (cart == null || cart.getTbShopCartItems().size() < 1) {
			addActionError("当前没有选购商品");
			return getTemplatePath() + "/error";
		}

		model.addAttribute("cart", cart);
		return getTemplatePath() + "/cart";
	}

	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String id, Integer quantity, HttpServletResponse response) throws Exception {
		boolean isMyCart = false; // 是否我的商品
		TbShopCartItem cartItem = cartItemService.get(id);
		if (cartItem != null) {
			TbShopCart cart = cartItem.getTbShopCart();
			if (cart.getTbShopMember() == null) {
				isMyCart = getTuid(response).equals(cart.getCartKey());
			} else {
				isMyCart = cart.getTbShopMember().getId().equals(getMemberId());
			}
		}
		if (isMyCart) {
			String datetime = Util.getNowDateTimeString();
			cartItem.setQuantity(quantity);
			cartItem.setModifyDate(datetime);
			cartItem.getTbShopCart().setModifyDate(datetime);
			cartItemService.update(cartItem);
			resetOrderInfoToken();
		} else {
			return ajaxJsonErrorMessage("参数错误，没有找到商品");
		}

		return ajaxJsonSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id, HttpServletResponse response) throws Exception {
		TbShopCartItem cartItem = cartItemService.get(id);
		if (cartItem == null) {
			return ajaxJsonErrorMessage("当前购物车没此商品");
		} else {
			boolean isMyCart = false; // 是否我的商品
			TbShopCart cart = cartItem.getTbShopCart();
			if (cart.getTbShopMember() == null) {
				isMyCart = getTuid(response).equals(cart.getCartKey());
			} else {
				isMyCart = cart.getTbShopMember().getId().equals(getMemberId());
			}
			if (isMyCart) {
				String datetime = Util.getNowDateTimeString();
				cart.setModifyDate(datetime);
				cartItemService.delete(cartItem);
				resetOrderInfoToken();
			} else {
				return ajaxJsonErrorMessage("参数错误，没有找到商品");
			}
		}

		return ajaxJsonSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public String clear() throws Exception {
		TbShopCart cart = null;
		try {
			cart = getCart();
		} catch (com.homlin.module.shop.execption.MemberLoginedRequiredException e) {
			return ajaxJsonErrorUnloginMessage("请先登入");
		}

		if (cart != null) {
			cartService.delete(cart);
			resetOrderInfoToken();
		}

		return ajaxJsonSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/getCartAmount")
	public String getCartAmount() throws Exception {
		Integer totalQuantity = 0;
		BigDecimal totalPrice = BigDecimal.ZERO;
		TbShopCart cart = null;
		try {
			cart = getCart();
		} catch (MemberLoginedRequiredException e) {
			return ajaxJsonErrorUnloginMessage("请先登入");
		}

		BigDecimal discount = null; // 会员折扣
		if (cart != null) {
			if (isMemberLogined()) {
				TbShopMember member = loadMember();
				TbShopMemberGrade grade = member.getTbShopMemberGrade();
				if (grade != null) {
					discount = grade.getDiscount();
				}
			}
			if (discount == null) {
				discount = BigDecimal.ONE;
			}
			Set<TbShopCartItem> items = cart.getTbShopCartItems();
			for (TbShopCartItem item : items) {
				totalQuantity += item.getQuantity();
				// 不同件数可能价格策略不一样
				totalPrice = totalPrice.add(item.getTbShopSku().getPrice().multiply(discount).multiply(new BigDecimal(item.getQuantity())));
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalQuantity", totalQuantity);
		result.put("totalPrice", totalPrice);
		result.put("discount", discount);

		return ajaxJsonSuccessMessage("", result);
	}

}
