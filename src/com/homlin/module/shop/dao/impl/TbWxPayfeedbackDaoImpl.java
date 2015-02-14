package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbWxPayfeedbackDao;
import com.homlin.module.shop.model.TbWxPayfeedback;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbWxPayfeedbackDaoImpl extends BaseDaoImpl<TbWxPayfeedback, String> implements TbWxPayfeedbackDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = HqlHelper
				.selectMap("appId,extInfo,feedBackId,status,openId,picUrl0,picUrl1,picUrl2,picUrl3,picUrl4,reason,solution,timeStamp,transId");
		hql += " from TbWxPayfeedback ";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "feedBackId";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and feedBackId=?";
				queryParams.add(value);
			}
			pager.setQueryParams(queryParams.toArray());
		}
		hql += " order by feedBackId desc";

		pager.setHql(hql);
		return findByPage(pager);
	}

}
