package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopMemberScoreLogDao;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberScoreLog;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopMemberScoreLogDaoImpl extends BaseDaoImpl<TbShopMemberScoreLog, String> implements TbShopMemberScoreLogDao {

	@Override
	public Pager getPagedList(TbShopMember queryMember, Pager pager) {
		String hql = HqlHelper.selectMap("createDate,id,operator,remark,val,valtype");
		hql += " from TbShopMemberScoreLog where tbShopMember=? order by createDate desc";
		List<Object> queryParams = new ArrayList<Object>();
		queryParams.add(queryMember);
		pager.setQueryParams(queryParams.toArray());
		pager.setHql(hql);
		return findByPage(pager);
	}

}
