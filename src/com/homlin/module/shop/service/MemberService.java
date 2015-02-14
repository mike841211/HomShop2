package com.homlin.module.shop.service;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.utils.Pager;

public interface MemberService extends BaseService<TbShopMember, String> {

	Pager getPagedList(Pager pager);

	TbShopMember getByUsername(String username);

	TbShopMember getByEmail(String email);

	void updateLoginInfo(TbShopMember member);

	Pager getPagedScoreLogs(TbShopMember queryMember, Pager pager);

	TbShopMember getByCardno(String cardno);

	TbShopMember getByMobile(String mobile);

	void setEnabled(String[] ids, String enabled);

}
