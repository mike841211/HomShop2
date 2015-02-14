package com.homlin.module.shop.service;

import java.util.List;
import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopProductCategory;

public interface ProductCategoryService extends BaseService<TbShopProductCategory, String> {

	List<Map<String, Object>> getProductCatagoryTreeData(String pid);

	List<TbShopProductCategory> getSubCategories(String category_id, boolean siblings);

}
