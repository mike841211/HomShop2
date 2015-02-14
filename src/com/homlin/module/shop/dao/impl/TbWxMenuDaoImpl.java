package com.homlin.module.shop.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbWxMenuDao;
import com.homlin.module.shop.model.TbWxMenu;
import com.homlin.utils.HqlHelper;

@Repository
public class TbWxMenuDaoImpl extends BaseDaoImpl<TbWxMenu, String> implements TbWxMenuDao {

	@Override
	public List<Map<String, Object>> getTreeData(String pid) {
		String hql = "select new map(";
		hql += HqlHelper
				.mapping("menu.createDate,menu.displayorder,menu.id,menu.inuse,menu.menuType,menu.messageType,menu.modifyDate,menu.name,menu.remark,menu.url", "menu");
		hql += ",menu.tbWxMenu.id as pid";
		hql += ",case when exists(from TbWxMenu as submenu where submenu.tbWxMenu = menu) then 'false' else 'true' end as isLeaf";
		hql += ") from TbWxMenu as menu";
		Object[] params = new Object[] {};
		if (StringUtils.isBlank(pid)) {
			hql += " where menu.tbWxMenu is null";
		} else {
			hql += " where menu.tbWxMenu.id=?";
			params = new Object[] { pid };
		}
		hql += " order by isnull(menu.inuse,'0') desc,menu.displayorder";

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> listMap = (List<Map<String, Object>>) find(hql, params);
		for (Map<String, Object> map : listMap) {
			map.put("isLeaf", Boolean.valueOf(map.get("isLeaf").toString()));
		}
		return listMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbWxMenu> getSynchrodMenus() {
		String hql = "from TbWxMenu where tbWxMenu is null and isnull(inuse,'0')='1' order by displayorder";
		Query query = getSession().createQuery(hql).setMaxResults(3);
		return query.list();
	}

}
