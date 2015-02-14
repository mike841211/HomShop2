package com.homlin.module.shop.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopCart;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.module.shop.model.TbShopOrderItem;
import com.homlin.utils.Pager;

public interface OrderService extends BaseService<TbShopOrder, String> {

	String getLastOrderSn();

	Pager getPagedList(Pager pager);

	TbShopOrder create(TbShopOrder order, TbShopCart cart, Map<String, Object> params) throws Exception;

	void confirmed(TbShopOrder order) throws Exception;

	void cancelled(TbShopOrder order) throws Exception;

	void paid(TbShopOrder order) throws Exception;

	void shipped(TbShopOrder order) throws Exception;

	void completed(TbShopOrder order) throws Exception;

	void evictPaymentExpiredOrders(Date expiredDate);

	void autoCompletedOrders(Date expiredDate);

	List<TbShopOrder> getExportDataList(Map<String, Object> params);

	TbShopOrderItem getTbShopOrderItem(String item_id);

}
