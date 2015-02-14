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
import com.homlin.module.shop.model.TbShopProductComment;
import com.homlin.module.shop.service.ProductCommentService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller("adminProductCommentController")
@RequestMapping("/admin/product_comment")
public class ProductCommentController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/product_comment";

	@Autowired
	ProductCommentService productCommentService;

	@CheckAuthority(AppConstants.PRODUCT_COMMENT)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.PRODUCT_COMMENT)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams();

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = productCommentService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.PRODUCT_COMMENT)
	@ResponseBody
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String update(String id, String replyContent) throws Exception {
		TbShopProductComment tbShopProductComment = productCommentService.get(id);
		tbShopProductComment.setReplyContent(replyContent);
		tbShopProductComment.setReplyDate(Util.getNowDateTimeString());
		try {
			productCommentService.update(tbShopProductComment);
			actionlog("回复商品评价", id);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.PRODUCT_COMMENT)
	@ResponseBody
	@RequestMapping(value = "/setShow", method = RequestMethod.POST)
	public String setShow(@RequestParam(value = "ids[]", defaultValue = "") String ids[],
			@RequestParam(value = "bool") String bool) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			productCommentService.setBoolean(ids, "isShow", bool);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.PRODUCT_COMMENT)
	@ResponseBody
	@RequestMapping(value = "/setDelete", method = RequestMethod.POST)
	public String setDelete(@RequestParam(value = "ids[]", defaultValue = "") String ids[]) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			productCommentService.setBoolean(ids, "isDelete", AppConstants.TRUE);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.PRODUCT_COMMENT)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			productCommentService.delete(ids.split(","));
			actionlog("删除商品评价", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

}
