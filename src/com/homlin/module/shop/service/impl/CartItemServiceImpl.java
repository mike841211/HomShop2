package com.homlin.module.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopCartItemDao;
import com.homlin.module.shop.model.TbShopCartItem;
import com.homlin.module.shop.service.CartItemService;

@Service
public class CartItemServiceImpl extends BaseServiceImpl<TbShopCartItem, String> implements CartItemService {

	@Autowired
	public void setBaseDao(TbShopCartItemDao baseDao) {
		super.setBaseDao(baseDao);
	}

}
