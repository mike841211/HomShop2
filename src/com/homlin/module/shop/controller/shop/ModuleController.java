package com.homlin.module.shop.controller.shop;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.homlin.module.shop.model.TbShopArticle;
import com.homlin.module.shop.service.ArticleService;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.Pager;

@Controller
@RequestMapping(value = "/module")
public class ModuleController extends BaseShopController {

	@Autowired
	ArticleService articleService;

	@RequestMapping(value = "/{path}")
	public String module(@PathVariable String path) {
		return getTemplatePath("/" + path);
	}

	private Pager getPagedArticles(int pageIndex, int pageSize, String cate_code) {

		Map<String, Object> params = getAllQueryParams();
		params.put("isShow", "1");
		params.put("basecid", CacheUtil.getArticleCategoryIdByCode(cate_code)); // 仅读取当前栏目下内容

		Pager pager = new Pager(pageIndex, pageSize, params);
		pager = articleService.getPagedList(pager);

		return pager;
	}

	@RequestMapping(value = "/news/list")
	public String module_news_list(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
			@RequestParam(value = "pagesize", defaultValue = "10") int pageSize, Model model) {

		Pager pager = getPagedArticles(pageIndex, pageSize, "news");
		model.addAttribute("pager", pager);

		return getTemplatePath("/news/list");
	}

	@RequestMapping(value = "/news/view/{id}")
	public String module_news_view(@PathVariable String id, Model model) {
		TbShopArticle article = articleService.get(id);
		model.addAttribute("article", article);
		return getTemplatePath("/news/view");
	}

	@RequestMapping(value = "/case/list")
	public String module_case(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
			@RequestParam(value = "pagesize", defaultValue = "10") int pageSize, Model model) {

		Pager pager = getPagedArticles(pageIndex, pageSize, "case");
		model.addAttribute("pager", pager);

		return getTemplatePath("/case/list");
	}

	@RequestMapping(value = "/case/view/{id}")
	public String module_case_view(@PathVariable String id, Model model) {
		TbShopArticle article = articleService.get(id);
		model.addAttribute("article", article);
		return getTemplatePath("/case/view");
	}

}
