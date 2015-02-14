package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbWxGroup;

public interface TbWxGroupDao extends BaseDao<TbWxGroup, Integer> {

	void removeAll();

}
