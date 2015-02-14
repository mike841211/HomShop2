package com.homlin.module.shop.controller.shop;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.service.ProductCategoryService;
import com.homlin.module.shop.util.CacheUtil;

@Controller
public class ProductCategoryController extends BaseShopController {

	@Autowired
	ProductCategoryService productCategoryService;

	@RequestMapping(value = "/category/list")
	public String list(Model model) {
		// List<TbShopProductCategory> categories = productCategoryService.getFirstLeverCategories();
		// model.addAttribute("categories", categories);
		String catelist = CacheUtil.getConfig(CacheConfigKeys.TEMPLATE_MOBILE_CATELIST);
		if (StringUtils.isBlank(catelist)) {
			catelist = "list";
		}
		return getTemplatePath() + "/category/" + catelist;
	}
}
