package com.homlin.module.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbWxPayfeedbackDao;
import com.homlin.module.shop.model.TbWxPayfeedback;
import com.homlin.module.shop.service.WxPayfeedbackService;
import com.homlin.utils.Pager;

@Service
public class WxPayfeedbackServiceImpl extends BaseServiceImpl<TbWxPayfeedback, String> implements WxPayfeedbackService {

	@Autowired
	public void setBaseDao(TbWxPayfeedbackDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbWxPayfeedbackDao tbWxPayfeedbackDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return tbWxPayfeedbackDao.getPagedList(pager);
	}

}
