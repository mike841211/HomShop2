package com.homlin.module.shop.controller.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homlin.module.shop.model.TbShopAdvert;
import com.homlin.module.shop.service.AdvertService;

/**
 * 自定义版块，内容显示，页面客户自己设计
 * 
 * @author Administrator
 * 
 */
@Controller
public class PageController extends BaseShopController {

	@Autowired
	AdvertService advertService;

	@RequestMapping(value = "/page/{keyword}")
	public String page(@PathVariable String keyword, Model model) throws Exception {
		List<TbShopAdvert> list = advertService.getByKeyword(keyword);
		if (list.size() == 0) {
			addActionError("页面没有找到！");
			// response.sendError(404);
			// response.setStatus(404);
			return PAGE_ERROR;
		}
		model.addAttribute("page", list.get(0));
		return getTemplatePath() + "/page";
	}
}
