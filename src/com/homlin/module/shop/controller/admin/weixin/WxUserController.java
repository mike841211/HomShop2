package com.homlin.module.shop.controller.admin.weixin;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.controller.admin.BaseAdminController;
import com.homlin.module.shop.model.TbWxUser;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.service.WxUserService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;

@Controller
@RequestMapping("/admin/weixin/user")
public class WxUserController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/weixin/user";

	@Autowired
	WxUserService wxUserService;

	@CheckAuthority(AppConstants.WEIXIN_USER)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.WEIXIN_USER)
	@ResponseBody
	@RequestMapping(value = "/datalist")
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("group_id", "nickname", "sex", "country", "province", "city");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = wxUserService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.WEIXIN_USER)
	@ResponseBody
	@RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
	public String updateGroup(@RequestParam(value = "ids[]", defaultValue = "") String ids[], Integer groupId) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}
		if ("".equals(groupId)) {
			return ajaxJsonErrorMessage("groupId参数错误");
		}

		try {
			for (String id : ids) {
				String data = "{\"openid\":\"" + id + "\",\"to_groupid\":" + groupId + "}";
				WeixinUtil.updateUserGroups(data);
				wxUserService.setValue(ids, "groupId", groupId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@SuppressWarnings("unchecked")
	@CheckAuthority(AppConstants.WEIXIN_USER)
	@ResponseBody
	@RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
	public String updateInfo(@RequestParam(value = "ids[]", defaultValue = "") String ids[]) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			for (String id : ids) {
				String result = WeixinUtil.getUserInfo(id);
				TbWxUser user = JacksonUtil.toObject(result, TbWxUser.class);
				Map<String, Object> map = JacksonUtil.toObject(result, Map.class);
				user.setSubscribeTime(Integer.valueOf(map.get("subscribe_time").toString()));
				// 查询用户组
				String data = "{\"openid\":\"" + id + "\"}";
				String result2 = WeixinUtil.getUserGroupId(data);
				Map<String, Integer> map2 = JacksonUtil.toObject(result2, Map.class);
				user.setGroupId(map2.get("groupid"));
				wxUserService.update(user);
			}
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.WEIXIN_USER)
	@ResponseBody
	@RequestMapping(value = "/sendCustomMessage", method = RequestMethod.POST)
	public String sendCustomMessage(String content, @RequestParam(value = "ids[]", defaultValue = "") String ids[]) throws Exception {
		if (StringUtils.isBlank(content)) {
			return ajaxJsonErrorMessage("消息不能为空");
		}

		try {
			for (String id : ids) {
				if (StringUtils.isEmpty(id)) {
					continue;
				}
				String data = "{\"touser\":\"" + id + "\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + content + "\"}}";
				WeixinUtil.sendCustomMessage(data);
			}
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	// 载入全部用户
	@CheckAuthority(AppConstants.WEIXIN_USER)
	@ResponseBody
	@RequestMapping(value = "/loadUsers", method = RequestMethod.POST)
	public String loadUsers() throws Exception {

		final String THREAD_NAME = "WEIXIN_LOAD_USERS";

		// 是否已有同步任务正在执行
		boolean hasThread = false;
		Map<Thread, StackTraceElement[]> maps = Thread.getAllStackTraces();
		for (Thread thread : maps.keySet()) {
			if (THREAD_NAME.equals(thread.getName())) {
				hasThread = true;
				break;
			}
		}

		if (hasThread) {
			return ajaxJsonErrorMessage("系统当前正在同步数据，请稍候再试...");
		}

		new Thread(THREAD_NAME) {
			private String next_openid = "";

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					String string = WeixinUtil.getUsers(next_openid);
					Map<String, Object> json = JacksonUtil.toObject(string, Map.class);
					if (Integer.valueOf(json.get("count").toString()) > 0) {
						List<String> openids = (List<String>) ((Map<String, Object>) json.get("data")).get("openid");
						for (String openid : openids) {
							try {
								// 账号信息
								String result = WeixinUtil.getUserInfo(openid);
								TbWxUser user = JacksonUtil.toObject(result, TbWxUser.class);
								Map<String, Object> map = JacksonUtil.toObject(result, Map.class);
								user.setSubscribeTime(Integer.valueOf(map.get("subscribe_time").toString()));
								// 查询用户组
								String data = "{\"openid\":\"" + openid + "\"}";
								String result2 = WeixinUtil.getUserGroupId(data);
								Map<String, Integer> map2 = JacksonUtil.toObject(result2, Map.class);
								user.setGroupId(map2.get("groupid"));
								wxUserService.update(user);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						next_openid = json.get("next_openid").toString();
						if (StringUtils.isNotEmpty(next_openid)) {
							run();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		return ajaxJsonSuccessMessage();
	}

}
