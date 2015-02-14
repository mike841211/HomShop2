package com.homlin.module.shop.dao;

import java.util.List;
import java.util.Map;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopArticleCategory;

public interface TbShopArticleCategoryDao extends BaseDao<TbShopArticleCategory, String> {

	List<Map<String, Object>> getArticleCategoryTreeData(String pid);

	void batchUpdateChildrenIndexpath(String old_indexpath, String new_indexpath);

	List<TbShopArticleCategory> getSubCategories(String category_id, boolean siblings);

}
