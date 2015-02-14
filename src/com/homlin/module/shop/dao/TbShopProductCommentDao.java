package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopProductComment;
import com.homlin.utils.Pager;

public interface TbShopProductCommentDao extends BaseDao<TbShopProductComment, String> {

	Pager getPagedList(Pager pager);

	void setBoolean(String[] ids, String prop, String bool);

}
