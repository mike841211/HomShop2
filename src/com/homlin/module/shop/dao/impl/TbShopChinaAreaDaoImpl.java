package com.homlin.module.shop.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopChinaAreaDao;
import com.homlin.module.shop.model.TbShopChinaArea;

@Repository
public class TbShopChinaAreaDaoImpl extends BaseDaoImpl<TbShopChinaArea, String> implements TbShopChinaAreaDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopChinaArea> getProvices() {
		String hql = "from TbShopChinaArea where code like '__0000' order by code";
		return (List<TbShopChinaArea>) find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopChinaArea> getCities(String code) {
		String provice = code.substring(0, 2);
		String hql = "from TbShopChinaArea where code<>? and code like ? order by code";
		return (List<TbShopChinaArea>) find(hql, provice + "0000", provice + "__00");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopChinaArea> getDistricts(String code) {
		String city = code.substring(0, 4);
		String hql = "from TbShopChinaArea where code<>? and code like ? order by code";
		return (List<TbShopChinaArea>) find(hql, city + "00", city + "__");
	}

}
