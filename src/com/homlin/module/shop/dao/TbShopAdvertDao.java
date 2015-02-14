package com.homlin.module.shop.dao;

import java.util.List;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopAdvert;
import com.homlin.utils.Pager;

public interface TbShopAdvertDao extends BaseDao<TbShopAdvert, String> {

	Pager getPagedList(Pager pager);

	void setInuse(String[] ids, String inuse);

	List<TbShopAdvert> getByKeyword(String keyword);

}
