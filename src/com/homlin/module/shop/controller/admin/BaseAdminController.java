package com.homlin.module.shop.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.homlin.app.controller.BaseController;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopActionlog;
import com.homlin.module.shop.model.TbShopAdmin;
import com.homlin.module.shop.service.ActionlogService;
import com.homlin.utils.IPUtil;
import com.homlin.utils.Util;

public class BaseAdminController extends BaseController {

	// ===================
	// [[ 操作记录 ]]
	// ===================

	@Autowired
	private ActionlogService actionlogService;

	protected ActionlogService getActionlogService() {
		return actionlogService;
	}

	/**
	 * 操作记录
	 * 
	 * @param action
	 *            操作
	 * @param detail
	 *            操作内容
	 * @throws Exception
	 */
	protected void actionlog(String action, String detail) throws Exception {
		TbShopActionlog tbShopActionlog = new TbShopActionlog();
		tbShopActionlog.setAction(action);
		tbShopActionlog.setDetail(detail);
		tbShopActionlog.setCreateDate(Util.getNowDateTimeString());
		tbShopActionlog.setUsername(getAdminUsername());
		tbShopActionlog.setIp(IPUtil.getRemoteIP(getRequest()));
		getActionlogService().save(tbShopActionlog);
	}

	// ===================
	// ]] 操作记录 ]]
	// ===================

	// ===========================
	// [[ 读取 Admin Session 值 ]]
	// ===========================

	public boolean isAdminLogin() {
		Object object = getSession(AppConstants.SESSION_ADMIN_ISLOGIN);
		if (object == null) {
			return false;
		}
		return (Boolean) object;
	}

	// 超级，整个系统权限
	public boolean isSuperAdmin() {
		Object object = getSession(AppConstants.SESSION_ADMIN_ISSUPER);
		if (object == null) {
			return false;
		}
		return (Boolean) object;
	}

	public TbShopAdmin getAdmin() {
		return (TbShopAdmin) getSession(AppConstants.SESSION_ADMIN);
	}

	/**
	 * 获取登入用户ID
	 * 
	 * @return
	 */
	public String getAdminId() {
		return (String) getSession(AppConstants.SESSION_ADMIN_ID);
	}

	public String getAdminUsername() {
		return (String) getSession(AppConstants.SESSION_ADMIN_USERNAME);
	}

	// ===========================
	// ]] 读取 Admin Session 值 ]]
	// ===========================

	// ==============
	// [[ TOOLS ]]
	// ==============

	// miniui专用
	protected Map<String, Object> getMiniuiParams(String... keys) {
		HttpServletRequest request = getRequest();
		// 查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		for (String key : keys) {
			String value = request.getParameter(key);
			if (StringUtils.isNotEmpty(value)) {
				params.put(key, value);
			}
		}
		// 指定查询
		String searchkey = request.getParameter("searchkey");
		if (StringUtils.isNotEmpty(searchkey)) {
			String searchby = request.getParameter("searchby");
			if (StringUtils.isNotEmpty(searchby)) {
				params.put(searchby, searchkey);
			}
		}
		// 排序
		String sortField = request.getParameter("sortField");
		if (StringUtils.isNotEmpty(sortField)) {
			String sortOrder = request.getParameter("sortOrder");
			if (StringUtils.isEmpty(sortOrder)) {
				sortOrder = "";
			}
			params.put("sortField", sortField);
			params.put("sortOrder", sortOrder);
		}
		return params;
	}

	// ==============
	// ]] TOOLS ]]
	// ==============

}