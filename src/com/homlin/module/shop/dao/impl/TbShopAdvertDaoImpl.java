package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopAdvertDao;
import com.homlin.module.shop.model.TbShopAdvert;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopAdvertDaoImpl extends BaseDaoImpl<TbShopAdvert, String> implements TbShopAdvertDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = HqlHelper.selectMap("createDate,displayorder,id,imgpath,inuse,keyword,link,modifyDate,remark,title,products");
		hql += " from TbShopAdvert";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "keyword";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and keyword like ?";
				queryParams.add("%" + value + "%");
			}
			key = "title";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and title like ?";
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());

		}
		hql += " order by keyword,displayorder";

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public void setInuse(String[] ids, String inuse) {
		String hql = "update TbShopAdvert set inuse=? where id in (:ids)";
		Query query = getSession().createQuery(hql);
		// query.setParameter(0, inuse).setParameterList("ids", ids).executeUpdate();
		setParameters(query, inuse);
		query.setParameterList("ids", ids).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopAdvert> getByKeyword(String keyword) {
		String hql = "from TbShopAdvert where isnull(inuse,'0')='1' and keyword=? order by displayorder";
		return (List<TbShopAdvert>) find(hql, keyword);
	}

}
