package com.homlin.module.shop.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.dao.TbShopBrandDao;
import com.homlin.module.shop.dao.TbShopProductCategoryDao;
import com.homlin.module.shop.model.TbShopBrand;
import com.homlin.module.shop.model.TbShopProductCategory;
import com.homlin.module.shop.service.BrandService;
import com.homlin.utils.Pager;

@Service
public class BrandServiceImpl extends BaseServiceImpl<TbShopBrand, String> implements BrandService {

	@Autowired
	public void setBaseDao(TbShopBrandDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopBrandDao brandDao;

	@Autowired
	TbShopProductCategoryDao tbShopProductCategoryDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return brandDao.getPagedList(pager);
	}

	@Override
	@Cacheable(value = EhCacheNames.brand)
	public List<Map<String, Object>> getAllForSelect() {
		return brandDao.getAllForSelect();
	}

	@Override
	@CacheEvict(value = EhCacheNames.brand)
	public String save(TbShopBrand entity) throws Exception {
		return super.save(entity);
	}

	@Override
	@CacheEvict(value = EhCacheNames.brand, allEntries = true)
	public void update(TbShopBrand entity) throws Exception {
		super.update(entity);
	}

	@Override
	@CacheEvict(value = EhCacheNames.brand, allEntries = true)
	public void delete(String[] ids) throws Exception {
		super.delete(ids);
	}

	@Override
	@Cacheable(value = EhCacheNames.brand)
	public List<TbShopBrand> getByCateId(String category_id) {
		TbShopProductCategory category = tbShopProductCategoryDao.get(category_id);
		if (null != category) {
			return category.getTbShopBrands();
		}
		return null;
	}

}
