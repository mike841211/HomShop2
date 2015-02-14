package com.homlin.module.shop.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopConfigDao;
import com.homlin.module.shop.model.TbShopConfig;
import com.homlin.module.shop.service.ConfigService;

@Service
public class ConfigServiceImpl extends BaseServiceImpl<TbShopConfig, String> implements ConfigService {

	@Autowired
	public void setBaseDao(TbShopConfigDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopConfigDao configDao;

	@Override
	// @CacheEvict(value = CacheNames.config, allEntries = true)
	public void updateConfigs(Set<TbShopConfig> configs) {
		for (TbShopConfig tbShopConfig : configs) {
			configDao.updateConfigValue(tbShopConfig);
		}
	}

}
