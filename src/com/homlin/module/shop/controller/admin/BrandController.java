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
import com.homlin.module.shop.model.TbShopBrand;
import com.homlin.module.shop.service.BrandService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller("adminBrandController")
@RequestMapping("/admin/brand")
public class BrandController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/brand";

	@Autowired
	BrandService brandService;

	@CheckAuthority(AppConstants.BRAND)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.BRAND)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams();

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = brandService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.BRAND)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() throws Exception {
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.BRAND)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopBrand brand) throws Exception {
		if (brand.getDisplayorder() == null) {
			brand.setDisplayorder(0);
		}
		brand.setCreateDate(Util.getNowDateTimeString());
		brand.setModifyDate(Util.getNowDateTimeString());
		try {
			brandService.save(brand);
			actionlog("新建品牌", brand.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.BRAND)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopBrand brand = brandService.get(id);
			model.addAttribute("brand", brand);
		}
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.BRAND)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopBrand brand) throws Exception {
		if (brand.getDisplayorder() == null) {
			brand.setDisplayorder(0);
		}
		brand.setModifyDate(Util.getNowDateTimeString());
		try {
			brandService.update(brand);
			actionlog("编辑品牌", brand.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.BRAND)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			brandService.delete(ids.split(","));
			actionlog("删除品牌", ids);
		} catch (DataIntegrityViolationException e) {
			return ajaxJsonErrorMessage("删除失败：当前品牌正被使用！");
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority({ AppConstants.BRAND, AppConstants.PRODUCT })
	@ResponseBody
	@RequestMapping(value = "/getAllForSelect")
	public String getAllForSelect() throws Exception {
		return JacksonUtil.toJsonString(brandService.getAllForSelect());
	}

}
