package com.homlin.module.shop.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.utils.Pager;

public interface TbShopOrderDao extends BaseDao<TbShopOrder, String> {

	String getLastOrderSn();

	Pager getPagedList(Pager pager);

	List<TbShopOrder> getPaymentExpiredOrders(Date expiredDate);

	List<TbShopOrder> getCompletedExpiredOrders(Date expiredDate);

	List<TbShopOrder> getExportDataList(Map<String, Object> params);

}
