package com.homlin.module.shop.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopActionlogDao;
import com.homlin.module.shop.model.TbShopActionlog;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopActionlogDaoImpl extends BaseDaoImpl<TbShopActionlog, String> implements TbShopActionlogDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = HqlHelper.selectMap("action,createDate,detail,id,ip,username");
		hql += " FROM TbShopActionlog";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " WHERE 1=1";
			String key, value;
			key = "username";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " AND username LIKE '%" + value + "%'";
			}
			key = "action";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " AND action LIKE '%" + value + "%'";
			}
			key = "createDateBegin";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " AND createDate>='" + value + "'";
			}
			key = "createDateEnd";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " AND createDate<='" + value + "'";
			}
			// 排序
			if (params.containsKey("sortField")) {
				hql += " ORDER BY " + params.get("sortField");
				hql += " " + params.get("sortOrder");
			} else {
				hql += " ORDER BY createDate DESC";
			}
		} else {
			hql += " ORDER BY createDate DESC";
		}

		pager.setHql(hql);
		return findByPage(pager);
	}

}
