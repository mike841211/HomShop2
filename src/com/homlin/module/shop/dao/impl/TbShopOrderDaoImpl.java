package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopOrderDao;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Repository
public class TbShopOrderDaoImpl extends BaseDaoImpl<TbShopOrder, String> implements TbShopOrderDao {

	@Override
	public String getLastOrderSn() {
		String hql = "select max(sn) from TbShopOrder";
		return (String) findOne(hql);
	}

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = "select new map(";
		hql += HqlHelper
				.mapping("address,adjustAmount,areaCode,buyerRemark,sn,createDate,freightCollect,id,invoice,memberUsername,mobile" +
						",modifyDate,name,orderStatus,paidAmount,paymentCode,paymentFee,paymentMethod,paymentStatus,phone,postcode,salerRemark" +
						",shippingCode,shippingCompany,shippingFee,shippingMethod,shippingStatus,totalAmount,totalPrice,totalQuantity,totalWeight" +
						",couponAmount,useScoreAmount,useScore,gainScore,gainScoreMutiple");
		hql += ") from TbShopOrder";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;

			key = "member_id";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and tbShopMember.id=?";
				queryParams.add(value);
			}

			key = "tuid";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and tuid=?";
				queryParams.add(value);
			}

			key = "sn";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and sn like ?";
				queryParams.add("%" + value + "%");
			}
			key = "name";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and p.name like ?";
				queryParams.add("%" + value + "%");
			}
			key = "memberUsername";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and memberUsername like ?";
				queryParams.add("%" + value + "%");
			}

			key = "paymentCode";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and paymentCode=?";
				queryParams.add(value);
			}

			pager.setQueryParams(queryParams.toArray());

		}
		hql += " order by createDate desc";

		pager.setHql(hql);
		return findByPage(pager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopOrder> getPaymentExpiredOrders(Date expiredDate) {
		String hql = "from TbShopOrder where orderStatus<>'cancelled' and paymentStatus='unpaid' and shippingStatus='unshipped' and modifyDate<=?";
		return (List<TbShopOrder>) find(hql, Util.getDateTimeString(expiredDate));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopOrder> getCompletedExpiredOrders(Date expiredDate) {
		String hql = "from TbShopOrder where orderStatus<>'completed' and shippingStatus='shipped' and paymentStatus='paid' and modifyDate<=?";
		return (List<TbShopOrder>) find(hql, Util.getDateTimeString(expiredDate));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopOrder> getExportDataList(Map<String, Object> params) {
		String hql = " from TbShopOrder";
		// 条件
		hql += " where 1=1";
		List<Object> queryParams = new ArrayList<Object>();
		String key, value;

		key = "member_id";
		if (params.containsKey(key)) {
			value = params.get(key).toString();
			hql += " and tbShopMember.id=?";
			queryParams.add(value);
		}

		key = "tuid";
		if (params.containsKey(key)) {
			value = params.get(key).toString();
			hql += " and tuid=?";
			queryParams.add(value);
		}

		key = "sn";
		if (params.containsKey(key)) {
			value = params.get(key).toString();
			hql += " and sn like ?";
			queryParams.add("%" + value + "%");
		}
		key = "name";
		if (params.containsKey(key)) {
			value = params.get(key).toString();
			hql += " and p.name like ?";
			queryParams.add("%" + value + "%");
		}
		key = "memberUsername";
		if (params.containsKey(key)) {
			value = params.get(key).toString();
			hql += " and memberUsername like ?";
			queryParams.add("%" + value + "%");
		}

		key = "createDate1";
		if (params.containsKey(key)) {
			value = params.get(key).toString();
			hql += " and createDate>=?";
			queryParams.add(value);
		}

		key = "createDate2";
		if (params.containsKey(key)) {
			value = params.get(key).toString();
			hql += " and createDate<=?";
			queryParams.add(value);
		}

		key = "sn1";
		if (params.containsKey(key)) {
			value = params.get(key).toString();
			hql += " and sn>=?";
			queryParams.add(value);
		}

		key = "sn2";
		if (params.containsKey(key)) {
			value = params.get(key).toString();
			hql += " and sn<=?";
			queryParams.add(value);
		}

		return (List<TbShopOrder>) find(hql, queryParams.toArray());
	}

}
