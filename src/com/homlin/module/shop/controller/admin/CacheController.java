package com.homlin.module.shop.controller.admin;

import javax.ws.rs.PathParam;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.util.CacheUtil;

@Controller
@RequestMapping("/admin/cache")
public class CacheController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/cache";

	@CheckAuthority(AppConstants.CACHE)
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return RETURN_PATH + "/index";
	}

	@CheckAuthority(AppConstants.CACHE)
	@ResponseBody
	@RequestMapping(value = "/clear")
	public String clear(@PathParam(value = "cacheName") String cacheName) {
		Cache cache = CacheUtil.getCache(cacheName);
		if (null != cache) {
			cache.removeAll();
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.CACHE)
	@ResponseBody
	@RequestMapping(value = "/clearAll")
	public String clearAll() {
		CacheManager manager = CacheManager.getInstance();
		String[] cacheNames = manager.getCacheNames();
		for (String cacheName : cacheNames) {
			manager.getCache(cacheName).removeAll();
		}
		return ajaxJsonSuccessMessage();
	}
}
