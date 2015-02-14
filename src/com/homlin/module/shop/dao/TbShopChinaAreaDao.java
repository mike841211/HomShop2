package com.homlin.module.shop.dao;

import java.util.List;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopChinaArea;

public interface TbShopChinaAreaDao extends BaseDao<TbShopChinaArea, String> {

	List<TbShopChinaArea> getProvices();

	List<TbShopChinaArea> getCities(String code);

	List<TbShopChinaArea> getDistricts(String code);

}
