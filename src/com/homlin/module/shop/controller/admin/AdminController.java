package com.homlin.module.shop.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopAdmin;
import com.homlin.module.shop.model.TbShopRole;
import com.homlin.module.shop.service.AdminService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller
@RequestMapping("/admin/admin")
public class AdminController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/admin";

	@Autowired
	private AdminService adminService;

	// ==============
	// [[ 账号管理 ]]
	// ==============

	@CheckAuthority(AppConstants.ADMIN)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.ADMIN)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize, HttpServletRequest request) throws Exception {

		Map<String, Object> params = getMiniuiParams();

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = adminService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.ADMIN)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() throws Exception {
		return RETURN_PATH + "/add";
	}

	@ResponseBody
	@CheckAuthority(AppConstants.ADMIN)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopAdmin admin, String selectroles) throws Exception {
		if (adminService.exists("username", admin.getUsername())) {
			return ajaxJsonErrorMessage("用户名已存在，请重新设置");
		}
		admin.setUsername(admin.getUsername().toLowerCase());
		admin.setPassword(Util.md5(admin.getPassword(), admin.getUsername().toLowerCase()));
		if (admin.getLock() == null) {
			admin.setLock(AppConstants.FALSE);
		}
		admin.setCreateDate(Util.getNowDateTimeString());
		admin.setModifyDate(Util.getNowDateTimeString());
		admin.setLoginCount(0);
		admin.setLoginFailureCount(0);
		String[] roles = selectroles.split(",");
		for (String roleId : roles) {
			admin.getTbShopRoles().add(new TbShopRole(roleId));
		}

		try {
			adminService.save(admin);
			actionlog("新建管理员", "帐号：" + admin.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ADMIN)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopAdmin admin = adminService.get(id);
			Set<TbShopRole> roles = admin.getTbShopRoles();
			List<String> selectroles = new ArrayList<String>();
			for (TbShopRole role : roles) {
				selectroles.add(role.getId());
			}
			model.addAttribute("selectroles", StringUtils.join(selectroles, ","));
			model.addAttribute("admin", admin);
		}
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.ADMIN)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopAdmin admin, String selectroles) throws Exception {
		if (admin.getLock() == null) {
			admin.setLock(AppConstants.FALSE);
		}
		admin.setModifyDate(Util.getNowDateTimeString());
		String[] roles = selectroles.split(",");
		for (String roleId : roles) {
			admin.getTbShopRoles().add(new TbShopRole(roleId));
		}

		TbShopAdmin poAdmin = adminService.get(admin.getId());
		BeanUtils.copyProperties(admin, poAdmin, new String[] { "id", "username", "password", "createDate", "loginDate", "loginIp",
				"loginCount", "loginFailureCount", "loginFailureDate" });
		if (StringUtils.isNotBlank(admin.getPassword())) {
			poAdmin.setPassword(Util.md5(admin.getPassword(), admin.getUsername()));
		}

		try {
			adminService.update(poAdmin);
			actionlog("编辑管理员", "帐号：" + poAdmin.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ADMIN)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			String SUPER_ADMIN_ID = getRequest().getServletContext().getInitParameter("SUPER_ADMIN_ID");
			ids = ids.replace(SUPER_ADMIN_ID, ""); // 超级管理员不可删除
			ids = ids.replace(getAdminId(), ""); // 不可删除自己
			adminService.delete(ids.split(","));
			actionlog("删除管理员", "帐号：" + ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("操作失败");
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

	@RequestMapping(value = "/myInfo", method = RequestMethod.GET)
	public String myInfo(Model model) throws Exception {
		TbShopAdmin admin = adminService.get(getAdminId());
		if (admin == null) {
			addActionError("参数错误");
			return PAGE_ERROR;
		}
		model.addAttribute("admin", admin);
		return RETURN_PATH + "/myInfo";
	}

	@ResponseBody
	@RequestMapping(value = "/updateMyInfo", method = RequestMethod.POST)
	public String updateMyInfo(TbShopAdmin admin, String newPassword) throws Exception {
		TbShopAdmin poAdmin = adminService.get(admin.getId());
		String password = Util.md5(admin.getPassword(), poAdmin.getUsername().toLowerCase());
		if (!password.equals(poAdmin.getPassword())) {
			return ajaxJsonErrorMessage("密码错误");
		}

		if (StringUtils.isNotEmpty(newPassword)) {
			newPassword = Util.md5(newPassword, poAdmin.getUsername().toLowerCase());
			if (!newPassword.equals(poAdmin.getPassword())) {
				poAdmin.setPassword(newPassword);
			}
		}

		poAdmin.setModifyDate(Util.getNowDateTimeString());
		poAdmin.setEmail(admin.getEmail());
		adminService.update(poAdmin);

		actionlog("编辑管理员", "帐号：" + poAdmin.getUsername());

		return ajaxJsonSuccessMessage();
	}

	// ==============
	// ]] 账号管理 ]]
	// ==============

}
