package com.homlin.module.shop.controller.admin;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.app.exception.MessageException;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopArticleCategory;
import com.homlin.module.shop.service.ArticleCategoryService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Util;

@Controller("adminArticleCategoryController")
@RequestMapping("/admin/article_category")
public class ArticleCategoryController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/article_category";

	@Autowired
	ArticleCategoryService articleCategoryService;

	@CheckAuthority(AppConstants.ARTICLE_CATEGORY)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority({ AppConstants.ARTICLE_CATEGORY, AppConstants.ARTICLE })
	@ResponseBody
	@RequestMapping(value = "/getArticleCatagoryTreeData")
	public String getTreeData(@RequestParam(value = "id", defaultValue = "") String pid) throws Exception {
		List<Map<String, Object>> list = articleCategoryService.getArticleCategoryTreeData(pid);
		return JacksonUtil.toJsonString(list);
	}

	@CheckAuthority(AppConstants.ARTICLE_CATEGORY)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) throws Exception {
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.ARTICLE_CATEGORY)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopArticleCategory articleCategory) throws Exception {
		if (StringUtils.isBlank(articleCategory.getTbShopArticleCategory().getId())) {
			articleCategory.setTbShopArticleCategory(null);
		}
		String datetime = Util.getNowDateTimeString();
		articleCategory.setCreateDate(datetime);
		articleCategory.setModifyDate(datetime);
		try {
			if (articleCategoryService.exists("code", articleCategory.getCode())) {
				return ajaxJsonErrorMessage("分类编码已存在");
			}
			articleCategoryService.save(articleCategory);
			actionlog("新建文章分类", articleCategory.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ARTICLE_CATEGORY)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		}
		TbShopArticleCategory articleCategory = articleCategoryService.get(id);
		model.addAttribute("model", articleCategory);
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.ARTICLE_CATEGORY)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopArticleCategory articleCategory) throws Exception {
		if (articleCategory.getId().equals(articleCategory.getTbShopArticleCategory().getId())) {
			return ajaxJsonErrorMessage("上级分类不能是自己");
		}
		if (StringUtils.isBlank(articleCategory.getTbShopArticleCategory().getId())) {
			articleCategory.setTbShopArticleCategory(null);
		}
		articleCategory.setModifyDate(Util.getNowDateTimeString());
		try {
			// TbShopArticleCategory poArticleCategory = articleCategoryService.get(articleCategory.getId());
			// if (!articleCategoryService.isUnique("code", poArticleCategory.getCode(), articleCategory.getCode())) {
			// return ajaxJsonErrorMessage("分类编码已存在");
			// }
			articleCategoryService.update(articleCategory);
			actionlog("编辑文章分类", articleCategory.getName());
		} catch (MessageException e) {
			return ajaxJsonErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ARTICLE_CATEGORY)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			articleCategoryService.delete(ids.split(","));
			actionlog("删除文章分类", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

}
