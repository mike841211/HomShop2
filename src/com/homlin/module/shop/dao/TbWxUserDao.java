package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbWxUser;
import com.homlin.utils.Pager;

public interface TbWxUserDao extends BaseDao<TbWxUser, String> {

	Pager getPagedList(Pager pager);

	void setValue(String[] ids, String field, Object value);

}
