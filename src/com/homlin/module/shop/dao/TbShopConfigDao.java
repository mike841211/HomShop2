package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopConfig;

public interface TbShopConfigDao extends BaseDao<TbShopConfig, String> {

	void updateConfigValue(TbShopConfig tbShopConfig);

}
