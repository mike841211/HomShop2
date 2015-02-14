package com.homlin.module.shop.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopProductDao;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbShopProductDaoImpl extends BaseDaoImpl<TbShopProduct, String> implements TbShopProductDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = "select new map(";
		hql += HqlHelper
				.mapping("p.attachments,p.barcode,p.blockedStock,p.createDate,p.htmlPath,p.id,p.imageStore"
						+ ",p.isHot,p.isNew,p.isPromotion,p.isRecomend,p.isSale,p.isFreeShipping,p.keyword,p.marketPrice,p.metaDescription,p.metaKeyword,p.modifyDate"
						+ ",p.name,p.price,p.sales,p.sampleImage,p.slogan,p.sn,p.syssn,p.stock,p.unit,p.score,p.scoreCount,p.scoreTotal"
						, "p");
		hql += ",isnull(c.name,'[未分类]') as categoryName";
		hql += ") from TbShopProduct as p left join p.tbShopProductCategory as c";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;

			// 前台查询参数

			key = "isSale";
			if (params.containsKey(key)) {
				hql += " and isnull(p.isSale,'0')='1'";
			}
			key = "hasStock";
			if (params.containsKey(key)) {
				hql += " and isnull(p.stock,0)>0";
			}
			// 新品、热销、促销、推荐
			key = "isNew";
			if (params.containsKey(key)) {
				hql += " and isnull(p.isNew,'0')='1'";
			}
			key = "isHot";
			if (params.containsKey(key)) {
				hql += " and isnull(p.isHot,'0')='1'";
			}
			key = "isPromotion";
			if (params.containsKey(key)) {
				hql += " and isnull(p.isPromotion,'0')='1'";
			}
			key = "isRecomend";
			if (params.containsKey(key)) {
				hql += " and isnull(p.isRecomend,'0')='1'";
			}

			key = "minprice"; // 最低价
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and p.price>=?";
				queryParams.add(new BigDecimal(value));
			}
			key = "maxprice"; // 最高价
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and p.price<=?";
				queryParams.add(new BigDecimal(value));
			}

			// 品牌
			key = "bid";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and p.tbShopBrand.id=?";
				queryParams.add(value);
			}

			// 关键字
			key = "s";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and (p.sn like ? or p.name like ? or p.keyword like ?)";
				queryParams.add("%" + value + "%");
				queryParams.add("%" + value + "%");
				queryParams.add("%" + value + "%");
			}

			// ---

			key = "cid";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and c.indexpath like ?";
				queryParams.add("%" + value + "%");
			}
			key = "sn";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and p.sn like ?";
				queryParams.add("%" + value + "%");
			}
			key = "name";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " and p.name like ?";
				queryParams.add("%" + value + "%");
			}

			pager.setQueryParams(queryParams.toArray());

			// // 排序
			// if (params.containsKey("sortField")) {
			// hql += " order by " + params.get("sortField");
			// hql += " " + params.get("sortOrder");
			// } else {
			// // hql += " order by p.createDate desc";
			// }
			// } else {
			// // hql += " order by p.createDate desc";
		}
		hql += " order by p.createDate desc";

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public Pager getFavoriteProducts(TbShopMember member, Pager pager) {
		String hql = "select new map(";
		hql += HqlHelper
				.mapping("p.attachments,p.barcode,p.blockedStock,p.createDate,p.htmlPath,p.id,p.imageStore"
						+ ",p.isHot,p.isNew,p.isPromotion,p.isRecomend,p.isSale,p.isFreeShipping,p.keyword,p.marketPrice,p.metaDescription,p.metaKeyword,p.modifyDate"
						+ ",p.name,p.price,p.sales,p.sampleImage,p.slogan,p.sn,p.syssn,p.stock,p.unit", "p");
		hql += ") from TbShopProduct as p join p.tbShopMembers as m";

		hql += " where m=?";
		List<Object> queryParams = new ArrayList<Object>();
		// Map<String, Object> params = pager.getParams();
		// queryParams.add(params.get("member"));
		queryParams.add(member);
		pager.setQueryParams(queryParams.toArray());

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public String getLastProductSyssn() {
		String hql = "select max(syssn) from TbShopProduct";
		return (String) findOne(hql);
	}

	@Override
	public void setValue(String[] ids, String field, String value) {
		String hql = "update TbShopProduct set " + field + "=? where id in (:ids)";
		Query query = getSession().createQuery(hql);
		// query.setParameter(0, value).setParameterList("ids", ids).executeUpdate();
		setParameters(query, value);
		query.setParameterList("ids", ids).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopProduct> getProductForWeixinReply(String keyword, int num) {
		String hql = "from TbShopProduct where isnull(isSale,'0')='1' and  isnull(" + keyword + ",'0')='1'";
		return (List<TbShopProduct>) findTop(hql, num);
	}

}
