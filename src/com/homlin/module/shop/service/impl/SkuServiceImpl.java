package com.homlin.module.shop.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopSkuDao;
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.module.shop.model.TbShopSku;
import com.homlin.module.shop.service.SkuService;

@Service
public class SkuServiceImpl extends BaseServiceImpl<TbShopSku, String> implements SkuService {

	@Autowired
	public void setBaseDao(TbShopSkuDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopSkuDao skuDao;

	@Override
	public List<Map<String, Object>> getProductSkuList(String id) {
		return skuDao.getProductSkuList(id);
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			TbShopSku sku = skuDao.get(id);
			TbShopProduct product = sku.getTbShopProduct();

			// 更新produc库存
			product.setStock(product.getStock() - sku.getStock());
			product.setBlockedStock(product.getBlockedStock() - sku.getBlockedStock());

			// 是否同时更新sales

			// 删除
			skuDao.delete(sku);
		}
	}

}
