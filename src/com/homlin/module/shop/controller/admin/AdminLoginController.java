package com.homlin.module.shop.controller.admin;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopAdmin;
import com.homlin.module.shop.model.TbShopRole;
import com.homlin.module.shop.service.AdminService;
import com.homlin.utils.CaptchaServiceSingleton;
import com.homlin.utils.IPUtil;
import com.homlin.utils.Util;
import com.octo.captcha.service.CaptchaServiceException;

@Controller
@RequestMapping("/admin")
public class AdminLoginController extends BaseAdminController {

	private final String RETURN_PATH = "/admin";

	@RequestMapping(value = { "/index" })
	public String admin_index() {
		return RETURN_PATH + "/index";
	}

	@RequestMapping(value = "/menudata")
	public String admin_menudata(Model model) {
		return RETURN_PATH + "/menudata";
	}

	// =====

	@Autowired
	private AdminService adminService;

	// ============
	// [[ 登入 ]]
	// ============

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return RETURN_PATH + "/login";
	}

	// 调试登陆
	public String debug_login(String username) throws Exception {
		TbShopAdmin admin = adminService.findByUsername(username);
		if (admin == null) {
			return "用户名错误";
		}

		// 是否超级管理员
		boolean issuper = false;
		if (admin.getId().equalsIgnoreCase(getRequest().getServletContext().getInitParameter("SUPER_ADMIN_ID"))) {
			issuper = true;
		}

		// 用户角色权限
		Set<String> authoritySet = new HashSet<String>();
		if (!issuper) {
			Set<TbShopRole> tbShopRoles = admin.getTbShopRoles();
			for (TbShopRole tbShopRole : tbShopRoles) {
				if (tbShopRole.getAuthority().isEmpty()) {
					continue;
				}
				String strings[] = StringUtils.split(tbShopRole.getAuthority().toString(), ",");
				for (String string : strings) {
					authoritySet.add(string);
				}
			}
		}

		// 登录HttpSession
		HttpSession session = getRequest().getSession();
		session.setAttribute(AppConstants.SESSION_ADMIN_ISLOGIN, true);
		session.setAttribute(AppConstants.SESSION_ADMIN, admin);
		session.setAttribute(AppConstants.SESSION_ADMIN_ID, admin.getId());
		session.setAttribute(AppConstants.SESSION_ADMIN_USERNAME, admin.getUsername());
		session.setAttribute(AppConstants.SESSION_ADMIN_NAME, admin.getName());
		session.setAttribute(AppConstants.SESSION_ADMIN_AUTHORITY, authoritySet);
		session.setAttribute(AppConstants.SESSION_ADMIN_ISSUPER, issuper);

		return "登入成功";
	}

	public String login(String username, String password) throws Exception {
		TbShopAdmin admin = adminService.findByUsername(username);
		String errorMessage = null;
		if (admin == null) {
			errorMessage = "用户名或密码错误";
		} else if (!Util.md5(password, username.toLowerCase()).equals(admin.getPassword())) {
			errorMessage = "密码或用户名错误";
		} else if (AppConstants.TRUE.equals(admin.getLock())) {
			errorMessage = "帐号已禁用";
		}
		if (errorMessage != null) {
			actionlog("管理员登录", "帐号：" + username + " 登录失败，" + errorMessage);
			// todo 限制错误次数
			return ajaxJsonErrorMessage(errorMessage);
		}

		// 是否超级管理员
		boolean issuper = false;
		if (admin.getId().equalsIgnoreCase(getRequest().getServletContext().getInitParameter("SUPER_ADMIN_ID"))) {
			issuper = true;
		}

		// 用户角色权限
		Set<String> authoritySet = new HashSet<String>();
		if (!issuper) {
			Set<TbShopRole> tbShopRoles = admin.getTbShopRoles();
			for (TbShopRole tbShopRole : tbShopRoles) {
				if (tbShopRole.getAuthority().isEmpty()) {
					continue;
				}
				String strings[] = StringUtils.split(tbShopRole.getAuthority().toString(), ",");
				for (String string : strings) {
					authoritySet.add(string);
				}
			}
		}

		// 登录HttpSession
		HttpSession session = getRequest().getSession();
		session.setAttribute(AppConstants.SESSION_ADMIN_ISLOGIN, true);
		session.setAttribute(AppConstants.SESSION_ADMIN, admin);
		session.setAttribute(AppConstants.SESSION_ADMIN_ID, admin.getId());
		session.setAttribute(AppConstants.SESSION_ADMIN_USERNAME, admin.getUsername());
		session.setAttribute(AppConstants.SESSION_ADMIN_NAME, admin.getName());
		session.setAttribute(AppConstants.SESSION_ADMIN_AUTHORITY, authoritySet);
		session.setAttribute(AppConstants.SESSION_ADMIN_ISSUPER, issuper);

		// loginLog
		admin.setLoginCount(admin.getLoginCount() + 1);
		admin.setLoginDate(Util.getNowDateTimeString());
		admin.setLoginIp(IPUtil.getRemoteIP(getRequest()));
		admin.setLoginFailureCount(0);

		actionlog("管理员登录", "帐号：" + username + "，" + getRequest().getHeader("User-Agent"));

		if (isAjaxRequest()) {
			return ajaxJsonSuccessMessage("登入成功");
		} else {
			return RETURN_PATH;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String username, String password, String jcaptcha) throws Exception {
		Boolean isCaptchaCorrect = Boolean.FALSE;
		try {
			String captchaId = getRequest().getSession().getId();
			// String jcaptcha = getRequest().getParameter(JCaptchaEngine.CAPTCHA_INPUT_NAME).toUpperCase();
			jcaptcha = jcaptcha.toUpperCase();
			isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId, jcaptcha);
		} catch (CaptchaServiceException e) {
			// should not happen, may be thrown if the id is not valid
			// e.printStackTrace();
		} catch (Exception e) {
		}
		if (!isCaptchaCorrect) {
			return ajaxJsonErrorMessage("验证码输入错误!");
		}

		return login(username, password);
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return RETURN_PATH + "/login";
	}

	// ============
	// ]] 登入 ]]
	// ============
	

}
