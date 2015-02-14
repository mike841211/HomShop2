package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbWxPayfeedback;
import com.homlin.utils.Pager;

public interface TbWxPayfeedbackDao extends BaseDao<TbWxPayfeedback, String> {

	Pager getPagedList(Pager pager);

}
