package com.homlin.module.shop.service;

import java.util.List;
import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopProductBaseinfo;
import com.homlin.utils.Pager;

public interface ProductBaseinfoService extends BaseService<TbShopProductBaseinfo, String> {

	Pager getPagedList(Pager pager);

	List<Map<String, Object>> getAllForSelect();

	String getContent(String id);

}
