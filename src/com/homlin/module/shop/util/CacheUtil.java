package com.homlin.module.shop.util;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.stereotype.Component;

import com.homlin.app.SpringContextHolder;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.model.TbShopAdvert;
import com.homlin.module.shop.model.TbShopArticleCategory;
import com.homlin.module.shop.model.TbShopConfig;
import com.homlin.module.shop.service.AdvertService;
import com.homlin.module.shop.service.ArticleCategoryService;
import com.homlin.module.shop.service.ConfigService;
import com.homlin.module.shop.service.NavigationService;
import com.homlin.module.shop.service.ShippingMethodService;

@Component
public class CacheUtil {

	// [[ COMMON ]]

	public static void viewCacheKeys(String cacheName) {
		System.err.println("*************");
		Cache cache = CacheUtil.getCache(cacheName);
		List<?> keys = cache.getKeys();
		for (Object key : keys) {
			System.err.println(key);
		}
		System.err.println("*************");
	}

	public static Cache getCache(String cacheName) {
		CacheManager manager = CacheManager.getInstance();
		return manager.getCache(cacheName);
	}

	public static Object getCacheValue(String cacheName, String key) {
		Cache cache = getCache(cacheName);
		Element element = cache.get(key);
		if (element == null) {
			return null;
		}
		return element.getObjectValue();
	}

	public static void remove(String cacheName, String key) {
		try {
			Cache cache = getCache(cacheName);
			cache.remove(key);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public static void removeAll(String cacheName) {
		try {
			Cache cache = getCache(cacheName);
			cache.removeAll();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	// public static void removeAll() {
	// CacheManager manager = CacheManager.getInstance();
	// String[] cacheNames = manager.getCacheNames();
	// for (String cacheName : cacheNames) {
	// manager.getCache(cacheName).removeAll();
	// }
	// }

	// ]] COMMON ]]

	// ==============

	// [[ 系统设置 ]]

	public static void initConfigCache() {
		// 初始化系统设置
		Cache cache = getConfigCache();
		List<TbShopConfig> configs = SpringContextHolder.getBean(ConfigService.class).findAll();
		for (TbShopConfig config : configs) {
			Element element = new Element(config.getCfgKey(), config.getCfgValue());
			cache.put(element);
		}
	}

	public static Cache getConfigCache() {
		return getCache(EhCacheNames.config);
	}

	public static String getConfig(String key) {
		Cache cache = getConfigCache();
		Element element = cache.get(key);
		if (element == null) {
			initConfigCache();
			element = cache.get(key);
		}
		return element == null ? null : element.getObjectValue().toString();
	}

	public static void setConfig(String key, String value) {
		Cache cache = getConfigCache();
		Element element = new Element(key, value);
		cache.put(element);
	}

	// ]] 系统设置 ]]

	// [[ 广告位、自定义区 ]]

	public static Object getAdverts(String key) {
		Cache cache = getCache(EhCacheNames.advert);
		Element element = cache.get(key);
		if (element == null) {
			List<TbShopAdvert> adverts = SpringContextHolder.getBean(AdvertService.class).getByKeyword(key);
			element = new Element(key, adverts);
			cache.put(element);
		}
		return element.getObjectValue();
	}

	public static Object getAdvert(String key) {
		List<?> list = (List<?>) getAdverts(key);
		if (null == list || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	// ]] 广告位、自定义区 ]]

	// [[ 导航菜单 ]]

	public static Object getNavigation(String key) {
		Cache cache = getCache(EhCacheNames.navigation);
		Element element = cache.get(key);
		if (element == null) {
			SpringContextHolder.getBean(NavigationService.class).getByPosition(key);
			element = cache.get(key);
		}
		return element.getObjectValue();
	}

	// ]] 导航菜单 ]]

	// [[ 配送方式 ]]

	public static Object getShippingMethods() {
		Cache cache = getCache(EhCacheNames.common);
		String key = "ShippingMethods";
		Element element = cache.get(key);
		if (element == null) {
			SpringContextHolder.getBean(ShippingMethodService.class).getAllForSelect();
			element = cache.get(key);
		}
		return element.getObjectValue();
	}

	// ]]

	// [[ 文章分类 ]]

	public static Object getArticleCategoryIdByCode(String code) {
		Cache cache = getCache(EhCacheNames.article_category);
		Element element = cache.get(code);
		if (element == null) {
			String category_id = null;
			TbShopArticleCategory category = SpringContextHolder.getBean(ArticleCategoryService.class).getByCode(code);
			if (null != category) {
				category_id = category.getId();
			}
			element = new Element(code, category_id);
			cache.put(element);
		}
		return element.getObjectValue();
	}

	// ]]

}
