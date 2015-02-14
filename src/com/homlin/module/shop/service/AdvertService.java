package com.homlin.module.shop.service;

import java.util.List;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopAdvert;
import com.homlin.utils.Pager;

public interface AdvertService extends BaseService<TbShopAdvert, String> {

	Pager getPagedList(Pager pager);

	void setInuse(String[] ids, String inuse);

	List<TbShopAdvert> getByKeyword(String keyword);

}
