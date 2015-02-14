package com.homlin.module.shop.dao;

import java.util.List;
import java.util.Map;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopProductBaseinfo;
import com.homlin.utils.Pager;

public interface TbShopProductBaseinfoDao extends BaseDao<TbShopProductBaseinfo, String> {

	Pager getPagedList(Pager pager);

	List<Map<String, Object>> getAllForSelect();

}
