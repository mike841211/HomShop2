package com.homlin.module.shop.service;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbWxUser;
import com.homlin.utils.Pager;

public interface WxUserService extends BaseService<TbWxUser, String> {

	Pager getPagedList(Pager pager);

	void setValue(String[] ids, String field, Object value);

}
