package com.homlin.module.shop.service;

import java.util.Set;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopConfig;

public interface ConfigService extends BaseService<TbShopConfig, String> {

	void updateConfigs(Set<TbShopConfig> configs);

}
