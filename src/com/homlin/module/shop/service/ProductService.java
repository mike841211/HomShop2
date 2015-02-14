package com.homlin.module.shop.service;

import java.util.List;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.utils.Pager;

public interface ProductService extends BaseService<TbShopProduct, String> {

	Pager getPagedList(Pager pager);

	Pager getFavoriteProducts(TbShopMember member, Pager pager);

	String getLastProductSyssn();

	void setValue(String[] ids, String field, String value);

	List<TbShopProduct> getProductForWeixinReply(String keyword, int num);

}
