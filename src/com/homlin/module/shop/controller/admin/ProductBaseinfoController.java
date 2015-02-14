package com.homlin.module.shop.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopProductBaseinfo;
import com.homlin.module.shop.service.ProductBaseinfoService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller
@RequestMapping("/admin/product_baseinfo")
public class ProductBaseinfoController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/product_baseinfo";

	@Autowired
	ProductBaseinfoService productBaseinfoService;

	@CheckAuthority(AppConstants.PRODUCT)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("title");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = productBaseinfoService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) throws Exception {
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopProductBaseinfo productBaseinfo) throws Exception {
		productBaseinfo.setCreateDate(Util.getNowDateTimeString());
		productBaseinfo.setModifyDate(Util.getNowDateTimeString());
		try {
			productBaseinfoService.save(productBaseinfo);
			actionlog("新建商品描述模板", productBaseinfo.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopProductBaseinfo productBaseinfo = productBaseinfoService.get(id);
			model.addAttribute("productBaseinfo", productBaseinfo);
		}
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopProductBaseinfo productBaseinfo) throws Exception {
		productBaseinfo.setModifyDate(Util.getNowDateTimeString());
		try {
			productBaseinfoService.update(productBaseinfo);
			actionlog("编辑商品描述模板", productBaseinfo.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			productBaseinfoService.delete(ids.split(","));
			actionlog("删除商品描述模板", ids);
		} catch (DataIntegrityViolationException e) {
			return ajaxJsonErrorMessage("删除失败：当前模板正被使用！");
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/getAllForSelect")
	public String getAllForSlt() throws Exception {
		return JacksonUtil.toJsonString(productBaseinfoService.getAllForSelect());
	}

}
