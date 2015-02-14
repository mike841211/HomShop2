package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbWxUserDao;
import com.homlin.module.shop.model.TbWxUser;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbWxUserDaoImpl extends BaseDaoImpl<TbWxUser, String> implements TbWxUserDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = "select new map(";
		hql += HqlHelper.mapping("city,country,groupId,headimgurl,language,nickname,openid,province,sex,subscribe,subscribeTime");
		hql += ") from TbWxUser";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "group_id";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and groupId=?";
				queryParams.add(Integer.valueOf(value));
			}
			key = "nickname";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and nickname like ?";
				queryParams.add("%" + value + "%");
			}
			key = "country";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and country like ?";
				queryParams.add("%" + value + "%");
			}
			key = "province";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and province like ?";
				queryParams.add("%" + value + "%");
			}
			key = "city";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and city like ?";
				queryParams.add("%" + value + "%");
			}
			key = "sex";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				if (value.equals("男")) {
					hql += " and sex=1";
				} else if (value.equals("女")) {
					hql += " and sex=2";
				}
			}
			pager.setQueryParams(queryParams.toArray());
		}
		hql += " order by subscribeTime desc";

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public void setValue(String[] ids, String field, Object value) {
		String hql = "update TbWxUser set " + field + "=? where id in (:ids)";
		Query query = getSession().createQuery(hql);
		// query.setParameter(0, value).setParameterList("ids", ids).executeUpdate();
		setParameters(query, value);
		query.setParameterList("ids", ids).executeUpdate();
	}

}
