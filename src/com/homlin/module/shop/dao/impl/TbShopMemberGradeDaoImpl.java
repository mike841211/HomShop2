package com.homlin.module.shop.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopMemberGradeDao;
import com.homlin.module.shop.model.TbShopMemberGrade;
import com.homlin.utils.HqlHelper;

@Repository
public class TbShopMemberGradeDaoImpl extends BaseDaoImpl<TbShopMemberGrade, String> implements TbShopMemberGradeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAll() {
		String hql = "select new map(";
		hql += HqlHelper.mapping("createDate,discount,id,isSpecial,lever,modifyDate,name,remark,score");
		hql += ") from TbShopMemberGrade order by lever asc";
		return (List<Map<String, Object>>) find(hql);
	}

	@Override
	public TbShopMemberGrade getFirstLeverGrade() {
		String hql = "from TbShopMemberGrade order by lever asc";
		return (TbShopMemberGrade) findOne(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAllForSelect() {
		String hql = "select new map(id as id,name as name) from TbShopMemberGrade order by lever";
		return (List<Map<String, Object>>) find(hql);
	}

	@Override
	public TbShopMemberGrade findByScore(BigDecimal score) {
		String hqlString = "from TbShopMemberGrade where isnull(isSpecial,'0')<>'1' and score<=? order by lever desc";
		return (TbShopMemberGrade) findOne(hqlString, score);
	}

}
