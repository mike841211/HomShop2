package com.homlin.module.shop.service;

import java.util.List;
import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopSku;

public interface SkuService extends BaseService<TbShopSku, String> {

	List<Map<String, Object>> getProductSkuList(String id);

}
