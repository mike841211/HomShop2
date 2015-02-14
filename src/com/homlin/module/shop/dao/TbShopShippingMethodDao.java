package com.homlin.module.shop.dao;

import java.util.List;
import java.util.Map;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopShippingMethod;
import com.homlin.utils.Pager;

public interface TbShopShippingMethodDao extends BaseDao<TbShopShippingMethod, String> {

	Pager getPagedList(Pager pager);

	List<Map<String, Object>> getAllForSelect();

}
