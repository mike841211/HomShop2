package com.homlin.module.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.dao.TbWxMenuMsgDao;
import com.homlin.module.shop.model.TbWxMenuMsg;
import com.homlin.module.shop.service.WxMenuMsgService;
import com.homlin.utils.Pager;

@Service
public class WxMenuMsgServiceImpl extends BaseServiceImpl<TbWxMenuMsg, String> implements WxMenuMsgService {

	@Autowired
	public void setBaseDao(TbWxMenuMsgDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbWxMenuMsgDao tbWxMenuMsgDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return tbWxMenuMsgDao.getPagedList(pager);
	}

	@Override
	@CacheEvict(value = EhCacheNames.wx_menu_msg, allEntries = true)
	public void setValue(String[] ids, String field, String value) {
		tbWxMenuMsgDao.setValue(ids, field, value);
	}

	@Override
	@Cacheable(value = EhCacheNames.wx_menu_msg)
	public String getTextContent(String menu_id) {
		return tbWxMenuMsgDao.getTextContent(menu_id);
	}

	@Override
	@Cacheable(value = EhCacheNames.wx_menu_msg)
	public List<TbWxMenuMsg> getNews(String menu_id, int num) {
		return tbWxMenuMsgDao.getNews(menu_id, num);
	}

	@Override
	@CacheEvict(value = EhCacheNames.wx_menu_msg, key = "#model.tbWxMenu.id")
	public String save(TbWxMenuMsg model) throws Exception {
		return super.save(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.wx_menu_msg, allEntries = true)
	public void update(TbWxMenuMsg model) throws Exception {
		super.update(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.wx_menu_msg, allEntries = true)
	public void delete(String[] ids) throws Exception {
		if (ids != null) {
			for (String id : ids) {
				delete(id);
			}
		}
	}

	@Override
	public void updateHits(String id) {
		tbWxMenuMsgDao.updateHits(id);
	}

}
