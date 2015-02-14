package com.homlin.module.shop.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopSkuDao;
import com.homlin.module.shop.model.TbShopSku;
import com.homlin.utils.HqlHelper;

@Repository
public class TbShopSkuDaoImpl extends BaseDaoImpl<TbShopSku, String> implements TbShopSkuDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getProductSkuList(String id) {
		String hql = "select new map(";
		hql += HqlHelper
				.mapping("barcode,blockedStock,sn,syssn,cost,createDate,height,id,length,marketPrice,modifyDate,price,sales,sampleImage,specificationJson,specificationValueName,stock,storeLocation,weight,width");
		hql += ") from TbShopSku where tbShopProduct.id=?";
		return (List<Map<String, Object>>) find(hql, id);
	}

}
