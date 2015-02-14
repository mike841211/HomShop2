package com.homlin.module.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbWxMsgDao;
import com.homlin.module.shop.model.TbWxMsg;
import com.homlin.module.shop.service.WxMsgService;
import com.homlin.utils.Pager;

@Service
public class WxMsgServiceImpl extends BaseServiceImpl<TbWxMsg, String> implements WxMsgService {

	@Autowired
	public void setBaseDao(TbWxMsgDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbWxMsgDao tbWxMsgDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return tbWxMsgDao.getPagedList(pager);
	}

}
