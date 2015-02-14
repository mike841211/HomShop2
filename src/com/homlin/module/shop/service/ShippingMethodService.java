package com.homlin.module.shop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopShippingMethod;
import com.homlin.utils.Pager;

public interface ShippingMethodService extends BaseService<TbShopShippingMethod, String> {

	Pager getPagedList(Pager pager);

	List<Map<String, Object>> getAllForSelect();

	BigDecimal getShippingFee(String shippingMethodId, Integer totalWeight, String areaCode);

}
