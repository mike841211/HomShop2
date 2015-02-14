package com.homlin.module.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbWxUserDao;
import com.homlin.module.shop.model.TbWxUser;
import com.homlin.module.shop.service.WxUserService;
import com.homlin.utils.Pager;

@Service
public class WxUserServiceImpl extends BaseServiceImpl<TbWxUser, String> implements WxUserService {

	@Autowired
	public void setBaseDao(TbWxUserDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbWxUserDao tbWxUserDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return tbWxUserDao.getPagedList(pager);
	}

	@Override
	public void setValue(String[] ids, String field, Object value) {
		tbWxUserDao.setValue(ids, field, value);
	}

}
