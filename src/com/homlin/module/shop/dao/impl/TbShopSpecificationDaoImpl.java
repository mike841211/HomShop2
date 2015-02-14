package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopSpecificationDao;
import com.homlin.module.shop.model.TbShopSpecification;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopSpecificationDaoImpl extends BaseDaoImpl<TbShopSpecification, String> implements TbShopSpecificationDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = HqlHelper.selectMap("createDate,displayorder,id,modifyDate,name,remark");
		hql += " from TbShopSpecification";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "name";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and (name like ? or remark like ?)";
				queryParams.add("%" + value + "%");
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());

			// // 排序
			// if (params.containsKey("sortField")) {
			// hql += " order by " + params.get("sortField");
			// hql += " " + params.get("sortOrder");
			// } else {
			// // hql += " order by createDate";
			// }
		} else {
			// hql += " order by createDate";
		}
		hql += " order by displayorder";

		pager.setHql(hql);
		return findByPage(pager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopSpecification> getAll() {
		String hql = "from TbShopSpecification order by displayorder";
		return (List<TbShopSpecification>) find(hql);
	}

}
