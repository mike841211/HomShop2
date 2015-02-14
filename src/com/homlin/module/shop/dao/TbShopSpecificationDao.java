package com.homlin.module.shop.dao;

import java.util.List;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopSpecification;
import com.homlin.utils.Pager;

public interface TbShopSpecificationDao extends BaseDao<TbShopSpecification, String> {

	Pager getPagedList(Pager pager);

	List<TbShopSpecification> getAll();

}
