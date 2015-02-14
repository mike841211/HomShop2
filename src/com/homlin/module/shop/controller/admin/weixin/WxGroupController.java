package com.homlin.module.shop.controller.admin.weixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.controller.admin.BaseAdminController;
import com.homlin.module.shop.model.TbWxGroup;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.service.WxGroupService;
import com.homlin.utils.JacksonUtil;

@Controller
@RequestMapping("/admin/weixin/group")
public class WxGroupController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/weixin/group";

	@Autowired
	WxGroupService wxGroupService;

	@CheckAuthority(AppConstants.WEIXIN_GROUP)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.WEIXIN_GROUP)
	@ResponseBody
	@RequestMapping(value = "/datalist")
	public String datalist() throws Exception {
		return JacksonUtil.toJsonString(wxGroupService.findAll());
	}

	@SuppressWarnings("unchecked")
	@CheckAuthority(AppConstants.WEIXIN_MENU)
	@ResponseBody
	@RequestMapping(value = "/loadGroups", method = RequestMethod.POST)
	public String loadGroups() throws Exception {
		String string = null;
		try {
			string = WeixinUtil.getGroups();
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("更新失败：" + e.getMessage());
		}
		Map<String, Object> json = JacksonUtil.toObject(string, Map.class);
		if (json.get("groups") != null) {
			List<TbWxGroup> groups = new ArrayList<TbWxGroup>();
			List<Map<String, Object>> list = (List<Map<String, Object>>) json.get("groups");
			for (Map<String, Object> mapGroup : list) {
				TbWxGroup group = new TbWxGroup();
				BeanUtils.copyProperties(group, mapGroup);
				groups.add(group);
			}
			wxGroupService.merge(groups);
		}
		return ajaxJsonSuccessMessage();
	}

	@SuppressWarnings("unchecked")
	@CheckAuthority(AppConstants.WEIXIN_GROUP)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String name) throws Exception {
		if (StringUtils.isBlank(name)) {
			return ajaxJsonErrorMessage("名称不能为空");
		}

		String data = "{\"group\":{\"name\":\"" + name + "\"}}";
		String result = null;
		try {
			result = WeixinUtil.createGroups(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("更新失败：" + e.getMessage());
		}
		Map<String, Object> map = JacksonUtil.toObject(result, Map.class);
		Map<String, Object> mapGroup = (Map<String, Object>) map.get("group");
		TbWxGroup group = new TbWxGroup();
		BeanUtils.copyProperties(group, mapGroup);
		group.setCount(0);
		wxGroupService.save(group);

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.WEIXIN_GROUP)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Integer id, String name) throws Exception {
		if (StringUtils.isBlank(name)) {
			return ajaxJsonErrorMessage("名称不能为空");
		}

		String data = "{\"group\":{\"id\":\"" + id + "\",\"name\":\"" + name + "\"}}";
		try {
			WeixinUtil.updateGroups(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("更新失败：" + e.getMessage());
		}
		TbWxGroup group = wxGroupService.get(id);
		group.setName(name);
		wxGroupService.update(group);

		return ajaxJsonSuccessMessage();
	}
}
