package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.dao.TbShopRoleDao;
import com.homlin.module.shop.model.TbShopRole;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Pager;

@Repository
public class TbShopRoleDaoImpl extends BaseDaoImpl<TbShopRole, String> implements TbShopRoleDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String hql = HqlHelper.selectMap("authority,createDate,description,id,modifyDate,name");
		hql += " FROM TbShopRole";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			hql += " WHERE 1=1";
			String key, value;
			key = "name";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				hql += " AND name LIKE '%" + value + "%'";
			}
			// 排序
			if (params.containsKey("sortField")) {
				hql += " ORDER BY " + params.get("sortField");
				hql += " " + params.get("sortOrder");
			} else {
				hql += " ORDER BY createDate";
			}
		} else {
			hql += " ORDER BY createDate";
		}

		pager.setHql(hql);
		return findByPage(pager);
	}

	@Override
	public Map<String, Object> getRoleAuthorities() {

		class Item {
			private String text;
			private String value;

			@SuppressWarnings("unused")
			public String getText() {
				return this.text;
			}

			@SuppressWarnings("unused")
			public String getValue() {
				return this.value;
			}

			public Item(String text, String value) {
				this.text = text;
				this.value = value;
			}

			// @Override
			// public String toString() {
			// return "{\"text\":\"" + getText() + "\",\"value\":\"" + getValue() + "\"}";
			// }
		}

		// XXX 配置角色权限,controller权限也要配置
		Map<String, Object> group = new LinkedHashMap<String, Object>(); // 权限分组
		List<Item> list; // 分组权限列表

		list = new ArrayList<Item>();
		list.add(new Item("商品管理", AppConstants.PRODUCT));
		list.add(new Item("商品分类", AppConstants.PRODUCT_CATEGORY));
		list.add(new Item("规格管理", AppConstants.SPECIFICATION));
		list.add(new Item("品牌管理", AppConstants.BRAND));
		// list.add(new Item("运费模版", AppConstants.FREIGHT_TEMPLATE));
		list.add(new Item("商品评价", AppConstants.PRODUCT_COMMENT));
		group.put("商品管理", JacksonUtil.toJsonString(list));

		list = new ArrayList<Item>();
		list.add(new Item("订单管理", AppConstants.ORDER));
		list.add(new Item("配送方式", AppConstants.SHIPPING_METHOD));
		group.put("订单管理", JacksonUtil.toJsonString(list));

		list = new ArrayList<Item>();
		list.add(new Item("文章管理", AppConstants.ARTICLE));
		list.add(new Item("文章分类管理", AppConstants.ARTICLE_CATEGORY));
		list.add(new Item("广告位/自定义区", AppConstants.ADVERT));
		list.add(new Item("导航管理", AppConstants.NAVIGATION));
		// list.add(new Item("友情链接", AppConstants.FRIENDLINK));
		group.put("页面内容", JacksonUtil.toJsonString(list));

		list = new ArrayList<Item>();
		list.add(new Item("会员管理", AppConstants.MEMBER));
		list.add(new Item("会员等级", AppConstants.MEMBER_GRADE));
		// list.add(new Item("发送手机短信", AppConstants.SENTMESSAGE));
		group.put("会员管理", JacksonUtil.toJsonString(list));

		list = new ArrayList<Item>();
		list.add(new Item("菜单设置", AppConstants.WEIXIN_MENU));
		list.add(new Item("菜单消息设置", AppConstants.WEIXIN_MENUMSG));
		list.add(new Item("自动回复", AppConstants.WEIXIN_AUTOREPLY));
		list.add(new Item("分组管理", AppConstants.WEIXIN_GROUP));
		list.add(new Item("用户管理", AppConstants.WEIXIN_USER));
		list.add(new Item("消息管理", AppConstants.WEIXIN_MSG));
		list.add(new Item("维权仲裁", AppConstants.WEIXIN_PAYFEEDBACK));
		group.put("微信设置", JacksonUtil.toJsonString(list));

		list = new ArrayList<Item>();
		list.add(new Item("账号管理", AppConstants.ADMIN));
		list.add(new Item("角色管理", AppConstants.ROLE));
		list.add(new Item("日志管理", AppConstants.ACTIONLOG));
		list.add(new Item("系统设置", AppConstants.CONFIG));
		list.add(new Item("缓存管理", AppConstants.CACHE));
		group.put("系统管理", JacksonUtil.toJsonString(list));

		return group;
	}

}
