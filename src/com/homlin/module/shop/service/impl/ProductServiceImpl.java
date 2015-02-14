package com.homlin.module.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopProductDao;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.module.shop.service.ProductService;
import com.homlin.utils.Pager;

@Service
public class ProductServiceImpl extends BaseServiceImpl<TbShopProduct, String> implements ProductService {

	@Autowired
	public void setBaseDao(TbShopProductDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopProductDao productDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return productDao.getPagedList(pager);
	}

	@Override
	public Pager getFavoriteProducts(TbShopMember member, Pager pager) {
		return productDao.getFavoriteProducts(member, pager);
	}

	@Override
	public String getLastProductSyssn() {
		return productDao.getLastProductSyssn();
	}

	@Override
	public void setValue(String[] ids, String field, String value) {
		productDao.setValue(ids, field, value);
	}

	@Override
	public List<TbShopProduct> getProductForWeixinReply(String keyword, int num) {
		return productDao.getProductForWeixinReply(keyword.toString(), num);
	}

}
