package com.homlin.module.shop.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopNavigation;
import com.homlin.module.shop.service.NavigationService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Util;

@Controller
@RequestMapping("/admin/navigation")
public class NavigationController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/navigation";

	@Autowired
	NavigationService navigationService;

	@CheckAuthority(AppConstants.NAVIGATION)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.NAVIGATION)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist() throws Exception {
		List<TbShopNavigation> list = navigationService.getAll();
		return JacksonUtil.toJsonString(list);
	}

	@CheckAuthority(AppConstants.NAVIGATION)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() throws Exception {
		return RETURN_PATH + "/add";
	}

	@ResponseBody
	@CheckAuthority(AppConstants.NAVIGATION)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopNavigation navigation) throws Exception {
		String datetime = Util.getNowDateTimeString();
		navigation.setCreateDate(datetime);
		navigation.setModifyDate(datetime);

		try {
			navigationService.save(navigation);
			actionlog("新建导航菜单", navigation.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.NAVIGATION)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopNavigation navigation = navigationService.get(id);
			model.addAttribute("model", navigation);
		}
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.NAVIGATION)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopNavigation navigation) throws Exception {
		navigation.setModifyDate(Util.getNowDateTimeString());

		try {
			navigationService.update(navigation);
			actionlog("编辑导航菜单", navigation.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.NAVIGATION)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			navigationService.delete(ids.split(","));
			actionlog("删除导航菜单", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.NAVIGATION)
	@ResponseBody
	@RequestMapping(value = "/setIsShow", method = RequestMethod.POST)
	public String setIsShow(@RequestParam(value = "ids[]", defaultValue = "") String ids[],
			@RequestParam(value = "isShow") String isShow) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}
		if (!AppConstants.TRUE.equals(isShow)) {
			isShow = AppConstants.FALSE;
		}

		try {
			navigationService.setIsShow(ids, isShow);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

}
