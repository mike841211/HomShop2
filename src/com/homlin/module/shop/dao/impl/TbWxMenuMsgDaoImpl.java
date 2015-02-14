package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbWxMenuMsgDao;
import com.homlin.module.shop.model.TbWxMenuMsg;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbWxMenuMsgDaoImpl extends BaseDaoImpl<TbWxMenuMsg, String> implements TbWxMenuMsgDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = "select new map(";
		hql += HqlHelper.mapping("a.id,a.title,a.createDate,a.modifyDate,a.isShow,a.showCover,a.cover,a.topIndex,a.summary", "a");
		hql += ",isnull(c.name,'[未分类]') as menu_name) from TbWxMenuMsg as a left join a.tbWxMenu as c";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "menu_id";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and c.id=?";
				queryParams.add(value);
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
		String hql = "update TbWxMenuMsg set " + field + "=? where id in (:ids)";
		Query query = getSession().createQuery(hql);
		// query.setParameter(0, value).setParameterList("ids", ids).executeUpdate();
		setParameters(query, value);
		query.setParameterList("ids", ids).executeUpdate();
	}

	@Override
	public String getTextContent(String menu_id) {
		String hql = "select summary from TbWxMenuMsg where tbWxMenu.id=? and isnull(isShow,'0')='1' and isnull(summary,'')<>''";
		List<?> list = findTop(hql, 1, menu_id);
		return list.size() == 0 ? null : (String) list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbWxMenuMsg> getNews(String menu_id, int num) {
		String hql = "from TbWxMenuMsg where tbWxMenu.id=? and isnull(isShow,'0')='1'";
		return (List<TbWxMenuMsg>) findTop(hql, num, menu_id);
	}

	@Override
	public void updateHits(String id) {
		String hql = "update TbWxMenuMsg set hits=hits+1 where id=:id";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

}
