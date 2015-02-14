package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopAdminDao;
import com.homlin.module.shop.model.TbShopAdmin;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopAdminDaoImpl extends BaseDaoImpl<TbShopAdmin, String> implements TbShopAdminDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = HqlHelper.selectMap("createDate,department,email,id,lock,name,username" +
				",loginCount,loginDate,loginFailureCount,loginFailureDate,loginIp,modifyDate");
		hql += " from TbShopAdmin";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "username";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and username like ?";
				queryParams.add("%" + value + "%");
			}
			key = "name";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and name like ?";
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());

			// 排序
			if (params.containsKey("sortField")) {
				hql += " order by " + params.get("sortField");
				hql += " " + params.get("sortOrder");
			} else {
				hql += " order by createDate";
			}
		} else {
			hql += " order by createDate";
		}

		pager.setHql(hql);
		return findByPage(pager);
	}

}
