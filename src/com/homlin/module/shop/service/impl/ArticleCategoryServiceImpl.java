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
import com.homlin.module.shop.dao.TbShopArticleCategoryDao;
import com.homlin.module.shop.model.TbShopArticleCategory;
import com.homlin.module.shop.service.ArticleCategoryService;

@Service
public class ArticleCategoryServiceImpl extends BaseServiceImpl<TbShopArticleCategory, String> implements ArticleCategoryService {

	@Autowired
	public void setBaseDao(TbShopArticleCategoryDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopArticleCategoryDao articleCategoryDao;

	@Override
	public List<Map<String, Object>> getArticleCategoryTreeData(String pid) {
		return articleCategoryDao.getArticleCategoryTreeData(pid);
	}

	@Override
	@CacheEvict(value = { EhCacheNames.article_category }, allEntries = true)
	public String save(TbShopArticleCategory articleCategory) {

		articleCategoryDao.save(articleCategory);

		// 分类索引
		if (articleCategory.getTbShopArticleCategory() == null) {
			articleCategory.setIndexpath(articleCategory.getId());
		} else {
			TbShopArticleCategory parent = articleCategoryDao.get(articleCategory.getTbShopArticleCategory().getId());
			articleCategory.setIndexpath(parent.getIndexpath() + "," + articleCategory.getId());
		}
		articleCategoryDao.update(articleCategory);

		return articleCategory.getId();
	}

	// 父类是否相同
	private boolean parentEquals(TbShopArticleCategory category1, TbShopArticleCategory category2) {
		return (null == category1.getTbShopArticleCategory() && null == category2.getTbShopArticleCategory())
				|| (null != category1.getTbShopArticleCategory() && null != category2.getTbShopArticleCategory()
				&& category1.getTbShopArticleCategory().getId().equals(category2.getTbShopArticleCategory().getId()));
	}

	@Override
	@CacheEvict(value = { EhCacheNames.article_category }, allEntries = true)
	public void update(TbShopArticleCategory articleCategory) throws Exception {
		TbShopArticleCategory poArticleCategory = articleCategoryDao.get(articleCategory.getId());
		if (!articleCategoryDao.isUnique("code", poArticleCategory.getCode(), articleCategory.getCode())) {
			throw new MessageException("分类编码已存在");
		}
		if (!parentEquals(articleCategory, poArticleCategory)) {
			// 更新分类索引
			String old_indexpath = poArticleCategory.getIndexpath();
			if (articleCategory.getTbShopArticleCategory() == null) {
				articleCategory.setIndexpath(articleCategory.getId());
			} else {
				TbShopArticleCategory parent = articleCategoryDao.get(articleCategory.getTbShopArticleCategory().getId());
				if (parent.getIndexpath().indexOf(articleCategory.getId()) > -1) {
					throw new MessageException("上级不能是自己或当前子类");
				}
				articleCategory.setIndexpath(parent.getIndexpath() + "," + articleCategory.getId());
			}
			String new_indexpath = articleCategory.getIndexpath();

			// 更新子分类索引
			if (!new_indexpath.equalsIgnoreCase(old_indexpath)) {
				articleCategoryDao.batchUpdateChildrenIndexpath(old_indexpath, new_indexpath);
			}
		}
		articleCategory.setCreateDate(poArticleCategory.getCreateDate());
		articleCategoryDao.merge(articleCategory);
	}

	@Override
	@CacheEvict(value = { EhCacheNames.article_category }, allEntries = true)
	public void delete(String[] ids) throws Exception {
		super.delete(ids);
	}

	@Override
	@Cacheable(value = EhCacheNames.article_category)
	public List<TbShopArticleCategory> getSubArticleCategories(String category_id, boolean siblings) {
		return articleCategoryDao.getSubCategories(category_id, siblings);
	}

	@Override
	public TbShopArticleCategory getByCode(String code) {
		return articleCategoryDao.findOneByProperty("code", code);
	}

}
