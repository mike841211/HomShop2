package com.homlin.module.shop.service;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopProductComment;
import com.homlin.utils.Pager;

public interface ProductCommentService extends BaseService<TbShopProductComment, String> {

	Pager getPagedList(Pager pager);

	TbShopProductComment getByOrderItemId(String item_id);

	void setBoolean(String[] ids, String prop, String bool);

}
