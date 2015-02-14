package com.homlin.module.shop.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.exception.MessageException;
import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopMemberGradeDao;
import com.homlin.module.shop.model.TbShopMemberGrade;
import com.homlin.module.shop.service.MemberGradeService;

@Service
public class MemberGradeServiceImpl extends BaseServiceImpl<TbShopMemberGrade, String> implements MemberGradeService {

	@Autowired
	public void setBaseDao(TbShopMemberGradeDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopMemberGradeDao memberGradeDao;

	@Override
	public List<Map<String, Object>> getAll() {
		return memberGradeDao.getAll();
	}

	@Override
	public void delete(String[] ids) throws Exception {
		for (String id : ids) {
			TbShopMemberGrade memberGrade = memberGradeDao.get(id);
			if (memberGrade.getTbShopMembers().size() > 0) {
				throw new MessageException("删除失败：等级下存在用户");
			}
			memberGradeDao.delete(memberGrade);
		}
	}

	@Override
	public TbShopMemberGrade getFirstLeverGrade() {
		return memberGradeDao.getFirstLeverGrade();
	}

	@Override
	public List<Map<String, Object>> getAllForSelect() {
		return memberGradeDao.getAllForSelect();
	}

}
