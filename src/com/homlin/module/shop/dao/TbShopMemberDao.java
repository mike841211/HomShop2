package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.utils.Pager;

public interface TbShopMemberDao extends BaseDao<TbShopMember, String> {

	Pager getPagedList(Pager pager);

	void updateLoginInfo(TbShopMember member);

	void setEnabled(String[] ids, String enabled);

}
