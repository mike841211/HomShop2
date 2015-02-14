package com.homlin.module.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopActionlogDao;
import com.homlin.module.shop.model.TbShopActionlog;
import com.homlin.module.shop.service.ActionlogService;
import com.homlin.utils.Pager;

@Service
public class ActionlogServiceImpl extends BaseServiceImpl<TbShopActionlog, String> implements ActionlogService {

	@Autowired
	public void setBaseDao(TbShopActionlogDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopActionlogDao tbShopActionlogDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return tbShopActionlogDao.getPagedList(pager);
	}

}
