package com.homlin.module.shop.service;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopAdmin;
import com.homlin.utils.Pager;

public interface AdminService extends BaseService<TbShopAdmin, String> {

	TbShopAdmin findByUsername(String username);

	Pager getPagedList(Pager pager);

}
