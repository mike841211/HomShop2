package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopArticleDao;
import com.homlin.module.shop.model.TbShopArticle;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopArticleDaoImpl extends BaseDaoImpl<TbShopArticle, String> implements TbShopArticleDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = "select new map(";
		hql += HqlHelper.mapping("a.id,a.title,a.createDate,a.modifyDate,a.isShow,a.topIndex,a.subtitle,a.coverimg", new String[] { "a" });
		hql += ",isnull(c.name,'[未分类]') as category_name) from TbShopArticle as a left join a.tbShopArticleCategory as c";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "isShow";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and isnull(a.isShow,'0')=?";
				queryParams.add(value);
			}
			key = "basecid"; // 强制指定类目下
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and c.indexpath like ?";
				queryParams.add("%" + value + "%");
			}
			key = "cid";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and c.indexpath like ?";
				queryParams.add("%" + value + "%");
			}
			key = "title";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and a.title like ?";
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());

			// 排序
			if (params.containsKey("sortField")) {
				hql += " order by " + params.get("sortField");
				hql += " " + params.get("sortOrder");
			} else {
				hql += " order by a.createDate desc";
			}
		} else {
			hql += " order by a.createDate desc";
		}

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public void setIsShow(String ids, String status) {
		// String hql = "update TbShopArticle set isShow=? where ? like '%'+id+'%'";
		// update(hql, status, ids);

		String hql = "update TbShopArticle set isShow=? where id in (:ids)";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, status);
		query.setParameterList("ids", ids.split(","));
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopArticle> getNextArticles(String article_id, int num) {
		String hql = "from TbShopArticle where isnull(isShow,'0')='1' and id>?";
		return (List<TbShopArticle>) findTop(hql, num, article_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopArticle> getArticlesByCate(String category_id, int num) {
		String hql = "from TbShopArticle where isnull(isShow,'0')='1' and tbShopArticleCategory.id=?";
		return (List<TbShopArticle>) findTop(hql, num, category_id);
	}

	@Override
	public void updateHits(String id) {
		String hql = "update TbShopArticle set hits=hits+1 where id=:id";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

}
