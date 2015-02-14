package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbWxAutoreplyDao;
import com.homlin.module.shop.model.TbWxAutoreply;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbWxAutoreplyDaoImpl extends BaseDaoImpl<TbWxAutoreply, String> implements TbWxAutoreplyDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = "select new map(";
		hql += HqlHelper
				.mapping("a.id,a.title,a.createDate,a.modifyDate,a.isShow,a.showCover,a.cover,a.topIndex,a.summary,a.keyword,a.replyType,a.matching", "a");
		hql += ") from TbWxAutoreply as a ";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "keyword";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and a.keyword like ?";
				queryParams.add("%" + value + "%");
			}
			key = "title";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and a.title like ?";
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());
		}

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public void setValue(String[] ids, String field, String value) {
		String hql = "update TbWxAutoreply set " + field + "=? where id in (:ids)";
		Query query = getSession().createQuery(hql);
		// query.setParameter(0, value).setParameterList("ids", ids).executeUpdate();
		setParameters(query, value);
		query.setParameterList("ids", ids).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbWxAutoreply> findByKeyword(String keyword, int num) {
		// String hql = "from TbWxAutoreply where isnull(isShow,'0')='1' and keyword like ?";
		// return (List<TbWxAutoreply>) findTop(hql, num, "%" + keyword + "%");
		String hql = "from TbWxAutoreply where isnull(isShow,'0')='1'" +
				" and ((isnull(matching,'complete')='complete' and keyword=?) or (isnull(matching,'complete')<>'complete' and keyword like ?))";
		return (List<TbWxAutoreply>) findTop(hql, num, keyword, "%" + keyword + "%");
	}

	@Override
	public void updateHits(String id) {
		String hql = "update TbWxAutoreply set hits=hits+1 where id=:id";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

}
