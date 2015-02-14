package com.homlin.module.shop.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.dao.TbShopProductBaseinfoDao;
import com.homlin.module.shop.model.TbShopProductBaseinfo;
import com.homlin.module.shop.service.ProductBaseinfoService;
import com.homlin.utils.Pager;

@Service
public class ProductBaseinfoServiceImpl extends BaseServiceImpl<TbShopProductBaseinfo, String> implements ProductBaseinfoService {

	@Autowired
	public void setBaseDao(TbShopProductBaseinfoDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopProductBaseinfoDao productBaseinfoDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return productBaseinfoDao.getPagedList(pager);
	}

	@Override
	@Cacheable(value = EhCacheNames.product_baseinfo)
	public List<Map<String, Object>> getAllForSelect() {
		return productBaseinfoDao.getAllForSelect();
	}

	@Override
	@Cacheable(value = EhCacheNames.product_baseinfo)
	public String getContent(String id) {
		return productBaseinfoDao.get(id).getContent();
	}

	// ---

	@Override
	@CacheEvict(value = EhCacheNames.product_baseinfo, allEntries = true)
	public String save(TbShopProductBaseinfo model) throws Exception {
		return super.save(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.product_baseinfo, allEntries = true)
	public void update(TbShopProductBaseinfo model) throws Exception {
		super.update(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.product_baseinfo, allEntries = true)
	public void delete(String[] ids) throws Exception {
		super.delete(ids);
	}

}
