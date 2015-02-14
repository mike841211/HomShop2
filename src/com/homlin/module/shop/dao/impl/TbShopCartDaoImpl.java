package com.homlin.module.shop.dao.impl;

import java.util.Date;

import org.hibernate.FlushMode;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopCartDao;
import com.homlin.module.shop.model.TbShopCart;
import com.homlin.utils.Util;

@Repository
public class TbShopCartDaoImpl extends BaseDaoImpl<TbShopCart, String> implements TbShopCartDao {

	@Override
	public void evictExpired(Date expiredDate) {
		String hql = "delete from TbShopCart cart where cart.tbShopMember is null and cart.modifyDate <= :expire";
		getSession().createQuery(hql)
				.setFlushMode(FlushMode.COMMIT)
				.setParameter("expire", Util.getDateTimeString(expiredDate))
				.executeUpdate();
	}

}
