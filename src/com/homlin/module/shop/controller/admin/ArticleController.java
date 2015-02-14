package com.homlin.module.shop.controller.admin;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopArticle;
import com.homlin.module.shop.service.ArticleService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller("adminArticleController")
@RequestMapping("/admin/article")
public class ArticleController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/article";

	@Autowired
	ArticleService articleService;

	@CheckAuthority(AppConstants.ARTICLE)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.ARTICLE)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("categoryId");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = articleService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.ARTICLE)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) throws Exception {
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.ARTICLE)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopArticle article) throws Exception {
		if (StringUtils.isBlank(article.getTbShopArticleCategory().getId())) {
			article.setTbShopArticleCategory(null);
		}
		String datetime = Util.getNowDateTimeString();
		article.setCreateDate(datetime);
		article.setModifyDate(datetime);
		article.setHits(0);
		try {
			articleService.save(article);
			actionlog("新建文章", article.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ARTICLE)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopArticle article = articleService.get(id);
			model.addAttribute("article", article);
		}
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.ARTICLE)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopArticle article) throws Exception {
		article.setModifyDate(Util.getNowDateTimeString());
		TbShopArticle poArticle = articleService.get(article.getId());
		BeanUtils.copyProperties(article, poArticle, new String[] { "id", "createDate", "hits" });
		try {
			articleService.update(poArticle);
			actionlog("编辑文章", poArticle.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ARTICLE)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			articleService.delete(ids.split(","));
			actionlog("删除文章", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

	@CheckAuthority(AppConstants.ARTICLE)
	@ResponseBody
	@RequestMapping(value = "/setIsShow", method = RequestMethod.POST)
	public String setIsShow(@RequestParam(value = "ids", defaultValue = "") String ids,
			@RequestParam(value = "status") String status) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}
		if (!AppConstants.TRUE.equals(status)) {
			status = AppConstants.FALSE;
		}

		try {
			articleService.setIsShow(ids, status);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

}
