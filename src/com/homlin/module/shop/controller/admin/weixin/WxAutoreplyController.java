package com.homlin.module.shop.controller.admin.weixin;

import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
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
import com.homlin.module.shop.model.TbWxAutoreply;
import com.homlin.module.shop.service.WxAutoreplyService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller
@RequestMapping("/admin/weixin/autoreply")
public class WxAutoreplyController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/weixin/autoreply";

	@Autowired
	WxAutoreplyService wxAutoreplyService;

	@CheckAuthority(AppConstants.WEIXIN_AUTOREPLY)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.WEIXIN_AUTOREPLY)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("menu_id");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = wxAutoreplyService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.WEIXIN_AUTOREPLY)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) throws Exception {
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.WEIXIN_AUTOREPLY)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbWxAutoreply autoreply) throws Exception {
		if (StringUtils.isBlank(autoreply.getKeyword())) {
			return ajaxJsonErrorMessage("没有设置关键字");
		}
		String datetime = Util.getNowDateTimeString();
		autoreply.setCreateDate(datetime);
		autoreply.setModifyDate(datetime);
		autoreply.setHits(0);

		try {
			wxAutoreplyService.save(autoreply);
			actionlog("新建微信自动回复", autoreply.getTitle());
		} catch (MessageException e) {
			return ajaxJsonErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.WEIXIN_AUTOREPLY)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		}
		TbWxAutoreply autoreply = wxAutoreplyService.get(id);
		model.addAttribute("model", autoreply);
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.WEIXIN_AUTOREPLY)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbWxAutoreply autoreply) throws Exception {
		if (StringUtils.isBlank(autoreply.getKeyword())) {
			return ajaxJsonErrorMessage("没有设置关键字");
		}
		autoreply.setModifyDate(Util.getNowDateTimeString());

		try {
			wxAutoreplyService.update(autoreply);
			actionlog("编辑微信自动回复", autoreply.getTitle());
		} catch (MessageException e) {
			return ajaxJsonErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.WEIXIN_AUTOREPLY)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			wxAutoreplyService.delete(ids.split(","));
			actionlog("删除微信自动回复", ids);
		} catch (DataIntegrityViolationException e) {
			return ajaxJsonErrorMessage("删除失败：当前记录正被使用！");
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

	@CheckAuthority(AppConstants.WEIXIN_AUTOREPLY)
	@ResponseBody
	@RequestMapping(value = "/setStatus", method = RequestMethod.POST)
	public String setStatus(@RequestParam(value = "ids[]", defaultValue = "") String ids[],
			@RequestParam(value = "field", defaultValue = "") String field,
			@RequestParam(value = "status") String value) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}
		String[] fields = new String[] { "isShow", "showCover" };
		if (!ArrayUtils.contains(fields, field)) {
			return ajaxJsonErrorMessage("field参数错误");
		}

		try {
			wxAutoreplyService.setValue(ids, field, value);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

}
