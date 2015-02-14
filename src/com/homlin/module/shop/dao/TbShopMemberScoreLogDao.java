package com.homlin.module.shop.dao;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberScoreLog;
import com.homlin.utils.Pager;

public interface TbShopMemberScoreLogDao extends BaseDao<TbShopMemberScoreLog, String> {

	Pager getPagedList(TbShopMember queryMember, Pager pager);

}
