package com.homlin.module.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.dao.TbShopNavigationDao;
import com.homlin.module.shop.model.TbShopNavigation;
import com.homlin.module.shop.service.NavigationService;

@Service
public class NavigationServiceImpl extends BaseServiceImpl<TbShopNavigation, String> implements NavigationService {

	@Autowired
	public void setBaseDao(TbShopNavigationDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopNavigationDao tbShopNavigationDao;

	@Override
	public List<TbShopNavigation> getAll() {
		return tbShopNavigationDao.getAll();
	}

	// ---

	@Override
	@CacheEvict(value = EhCacheNames.navigation, allEntries = true)
	public String save(TbShopNavigation model) throws Exception {
		return super.save(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.navigation, allEntries = true)
	public void update(TbShopNavigation model) throws Exception {
		super.update(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.navigation, allEntries = true)
	public void delete(String[] ids) throws Exception {
		super.delete(ids);
	}

	@Override
	@Cacheable(value = EhCacheNames.navigation)
	public List<TbShopNavigation> getByPosition(String position) {
		return tbShopNavigationDao.getByPosition(position);
	}

	@Override
	@CacheEvict(value = EhCacheNames.navigation, allEntries = true)
	public void setIsShow(String[] ids, String isShow) {
		tbShopNavigationDao.setIsShow(ids, isShow);
	}

}
