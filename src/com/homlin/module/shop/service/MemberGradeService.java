package com.homlin.module.shop.service;

import java.util.List;
import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopMemberGrade;

public interface MemberGradeService extends BaseService<TbShopMemberGrade, String> {

	List<Map<String, Object>> getAll();

	TbShopMemberGrade getFirstLeverGrade();

	List<Map<String, Object>> getAllForSelect();

}
