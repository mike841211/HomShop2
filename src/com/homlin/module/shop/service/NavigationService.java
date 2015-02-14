package com.homlin.module.shop.service;

import java.util.List;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopNavigation;

public interface NavigationService extends BaseService<TbShopNavigation, String> {

	List<TbShopNavigation> getAll();

	List<TbShopNavigation> getByPosition(String position);

	void setIsShow(String[] ids, String isShow);

}
