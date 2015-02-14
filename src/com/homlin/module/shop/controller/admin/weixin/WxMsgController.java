package com.homlin.module.shop.controller.admin.weixin;

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
import com.homlin.module.shop.model.TbWxMsg;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.service.WxMsgService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;

@Controller
@RequestMapping("/admin/weixin/msg")
public class WxMsgController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/weixin/msg";

	@Autowired
	WxMsgService wxMsgService;

	@CheckAuthority(AppConstants.WEIXIN_MSG)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.WEIXIN_MSG)
	@ResponseBody
	@RequestMapping(value = "/datalist")
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("nickname", "content");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = wxMsgService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.WEIXIN_MSG)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids[]", required = true, defaultValue = "") String ids[]) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			wxMsgService.delete(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("操作失败");
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

	@CheckAuthority(AppConstants.WEIXIN_MSG)
	@ResponseBody
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(String msgId, String fromUserName, String lastreply) throws Exception {
		if (StringUtils.isBlank(msgId) || StringUtils.isBlank(fromUserName)) {
			return ajaxJsonErrorMessage("参数错误");
		}
		if (StringUtils.isBlank(lastreply)) {
			return ajaxJsonErrorMessage("消息不能为空");
		}

		try {
			String data = "{\"touser\":\"" + fromUserName + "\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + lastreply + "\"}}";
			WeixinUtil.sendCustomMessage(data);
			TbWxMsg tbWxMsg = wxMsgService.get(msgId);
			tbWxMsg.setLastreply(lastreply);
			wxMsgService.update(tbWxMsg);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

}
