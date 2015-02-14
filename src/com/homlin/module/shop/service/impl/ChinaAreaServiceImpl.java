package com.homlin.module.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.dao.TbShopChinaAreaDao;
import com.homlin.module.shop.model.TbShopChinaArea;
import com.homlin.module.shop.service.ChinaAreaService;

@Service
public class ChinaAreaServiceImpl extends BaseServiceImpl<TbShopChinaArea, String> implements ChinaAreaService {

	@Autowired
	public void setBaseDao(TbShopChinaAreaDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopChinaAreaDao chinaAreaDao;

	@Override
	@Cacheable(value = EhCacheNames.china_area)
	public TbShopChinaArea findByCode(String code) {
		return chinaAreaDao.get(code);
	}

	@Override
	@Cacheable(value = EhCacheNames.china_area, key = "'000000'")
	public List<TbShopChinaArea> getProvices() {
		return chinaAreaDao.getProvices();
	}

	@Override
	@Cacheable(value = EhCacheNames.china_area)
	public List<TbShopChinaArea> getCities(String code) {
		return chinaAreaDao.getCities(code);
	}

	@Override
	@Cacheable(value = EhCacheNames.china_area)
	public List<TbShopChinaArea> getDistricts(String code) {
		return chinaAreaDao.getDistricts(code);
	}

}
