package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopMemberDao;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopMemberDaoImpl extends BaseDaoImpl<TbShopMember, String> implements TbShopMemberDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = "select new map(";
		hql += HqlHelper
				.mapping(
						"m.address,m.areaCode,m.birthday,m.cardno,m.city,m.createDate,m.deposit,m.depositAddup,m.district,m.email,m.enabled,m.fax,m.gender,m.id" +
								",m.loginCount,m.loginDate,m.loginFailureCount,m.loginFailureDate,m.loginIp,m.mobile,m.modifyDate,m.name,m.phone,m.postcode,m.province,m.qq,m.registDate,m.registIp" +
								",m.registIparea,m.remark,m.score,m.scoreAddup,m.username,m.wangwang", "m");
		hql += ",isnull(g.name,'-') as gradeName";
		hql += ") from TbShopMember as m left join m.tbShopMemberGrade as g";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "username";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and m.username like ?";
				queryParams.add("%" + value + "%");
			}
			key = "name";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and m.name like ?";
				queryParams.add("%" + value + "%");
			}
			key = "mobile";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and m.mobile like ?";
				queryParams.add("%" + value + "%");
			}
			key = "email";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and m.email like ?";
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());

			// 排序
			if (params.containsKey("sortField")) {
				hql += " order by " + params.get("sortField");
				hql += " " + params.get("sortOrder");
			} else {
				// hql += " order by createDate";
			}
		} else {
			// hql += " order by createDate";
		}

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public void updateLoginInfo(TbShopMember member) {
		String hql = "update TbShopMember set loginCount=?,loginDate=?,loginIp=?,loginFailureCount=?,loginFailureDate=? where id=?";
		update(hql, member.getLoginCount(), member.getLoginDate(), member.getLoginIp(), member.getLoginFailureCount()
				, member.getLoginFailureDate(), member.getId());
	}

	@Override
	public void setEnabled(String[] ids, String enabled) {
		String hql = "update TbShopMember set enabled=? where id in (:ids)";
		Query query = getSession().createQuery(hql);
		// query.setParameter(0, inuse).setParameterList("ids", ids).executeUpdate();
		setParameters(query, enabled);
		query.setParameterList("ids", ids).executeUpdate();
	}

}
