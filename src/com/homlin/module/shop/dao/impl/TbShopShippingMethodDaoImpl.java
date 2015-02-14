package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopShippingMethodDao;
import com.homlin.module.shop.model.TbShopShippingMethod;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopShippingMethodDaoImpl extends BaseDaoImpl<TbShopShippingMethod, String> implements TbShopShippingMethodDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = HqlHelper
				.selectMap("continuePrice,continueWeight,createDate,displayorder,enabled,firstPrice,firstWeight,id,modifyDate,name,remark");
		hql += " from TbShopShippingMethod";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "name";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and name like ?";
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());

		}
		hql += " order by displayorder";

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public void update(TbShopShippingMethod shippingMethod) {
		String hql = "delete TbShopShippingMethodDetail where tbShopShippingMethod.id=?";
		super.update(hql, shippingMethod.getId());
		super.update(shippingMethod);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAllForSelect() {
		String hql = "select new map(id as value,name as text) from TbShopShippingMethod where isnull(enabled,'0')='1' order by displayorder";
		return (List<Map<String, Object>>) find(hql);
	}

}
