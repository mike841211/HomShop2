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
import com.homlin.module.shop.model.TbWxPayfeedback;
import com.homlin.module.shop.model.TbWxPayfeedback.FeedBackStatus;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.service.WxPayfeedbackService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;

@Controller
@RequestMapping("/admin/weixin/payfeedback")
public class WxPayfeedbackController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/weixin/payfeedback";

	@Autowired
	WxPayfeedbackService wxPayfeedbackService;

	@CheckAuthority(AppConstants.WEIXIN_PAYFEEDBACK)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.WEIXIN_PAYFEEDBACK)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("feedBackId");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = wxPayfeedbackService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.WEIXIN_PAYFEEDBACK)
	@ResponseBody
	@RequestMapping(value = "/updatePayfeedback", method = RequestMethod.POST)
	public String updatePayfeedback(
			@RequestParam(value = "feedbackids[]", defaultValue = "") String feedbackids[],
			@RequestParam(value = "openids[]", defaultValue = "") String openids[]
			) throws Exception {

		for (int i = 0; i < feedbackids.length; i++) {
			String feedbackid = feedbackids[i];
			String openid = openids[i];
			if (StringUtils.isBlank(feedbackid) || StringUtils.isBlank(openid)) {
				continue;
			}
			try {
				WeixinUtil.updatePayfeedback(feedbackid, openid);
				TbWxPayfeedback payfeedback = wxPayfeedbackService.get(feedbackid);
				payfeedback.setStatus(FeedBackStatus.user_unconfirmed);
				wxPayfeedbackService.update(payfeedback);
			} catch (Exception e) {
				return ajaxJsonErrorMessage(e.getMessage());
			}
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.WEIXIN_PAYFEEDBACK)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			wxPayfeedbackService.delete(ids.split(","));
			actionlog("删除微信维权申请", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

}
