package com.homlin.module.shop.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.homlin.app.exception.MessageException;
import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.dao.TbShopProductCategoryDao;
import com.homlin.module.shop.model.TbShopProductCategory;
import com.homlin.module.shop.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<TbShopProductCategory, String> implements ProductCategoryService {

	@Autowired
	public void setBaseDao(TbShopProductCategoryDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopProductCategoryDao productCategoryDao;

	@Override
	public List<Map<String, Object>> getProductCatagoryTreeData(String pid) {
		return productCategoryDao.getProductCatagoryTreeData(pid);
	}

	@Override
	@CacheEvict(value = { EhCacheNames.product_category }, allEntries = true)
	public String save(TbShopProductCategory productCategory) throws Exception {
		if (productCategoryDao.exists("code", productCategory.getCode())) {
			throw new MessageException("分类编码已存在");
		}

		productCategoryDao.save(productCategory);

		// 分类索引
		if (productCategory.getTbShopProductCategory() == null) {
			productCategory.setIndexpath(productCategory.getId());
		} else {
			TbShopProductCategory parent = productCategoryDao.get(productCategory.getTbShopProductCategory().getId());
			productCategory.setIndexpath(parent.getIndexpath() + "," + productCategory.getId());
		}
		productCategoryDao.update(productCategory);

		return productCategory.getId();
	}

	// 父类是否相同
	private boolean parentEquals(TbShopProductCategory category1, TbShopProductCategory category2) {
		return (null == category1.getTbShopProductCategory() && null == category2.getTbShopProductCategory())
				|| (null != category1.getTbShopProductCategory() && null != category2.getTbShopProductCategory()
				&& category1.getTbShopProductCategory().getId().equals(category2.getTbShopProductCategory().getId()));
	}

	@Override
	@CacheEvict(value = { EhCacheNames.product_category }, allEntries = true)
	public void update(TbShopProductCategory productCategory) throws Exception {
		TbShopProductCategory poCategory = productCategoryDao.get(productCategory.getId());
		if (!productCategoryDao.isUnique("code", poCategory.getCode(), productCategory.getCode())) {
			throw new MessageException("分类编码已存在");
		}
		if (!parentEquals(productCategory, poCategory)) {
			// 更新分类索引
			String old_indexpath = poCategory.getIndexpath();
			if (productCategory.getTbShopProductCategory() == null) {
				productCategory.setIndexpath(productCategory.getId());
			} else {
				TbShopProductCategory parent = productCategoryDao.get(productCategory.getTbShopProductCategory().getId());
				if (parent.getIndexpath().indexOf(productCategory.getId()) > -1) {
					throw new MessageException("上级不能是自己或当前子类");
				}
				productCategory.setIndexpath(parent.getIndexpath() + "," + productCategory.getId());
			}
			String new_indexpath = productCategory.getIndexpath();

			// 更新子分类索引
			if (!new_indexpath.equalsIgnoreCase(old_indexpath)) {
				productCategoryDao.batchUpdateChildrenIndexpath(old_indexpath, new_indexpath);
			}
		}
		productCategory.setCreateDate(poCategory.getCreateDate());
		productCategoryDao.merge(productCategory);
	}

	@Override
	@CacheEvict(value = { EhCacheNames.product_category }, allEntries = true)
	public void delete(String[] ids) throws Exception {
		super.delete(ids);
	}

	@Override
	@Cacheable(value = EhCacheNames.product_category)
	public List<TbShopProductCategory> getSubCategories(String category_id, boolean siblings) {
		return productCategoryDao.getSubCategories(category_id, siblings);
	}

}
