package com.homlin.module.shop.dao;

import java.util.List;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopNavigation;

public interface TbShopNavigationDao extends BaseDao<TbShopNavigation, String> {

	List<TbShopNavigation> getAll();

	List<TbShopNavigation> getByPosition(String position);

	void setIsShow(String[] ids, String isShow);

}
