package com.homlin.module.shop.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopNavigationDao;
import com.homlin.module.shop.model.TbShopNavigation;

@Repository
public class TbShopNavigationDaoImpl extends BaseDaoImpl<TbShopNavigation, String> implements TbShopNavigationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopNavigation> getAll() {
		String hql = "from TbShopNavigation order by position,displayorder";
		return (List<TbShopNavigation>) find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopNavigation> getByPosition(String position) {
		String hql = "from TbShopNavigation where isnull(isShow,'0')='1' and position=? order by displayorder";
		return (List<TbShopNavigation>) find(hql, position);
	}

	@Override
	public void setIsShow(String[] ids, String isShow) {
		String hql = "update TbShopNavigation set isShow=? where id in (:ids)";
		Query query = getSession().createQuery(hql);
		setParameters(query, isShow);
		query.setParameterList("ids", ids).executeUpdate();
	}

}
