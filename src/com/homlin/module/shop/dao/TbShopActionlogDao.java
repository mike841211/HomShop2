package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopActionlog;
import com.homlin.utils.Pager;

public interface TbShopActionlogDao extends BaseDao<TbShopActionlog, String> {

	Pager getPagedList(Pager pager);

}
