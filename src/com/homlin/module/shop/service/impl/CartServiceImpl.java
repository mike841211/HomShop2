package com.homlin.module.shop.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopCartDao;
import com.homlin.module.shop.model.TbShopCart;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.service.CartService;
import com.homlin.utils.Util;

@Service
public class CartServiceImpl extends BaseServiceImpl<TbShopCart, String> implements CartService {

	@Autowired
	public void setBaseDao(TbShopCartDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopCartDao cartDao;

	@Override
	public TbShopCart getCart(TbShopMember member) {
		return cartDao.findOneByProperty("tbShopMember", member);
	}

	@Override
	public TbShopCart getCartByTuid(String tuid) {
		return cartDao.findOneByProperty("cartKey", tuid);
	}

	@Override
	public void evictExpired(Date expiredDate) {
		logger.info("启动定时清理过期购物车" + Util.getNowDateTimeString());
		try {
			cartDao.evictExpired(expiredDate);
		} catch (Exception e) {
			logger.error("CartJob.evictExpired 失败 - 定时清理过期购物车:" + Util.getNowDateTimeString());
			e.printStackTrace();
		}
	}

}
