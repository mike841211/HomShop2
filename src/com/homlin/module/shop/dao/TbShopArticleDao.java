package com.homlin.module.shop.dao;

import java.util.List;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopArticle;
import com.homlin.utils.Pager;

public interface TbShopArticleDao extends BaseDao<TbShopArticle, String> {

	Pager getPagedList(Pager pager);

	void setIsShow(String ids, String status);

	List<TbShopArticle> getNextArticles(String article_id, int num);

	List<TbShopArticle> getArticlesByCate(String category_id, int num);

	void updateHits(String id);

}
