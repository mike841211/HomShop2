package com.homlin.module.shop.dao.impl;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbWxGroupDao;
import com.homlin.module.shop.model.TbWxGroup;

@Repository
public class TbWxGroupDaoImpl extends BaseDaoImpl<TbWxGroup, Integer> implements TbWxGroupDao {

	@Override
	public void removeAll() {
		String hql = "delete TbWxGroup";
		update(hql);
	}

}
