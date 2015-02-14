package com.homlin.module.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.dao.TbShopAdvertDao;
import com.homlin.module.shop.model.TbShopAdvert;
import com.homlin.module.shop.service.AdvertService;
import com.homlin.utils.Pager;

@Service
public class AdvertServiceImpl extends BaseServiceImpl<TbShopAdvert, String> implements AdvertService {

	@Autowired
	public void setBaseDao(TbShopAdvertDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopAdvertDao advertDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return advertDao.getPagedList(pager);
	}

	@Override
	@CacheEvict(value = EhCacheNames.advert, allEntries = true)
	public void setInuse(String[] ids, String inuse) {
		advertDao.setInuse(ids, inuse);
	}

	@Override
	@Cacheable(value = EhCacheNames.advert, key = "#keyword")
	public List<TbShopAdvert> getByKeyword(String keyword) {
		return advertDao.getByKeyword(keyword);
	}

	@Override
	@CacheEvict(value = EhCacheNames.advert, key = "#advert.keyword")
	public String save(TbShopAdvert advert) throws Exception {
		return super.save(advert);
	}

	@Override
	@CacheEvict(value = EhCacheNames.advert, allEntries = true)
	public void update(TbShopAdvert advert) throws Exception {
		super.update(advert);
	}

	@Override
	@CacheEvict(value = EhCacheNames.advert, allEntries = true)
	public void delete(String[] ids) throws Exception {
		super.delete(ids);
	}

}
