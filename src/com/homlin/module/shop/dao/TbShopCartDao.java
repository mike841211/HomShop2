package com.homlin.module.shop.dao;

import java.util.Date;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopCart;

public interface TbShopCartDao extends BaseDao<TbShopCart, String> {

	void evictExpired(Date expiredDate);

}
