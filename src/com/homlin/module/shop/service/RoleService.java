package com.homlin.module.shop.service;

import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopRole;
import com.homlin.utils.Pager;

public interface RoleService extends BaseService<TbShopRole, String> {

	Pager getPagedList(Pager pager);

	Map<String, Object> getRoleAuthorities();

}
