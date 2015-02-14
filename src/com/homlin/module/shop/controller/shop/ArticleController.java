package com.homlin.module.shop.controller.shop;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.shop.model.TbShopArticle;
import com.homlin.module.shop.service.ArticleCategoryService;
import com.homlin.module.shop.service.ArticleService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Pager;

@Controller
public class ArticleController extends BaseShopController {

	@Autowired
	ArticleService articleService;

	@Autowired
	ArticleCategoryService articleCategoryService;

	@RequestMapping(value = "/article/list")
	public String list(Model model) {
		return getTemplatePath() + "/article/list";
	}

	@ResponseBody
	@RequestMapping(value = "/article/list/json", method = RequestMethod.POST)
	public String list_json(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
			@RequestParam(value = "pagesize", defaultValue = "10") int pageSize) throws Exception {

		Map<String, Object> params = getAllQueryParams();
		params.put("isShow", "1");

		Pager pager = new Pager(pageIndex, pageSize, params);
		pager = articleService.getPagedList(pager);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(STATUS, SUCCESS);
		map.put("pageCount", pager.getPageCount());
		map.put("pageIndex", pager.getPageIndex());
		map.put("pageSize", pager.getPageSize());
		map.put("totalCount", pager.getTotalCount());
		map.put("data", pager.getDataList());
		return JacksonUtil.toJsonString(map);

	}

	@RequestMapping(value = "/article/view/{id}")
	public String view(@PathVariable String id, Model model) {
		TbShopArticle article = articleService.get(id);
		model.addAttribute("article", article);
		return getTemplatePath() + "/article/view";
	}

	@ResponseBody
	@RequestMapping(value = "/article/hits/{id}")
	public void hits(@PathVariable String id) {
		articleService.updateHits(id);
	}

}
