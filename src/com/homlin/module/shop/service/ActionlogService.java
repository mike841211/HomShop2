package com.homlin.module.shop.service;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopActionlog;
import com.homlin.utils.Pager;

public interface ActionlogService extends BaseService<TbShopActionlog, String> {

	Pager getPagedList(Pager pager);

}
