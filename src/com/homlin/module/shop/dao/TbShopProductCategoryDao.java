package com.homlin.module.shop.dao;

import java.util.List;
import java.util.Map;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopProductCategory;

public interface TbShopProductCategoryDao extends BaseDao<TbShopProductCategory, String> {

	List<Map<String, Object>> getProductCatagoryTreeData(String pid);

	void batchUpdateChildrenIndexpath(String old_indexpath, String new_indexpath);

	List<TbShopProductCategory> getSubCategories(String category_id, boolean siblings);

}
