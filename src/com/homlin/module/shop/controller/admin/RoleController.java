package com.homlin.module.shop.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopRole;
import com.homlin.module.shop.service.RoleService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/role";

	@Autowired
	RoleService roleService;

	@CheckAuthority(AppConstants.ROLE)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@ResponseBody
	@CheckAuthority(AppConstants.ROLE)
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize, HttpServletRequest request) throws Exception {

		Map<String, Object> params = getMiniuiParams();

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = roleService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.ROLE)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) throws Exception {
		model.addAttribute("authority", roleService.getRoleAuthorities());
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.ROLE)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopRole role) throws Exception {

		String datatime = Util.getNowDateTimeString();
		role.setCreateDate(datatime);
		role.setModifyDate(datatime);

		try {
			if (roleService.exists("name", role.getName())) {
				return ajaxJsonErrorMessage("角色名已存在，请重新设置");
			}
			roleService.save(role);
			actionlog("新建角色", role.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ROLE)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopRole role = roleService.get(id);
			model.addAttribute("role", role);
			model.addAttribute("authority", roleService.getRoleAuthorities());
		}
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.ROLE)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopRole role) throws Exception {

		role.setModifyDate(Util.getNowDateTimeString());

		try {
			TbShopRole poRole = roleService.get(role.getId());
			if (!roleService.isUnique("name", poRole.getName(), role.getName())) {
				return ajaxJsonErrorMessage("角色名已存在，请重新设置");
			}
			roleService.update(role);
			actionlog("编辑角色", role.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ROLE)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}
		roleService.delete(ids.split(","));
		actionlog("删除角色", ids);
		return ajaxJsonSuccessMessage("操作成功");
	}

	@CheckAuthority(AppConstants.ADMIN)
	@ResponseBody
	@RequestMapping(value = "/getRolesForSelect")
	public String getRolesForSelect() throws Exception {
		List<TbShopRole> list = roleService.findAll();
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		for (Iterator<TbShopRole> iterator = list.iterator(); iterator.hasNext();) {
			TbShopRole role = (TbShopRole) iterator.next();
			Map<String, String> map = new HashMap<String, String>();
			map.put("text", role.getName());
			map.put("value", role.getId());
			retList.add(map);
		}
		return JacksonUtil.toJsonString(retList);
	}

}
