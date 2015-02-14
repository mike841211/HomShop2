package com.homlin.module.shop.dao.impl;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopConfigDao;
import com.homlin.module.shop.model.TbShopConfig;

@Repository
public class TbShopConfigDaoImpl extends BaseDaoImpl<TbShopConfig, String> implements TbShopConfigDao {

	@Override
	public void updateConfigValue(TbShopConfig tbShopConfig) {
		String hqlString = "update TbShopConfig set cfgValue=? where cfgKey=?";
		update(hqlString, tbShopConfig.getCfgValue(), tbShopConfig.getCfgKey());
	}

}
