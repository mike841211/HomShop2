package com.homlin.module.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopArticleDao;
import com.homlin.module.shop.model.TbShopArticle;
import com.homlin.module.shop.service.ArticleService;
import com.homlin.utils.Pager;

@Service
public class ArticleServiceImpl extends BaseServiceImpl<TbShopArticle, String> implements ArticleService {

	@Autowired
	public void setBaseDao(TbShopArticleDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopArticleDao articleDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return articleDao.getPagedList(pager);
	}

	@Override
	public void setIsShow(String ids, String status) {
		articleDao.setIsShow(ids, status);
	}

	@Override
	public List<TbShopArticle> getNextArticles(String article_id, int num) {
		return articleDao.getNextArticles(article_id, num);
	}

	@Override
	public List<TbShopArticle> getArticlesByCate(String category_id, int num) {
		return articleDao.getArticlesByCate(category_id, num);
	}

	@Override
	public void updateHits(String id) {
		articleDao.updateHits(id);
	}

}
