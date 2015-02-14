package com.homlin.module.shop.service;

import java.util.List;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopChinaArea;

public interface ChinaAreaService extends BaseService<TbShopChinaArea, String> {

	TbShopChinaArea findByCode(String code);

	List<TbShopChinaArea> getProvices();

	List<TbShopChinaArea> getCities(String code);

	List<TbShopChinaArea> getDistricts(String code);

}
