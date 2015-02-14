package com.homlin.module.shop.controller.admin;

import java.util.List;
import java.util.Map;

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
import com.homlin.module.shop.model.TbShopBrand;
import com.homlin.module.shop.model.TbShopProductCategory;
import com.homlin.module.shop.service.BrandService;
import com.homlin.module.shop.service.ProductCategoryService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Util;

@Controller("adminProductCategoryController")
@RequestMapping("/admin/product_category")
public class ProductCategoryController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/product_category";

	@Autowired
	ProductCategoryService productCategoryService;

	@Autowired
	BrandService brandService;

	@CheckAuthority(AppConstants.PRODUCT_CATEGORY)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority({ AppConstants.PRODUCT_CATEGORY, AppConstants.ARTICLE })
	@ResponseBody
	@RequestMapping(value = "/getProductCategoryTreeData")
	public String getTreeData(@RequestParam(value = "id", defaultValue = "") String pid) throws Exception {
		List<Map<String, Object>> list = productCategoryService.getProductCatagoryTreeData(pid);
		return JacksonUtil.toJsonString(list);
	}

	@CheckAuthority(AppConstants.PRODUCT_CATEGORY)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) throws Exception {
		model.addAttribute("brands", brandService.findAll());
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.PRODUCT_CATEGORY)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopProductCategory productCategory, @RequestParam(value = "brands[]", defaultValue = "") String brands[])
			throws Exception {
		if (StringUtils.isBlank(productCategory.getTbShopProductCategory().getId())) {
			productCategory.setTbShopProductCategory(null);
		}
		productCategory.setCreateDate(Util.getNowDateTimeString());
		productCategory.setModifyDate(Util.getNowDateTimeString());

		for (String id : brands) {
			if (StringUtils.isNotBlank(id)) {
				productCategory.getTbShopBrands().add(new TbShopBrand(id));
			}
		}

		try {
			productCategoryService.save(productCategory);
			actionlog("新建商品分类", productCategory.getName());
		} catch (MessageException e) {
			return ajaxJsonErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.PRODUCT_CATEGORY)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		}
		TbShopProductCategory productCategory = productCategoryService.get(id);
		model.addAttribute("model", productCategory);
		model.addAttribute("brands", brandService.findAll());
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.PRODUCT_CATEGORY)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopProductCategory productCategory, @RequestParam(value = "brands[]", defaultValue = "") String brands[])
			throws Exception {
		if (productCategory.getId().equals(productCategory.getTbShopProductCategory().getId())) {
			return ajaxJsonErrorMessage("上级分类不能是自己");
		}
		if (StringUtils.isBlank(productCategory.getTbShopProductCategory().getId())) {
			productCategory.setTbShopProductCategory(null);
		}
		productCategory.setModifyDate(Util.getNowDateTimeString());

		productCategory.getTbShopBrands().clear();
		for (String id : brands) {
			if (StringUtils.isNotBlank(id)) {
				productCategory.getTbShopBrands().add(new TbShopBrand(id));
			}
		}

		try {
			productCategoryService.update(productCategory);
			actionlog("编辑商品分类", productCategory.getName());
		} catch (MessageException e) {
			return ajaxJsonErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.PRODUCT_CATEGORY)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			productCategoryService.delete(ids.split(","));
			actionlog("删除商品分类", ids);
		} catch (DataIntegrityViolationException e) {
			return ajaxJsonErrorMessage("删除失败：当前分类正被使用！");
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

}
