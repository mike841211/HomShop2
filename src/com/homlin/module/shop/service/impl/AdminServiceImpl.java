package com.homlin.module.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopAdminDao;
import com.homlin.module.shop.model.TbShopAdmin;
import com.homlin.module.shop.service.AdminService;
import com.homlin.utils.Pager;

@Service
public class AdminServiceImpl extends BaseServiceImpl<TbShopAdmin, String> implements AdminService {

	@Autowired
	public void setBaseDao(TbShopAdminDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopAdminDao adminDao;

	@Override
	public TbShopAdmin findByUsername(String username) {
		return adminDao.findOneByProperty("username", username);
	}

	@Override
	public Pager getPagedList(Pager pager) {
		return adminDao.getPagedList(pager);
	}

}
