package com.homlin.module.shop.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbShopMemberGrade;

public interface TbShopMemberGradeDao extends BaseDao<TbShopMemberGrade, String> {

	List<Map<String, Object>> getAll();

	TbShopMemberGrade getFirstLeverGrade();

	List<Map<String, Object>> getAllForSelect();

	TbShopMemberGrade findByScore(BigDecimal score);

}
