package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopBrandDao;
import com.homlin.module.shop.model.TbShopBrand;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopBrandDaoImpl extends BaseDaoImpl<TbShopBrand, String> implements TbShopBrandDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = HqlHelper.selectMap("createDate,displayorder,enname,id,logo,modifyDate,name,url");
		hql += " from TbShopBrand";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "name";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and (name like ? or enname like ?)";
				queryParams.add("%" + value + "%");
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());

			// 排序
			if (params.containsKey("sortField")) {
				hql += " order by " + params.get("sortField");
				hql += " " + params.get("sortOrder");
			} else {
				hql += " order by displayorder";
			}
		} else {
			hql += " order by displayorder";
		}

		pager.setHql(hql);
		return findByPage(pager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAllForSelect() {
		String hql = "select new map(id as value,name as text) from TbShopBrand order by displayorder,name";
		return (List<Map<String, Object>>) find(hql);
	}

}
