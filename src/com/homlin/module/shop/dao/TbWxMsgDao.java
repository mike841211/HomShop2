package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbWxMsg;
import com.homlin.utils.Pager;

public interface TbWxMsgDao extends BaseDao<TbWxMsg, String> {

	Pager getPagedList(Pager pager);

}
