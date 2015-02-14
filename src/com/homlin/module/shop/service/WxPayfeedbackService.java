package com.homlin.module.shop.service;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbWxPayfeedback;
import com.homlin.utils.Pager;

public interface WxPayfeedbackService extends BaseService<TbWxPayfeedback, String> {

	Pager getPagedList(Pager pager);

}
