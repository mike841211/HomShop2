package com.homlin.module.shop.service;

import java.util.List;
import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopBrand;
import com.homlin.utils.Pager;

public interface BrandService extends BaseService<TbShopBrand, String> {

	Pager getPagedList(Pager pager);

	List<Map<String, Object>> getAllForSelect();

	List<TbShopBrand> getByCateId(String category_id);

}
