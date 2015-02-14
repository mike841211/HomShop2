package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopProductCommentDao;
import com.homlin.module.shop.model.TbShopProductComment;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopProductCommentDaoImpl extends BaseDaoImpl<TbShopProductComment, String> implements TbShopProductCommentDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = HqlHelper
				.selectMap("contact,contents,createDate,id,images,ip,isDelete,isShow,modifyDate,productName,replyContent,replyDate,score,specifications,username" +
						",tbShopProduct.id as product_id,tbShopSku.id as sku_id,tbShopOrderItem.id as orderItem_id,tbShopMember.id as member_id");
		hql += " from TbShopProductComment where isnull(isDelete,'0')='0'";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "isShow";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and isnull(isShow,'0') = ?";
				queryParams.add(value);
			}
			key = "ip";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and ip = ?";
				queryParams.add(value);
			}
			key = "username";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and username like ?";
				queryParams.add("%" + value + "%");
			}
			key = "contents";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and username like ?";
				queryParams.add("%" + value + "%");
			}
			key = "replyContent";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and replyContent like ?";
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());

		}
		hql += " order by createDate desc";

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public void setBoolean(String[] ids, String prop, String bool) {
		String hql = "update TbShopProductComment set " + prop + "=? where id in (:ids)";
		Query query = getSession().createQuery(hql);
		// query.setParameter(0, bool).setParameterList("ids", ids).executeUpdate();
		setParameters(query, bool);
		query.setParameterList("ids", ids).executeUpdate();
	}

}
