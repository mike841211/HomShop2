package com.homlin.module.shop.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbWxMenuDao;
import com.homlin.module.shop.model.TbWxMenu;
import com.homlin.module.shop.service.WxMenuService;

@Service
public class WxMenuServiceImpl extends BaseServiceImpl<TbWxMenu, String> implements WxMenuService {

	@Autowired
	public void setBaseDao(TbWxMenuDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbWxMenuDao tbWxMenuDao;

	@Override
	public List<Map<String, Object>> getTreeData(String pid) {
		return tbWxMenuDao.getTreeData(pid);
	}

	@Override
	public List<TbWxMenu> getSynchrodMenus() {
		return tbWxMenuDao.getSynchrodMenus();
	}

}
