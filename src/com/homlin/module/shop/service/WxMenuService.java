package com.homlin.module.shop.service;

import java.util.List;
import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbWxMenu;

public interface WxMenuService extends BaseService<TbWxMenu, String> {

	List<Map<String, Object>> getTreeData(String pid);

	List<TbWxMenu> getSynchrodMenus();

}
