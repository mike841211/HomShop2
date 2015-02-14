package com.homlin.module.shop.service;

import java.util.Date;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopCart;
import com.homlin.module.shop.model.TbShopMember;

public interface CartService extends BaseService<TbShopCart, String> {

	TbShopCart getCart(TbShopMember member);

	TbShopCart getCartByTuid(String tuid);

	void evictExpired(Date expiredDate);

}
