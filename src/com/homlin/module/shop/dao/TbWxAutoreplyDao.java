package com.homlin.module.shop.dao;

import java.util.List;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbWxAutoreply;
import com.homlin.utils.Pager;

public interface TbWxAutoreplyDao extends BaseDao<TbWxAutoreply, String> {

	Pager getPagedList(Pager pager);

	void setValue(String[] ids, String field, String value);

	List<TbWxAutoreply> findByKeyword(String keyword, int num);

	void updateHits(String id);

}
