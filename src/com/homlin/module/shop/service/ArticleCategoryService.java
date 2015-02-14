package com.homlin.module.shop.service;

import java.util.List;
import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopArticleCategory;

public interface ArticleCategoryService extends BaseService<TbShopArticleCategory, String> {

	List<Map<String, Object>> getArticleCategoryTreeData(String pid);

	List<TbShopArticleCategory> getSubArticleCategories(String category_id, boolean siblings);

	TbShopArticleCategory getByCode(String code);

}
