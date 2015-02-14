package com.homlin.module.shop.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopRoleDao;
import com.homlin.module.shop.model.TbShopRole;
import com.homlin.module.shop.service.RoleService;
import com.homlin.utils.Pager;

@Service
public class RoleServiceImpl extends BaseServiceImpl<TbShopRole, String> implements RoleService {

	@Autowired
	public void setBaseDao(TbShopRoleDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopRoleDao roleDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return roleDao.getPagedList(pager);
	}

	@Override
	public Map<String, Object> getRoleAuthorities() {
		return roleDao.getRoleAuthorities();
	}

}
