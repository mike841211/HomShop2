package com.homlin.module.shop.dao;

import java.util.Map;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopRole;
import com.homlin.utils.Pager;

public interface TbShopRoleDao extends BaseDao<TbShopRole, String> {

	Pager getPagedList(Pager pager);

	Map<String, Object> getRoleAuthorities();

}
