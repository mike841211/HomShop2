package com.homlin.module.shop.service;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbWxMsg;
import com.homlin.utils.Pager;

public interface WxMsgService extends BaseService<TbWxMsg, String> {

	Pager getPagedList(Pager pager);

}
