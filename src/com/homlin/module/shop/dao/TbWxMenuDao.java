package com.homlin.module.shop.dao;

import java.util.List;
import java.util.Map;

import com.homlin.app.dao.BaseDao;
import com.homlin.module.shop.model.TbWxMenu;

public interface TbWxMenuDao extends BaseDao<TbWxMenu, String> {

	List<Map<String, Object>> getTreeData(String pid);

	List<TbWxMenu> getSynchrodMenus();

}
