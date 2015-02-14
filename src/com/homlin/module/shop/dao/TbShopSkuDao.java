package com.homlin.module.shop.dao;

import java.util.List;
import java.util.Map;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopSku;

public interface TbShopSkuDao extends BaseDao<TbShopSku, String> {

	List<Map<String, Object>> getProductSkuList(String id);

}
