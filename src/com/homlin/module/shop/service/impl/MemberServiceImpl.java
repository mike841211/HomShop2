package com.homlin.module.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopMemberDao;
import com.homlin.module.shop.dao.TbShopMemberScoreLogDao;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.service.MemberService;
import com.homlin.utils.Pager;

@Service
public class MemberServiceImpl extends BaseServiceImpl<TbShopMember, String> implements MemberService {

	@Autowired
	public void setBaseDao(TbShopMemberDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopMemberDao memberDao;

	@Autowired
	TbShopMemberScoreLogDao tbShopMemberScoreLogDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return memberDao.getPagedList(pager);
	}

	@Override
	public String save(TbShopMember member) throws Exception {
		return memberDao.save(member);
	}

	@Override
	public TbShopMember getByUsername(String username) {
		return memberDao.findOneByProperty("username", username);
	}

	@Override
	public TbShopMember getByEmail(String email) {
		return memberDao.findOneByProperty("email", email);
	}

	@Override
	public void update(TbShopMember member) throws Exception {
		memberDao.merge(member);
	}

	@Override
	public void updateLoginInfo(TbShopMember member) {
		memberDao.updateLoginInfo(member);
	}

	@Override
	public Pager getPagedScoreLogs(TbShopMember queryMember, Pager pager) {
		return tbShopMemberScoreLogDao.getPagedList(queryMember, pager);
	}

	@Override
	public TbShopMember getByCardno(String cardno) {
		return memberDao.findOneByProperty("cardno", cardno);
	}

	@Override
	public TbShopMember getByMobile(String mobile) {
		return memberDao.findOneByProperty("mobile", mobile);
	}

	@Override
	public void setEnabled(String[] ids, String enabled) {
		memberDao.setEnabled(ids, enabled);
	}

}
