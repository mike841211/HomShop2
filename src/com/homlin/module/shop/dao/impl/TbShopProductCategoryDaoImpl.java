package com.homlin.module.shop.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbShopProductCategoryDao;
import com.homlin.module.shop.model.TbShopProductCategory;
import com.homlin.utils.HqlHelper;

@Repository
public class TbShopProductCategoryDaoImpl extends BaseDaoImpl<TbShopProductCategory, String> implements TbShopProductCategoryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getProductCatagoryTreeData(String pid) {
		String hql = "select new map(";
		hql += HqlHelper.mapping("cate.id,cate.code,cate.name,cate.inuse,cate.displayorder", new String[] { "cate" });
		hql += ",cate.tbShopProductCategory.id as pid";
		hql += ",case when exists(from TbShopProductCategory as subCate where subCate.tbShopProductCategory = cate) then 'false' else 'true' end as isLeaf";
		hql += ") from TbShopProductCategory as cate";
		Object[] params = new Object[] {};
		if (StringUtils.isBlank(pid)) {
			hql += " where cate.tbShopProductCategory is null";
		} else {
			hql += " where cate.tbShopProductCategory.id=?";
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
		String hql = "update TbShopProductCategory set indexpath=replace(indexpath,?,?) where indexpath like ?";
		update(hql, old_indexpath, new_indexpath, "'%" + old_indexpath + ",%'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopProductCategory> getSubCategories(String category_id, boolean siblings) {
		String hql;
		List<?> list;
		if (StringUtils.isEmpty(category_id)) {
			hql = "from TbShopProductCategory where tbShopProductCategory is null and isnull(inuse,'0')='1' order by displayorder";
			list = find(hql);
		} else {
			hql = "from TbShopProductCategory where tbShopProductCategory.id=? and isnull(inuse,'0')='1' order by displayorder";
			list = find(hql, category_id);
			if (list.size() == 0 && siblings) {
				TbShopProductCategory category = get(category_id);
				if (category == null) {
					return getSubCategories("", siblings);
				}
				if (category.getTbShopProductCategory() == null) {
					return getSubCategories("", siblings);
				} else {
					return getSubCategories(category.getTbShopProductCategory().getId(), siblings);
				}
			}
		}
		return (List<TbShopProductCategory>) list;
	}

}
