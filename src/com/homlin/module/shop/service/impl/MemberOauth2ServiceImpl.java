package com.homlin.module.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopMemberOauth2Dao;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberOauth2;
import com.homlin.module.shop.service.MemberOauth2Service;

@Service
public class MemberOauth2ServiceImpl extends BaseServiceImpl<TbShopMemberOauth2, String> implements MemberOauth2Service {

	@Autowired
	public void setBaseDao(TbShopMemberOauth2Dao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopMemberOauth2Dao tbShopMemberOauth2Dao;

	@Override
	public TbShopMember getTbShopMember(String openid) {
		TbShopMemberOauth2 tbShopMemberOauth2 = get(openid);
		if (null != tbShopMemberOauth2) {
			return tbShopMemberOauth2.getTbShopMember();
		}
		return null;
	}

}
