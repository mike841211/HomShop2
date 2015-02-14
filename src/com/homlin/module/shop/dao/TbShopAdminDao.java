package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopAdmin;
import com.homlin.utils.Pager;

public interface TbShopAdminDao extends BaseDao<TbShopAdmin, String> {

	Pager getPagedList(Pager pager);

}
