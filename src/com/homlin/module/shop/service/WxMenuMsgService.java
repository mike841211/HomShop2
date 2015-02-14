package com.homlin.module.shop.service;

import java.util.List;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbWxMenuMsg;
import com.homlin.utils.Pager;

public interface WxMenuMsgService extends BaseService<TbWxMenuMsg, String> {

	Pager getPagedList(Pager pager);

	void setValue(String[] ids, String field, String value);

	String getTextContent(String menu_id);

	List<TbWxMenuMsg> getNews(String menu_id, int num);

	void updateHits(String id);

}
