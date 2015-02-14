package com.homlin.module.shop.controller.admin.weixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.app.exception.MessageException;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.controller.admin.BaseAdminController;
import com.homlin.module.shop.model.TbWxMenu;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.service.WxMenuService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Util;

@Controller
@RequestMapping("/admin/weixin/menu")
public class WxMenuController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/weixin/menu";

	@Autowired
	WxMenuService wxMenuService;

	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@ResponseBody
	@RequestMapping(value = "/getTreeData")
	public String getTreeData(@RequestParam(value = "id", defaultValue = "") String pid) throws Exception {
		return JacksonUtil.toJsonString(wxMenuService.getTreeData(pid));
	}

	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) throws Exception {
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbWxMenu wxMenu) throws Exception {
		if (StringUtils.isBlank(wxMenu.getTbWxMenu().getId())) {
			wxMenu.setTbWxMenu(null);
		}
		String datetime = Util.getNowDateTimeString();
		wxMenu.setCreateDate(datetime);
		wxMenu.setModifyDate(datetime);

		try {
			wxMenuService.save(wxMenu);
			actionlog("新建微信菜单", wxMenu.getName());
		} catch (MessageException e) {
			return ajaxJsonErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		}
		TbWxMenu wxMenu = wxMenuService.get(id);
		model.addAttribute("model", wxMenu);
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbWxMenu wxMenu) throws Exception {
		if (wxMenu.getId().equals(wxMenu.getTbWxMenu().getId())) {
			return ajaxJsonErrorMessage("上级分类不能是自己");
		}
		if (StringUtils.isBlank(wxMenu.getTbWxMenu().getId())) {
			wxMenu.setTbWxMenu(null);
		}
		wxMenu.setModifyDate(Util.getNowDateTimeString());

		try {
			wxMenuService.update(wxMenu);
			actionlog("编辑微信菜单", wxMenu.getName());
		} catch (MessageException e) {
			return ajaxJsonErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			wxMenuService.delete(ids.split(","));
			actionlog("删除微信菜单", ids);
		} catch (DataIntegrityViolationException e) {
			return ajaxJsonErrorMessage("删除失败：当前记录正被使用！");
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@ResponseBody
	@RequestMapping(value = "/updateMenuToServer", method = RequestMethod.POST)
	public String updateMenuToServer() throws Exception {

		// 准备更新数据
		List<TbWxMenu> menus = wxMenuService.getSynchrodMenus();
		if (menus == null || menus.size() < 1) {
			return ajaxJsonErrorMessage("读取菜单数据错误！");
		}

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (TbWxMenu tbWxMenu1 : menus) {
			Map<String, Object> menu1 = new HashMap<String, Object>();
			menu1.put("name", tbWxMenu1.getName());
			Set<TbWxMenu> subMenus = tbWxMenu1.getTbWxMenus();
			if (subMenus.size() > 0) {
				List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				int j = 0;
				for (TbWxMenu tbWxMenu2 : subMenus) {
					if (!AppConstants.TRUE.equals(tbWxMenu2.getInuse())) {
						continue;
					}
					Map<String, Object> menu2 = new HashMap<String, Object>();
					menu2.put("name", tbWxMenu2.getName());
					menu2.put("type", tbWxMenu2.getMenuType());
					if ("view".equalsIgnoreCase(tbWxMenu2.getMenuType())) {
						menu2.put("url", tbWxMenu2.getUrl());
					} else if ("click".equalsIgnoreCase(tbWxMenu2.getMenuType())) {
						menu2.put("key", tbWxMenu2.getId());
					}
					list2.add(menu2);
					j++;
					if (j == 5) {
						break;
					}
				}
				menu1.put("sub_button", list2);
			} else {
				menu1.put("type", tbWxMenu1.getMenuType());
				if ("view".equalsIgnoreCase(tbWxMenu1.getMenuType())) {
					menu1.put("url", tbWxMenu1.getUrl());
				} else if ("click".equalsIgnoreCase(tbWxMenu1.getMenuType())) {
					menu1.put("key", tbWxMenu1.getId());
				}
			}
			list1.add(menu1);
			i++;
			if (i == 3) {
				break;
			}
		}
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("button", list1);

		String data = JacksonUtil.toJsonString(button);
		// Util.copyToClipboard(data);

		// 提交更新
		try {
			WeixinUtil.createMenu(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("更新失败：" + e.getMessage());
		}

		actionlog("更新微信菜单", data);
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@ResponseBody
	@RequestMapping(value = "/getMenuFromServer")
	public String getMenuFromServer() throws Exception {
		try {
			return WeixinUtil.getMenu();
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("读取菜单失败：" + e.getMessage());
		}
	}

}
