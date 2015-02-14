package com.homlin.module.shop.service;

import java.util.List;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopSpecification;
import com.homlin.utils.Pager;

public interface SpecificationService extends BaseService<TbShopSpecification, String> {

	Pager getPagedList(Pager pager);

	List<TbShopSpecification> getAll();

}
