package com.homlin.module.shop.dao;

import java.util.List;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbWxMenuMsg;
import com.homlin.utils.Pager;

public interface TbWxMenuMsgDao extends BaseDao<TbWxMenuMsg, String> {

	Pager getPagedList(Pager pager);

	void setValue(String[] ids, String field, String value);

	String getTextContent(String menu_id);

	List<TbWxMenuMsg> getNews(String menu_id, int num);

	void updateHits(String id);

}
