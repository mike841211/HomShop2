package com.homlin.module.shop.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.service.ActionlogService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;

@Controller
@RequestMapping("/admin/actionlog")
public class ActionlogController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/actionlog";

	@Autowired
	ActionlogService actionlogService;

	@CheckAuthority(AppConstants.ACTIONLOG)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.ACTIONLOG)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("createDateBegin", "createDateEnd");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = actionlogService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.ACTIONLOG)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			actionlogService.delete(ids.split(","));
			actionlog("删除操作日志", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("操作失败");
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

}
