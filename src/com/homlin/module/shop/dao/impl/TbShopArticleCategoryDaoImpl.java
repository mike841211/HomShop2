package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopArticleCategoryDao;
import com.homlin.module.shop.model.TbShopArticleCategory;
import com.homlin.utils.HqlHelper;

@Repository
public class TbShopArticleCategoryDaoImpl extends BaseDaoImpl<TbShopArticleCategory, String> implements TbShopArticleCategoryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getArticleCategoryTreeData(String pid) {
		String hql = "select new map(";
		hql += HqlHelper.mapping("cate.id,cate.code,cate.name,cate.inuse,cate.displayorder", new String[] { "cate" });
		hql += ",cate.tbShopArticleCategory.id as pid";
		hql += ",case when exists(from TbShopArticleCategory as subCate where subCate.tbShopArticleCategory = cate) then 'false' else 'true' end as isLeaf";
		hql += ") from TbShopArticleCategory as cate";
		Object[] params = new Object[] {};
		if (StringUtils.isBlank(pid)) {
			hql += " where cate.tbShopArticleCategory is null";
		} else {
			hql += " where cate.tbShopArticleCategory.id=?";
			params = new Object[] { pid };
		}
		hql += " order by cate.displayorder";

		List<Map<String, Object>> listMap = (List<Map<String, Object>>) find(hql, params);
		for (Map<String, Object> map : listMap) {
			map.put("isLeaf", Boolean.valueOf(map.get("isLeaf").toString()));
		}
		return listMap;
	}

	@Override
	public void batchUpdateChildrenIndexpath(String old_indexpath, String new_indexpath) {
		String hql = "update TbShopArticleCategory set indexpath=replace(indexpath,?,?) where indexpath like ?";
		update(hql, old_indexpath, new_indexpath, "'%" + old_indexpath + ",%'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopArticleCategory> getSubCategories(String category_id, boolean siblings) {
		String hql;
		List<?> list;
		if (StringUtils.isEmpty(category_id)) {
			hql = "from TbShopArticleCategory where tbShopArticleCategory is null and isnull(inuse,'0')='1' order by displayorder";
			list = find(hql);
		} else {
			hql = "from TbShopArticleCategory where tbShopArticleCategory.id=? and isnull(inuse,'0')='1' order by displayorder";
			list = find(hql, category_id);
			if (list.size() == 0 && siblings) {
				TbShopArticleCategory category = get(category_id);
				if (category == null) {
					return new ArrayList<TbShopArticleCategory>();
				}
				if (category.getTbShopArticleCategory() == null) {
					return getSubCategories("", siblings);
				} else {
					return getSubCategories(category.getTbShopArticleCategory().getId(), siblings);
				}
			}
		}
		return (List<TbShopArticleCategory>) list;
	}

}
