package com.homlin.module.shop.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.homlin.app.SpringContextHolder;
import com.homlin.module.shop.model.TbShopArticle;
import com.homlin.module.shop.model.TbShopArticleCategory;
import com.homlin.module.shop.model.TbShopBrand;
import com.homlin.module.shop.model.TbShopProductCategory;
import com.homlin.module.shop.service.ArticleCategoryService;
import com.homlin.module.shop.service.ArticleService;
import com.homlin.module.shop.service.BrandService;
import com.homlin.module.shop.service.ProductCategoryService;
import com.homlin.utils.Util;

@Component
public class ElUtil {

	// === 辅助工具 ===

	/**
	 * 是否微信浏览器
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isWeixinBrowser(HttpServletRequest request) {
		return Util.isWeixinBrowser(request);
	}

	/**
	 * 是否支持微信支付，仅限微信浏览器5.0及以上版本
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isWxpayJsApiSupported(HttpServletRequest request) {
		return Util.isWxpayJsApiSupported(request);
	}

	/**
	 * URL编码
	 * 
	 * @param string
	 * @return URLEncoder.encode(string, "UTF-8")
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeURL(String string) throws UnsupportedEncodingException {
		return URLEncoder.encode(string, "UTF-8");
	}

	// public static String concat(String string1, String string2) {
	// return string1 + string2;
	// }

	// === 读取数据 ===

	/**
	 * 读取指定类别子分类
	 * 
	 * @param category_id
	 *            指定类别ID
	 * @param siblings
	 *            如果没有子类别是否读取同级类别
	 * @return
	 */
	public static List<TbShopProductCategory> getSubCategories(String category_id, boolean siblings) {
		ProductCategoryService productCategoryService = SpringContextHolder.getBean(ProductCategoryService.class);
		return productCategoryService.getSubCategories(category_id, siblings);
	}

	public static List<TbShopBrand> getBrands(String category_id) {
		BrandService brandService = SpringContextHolder.getBean(BrandService.class);
		List<TbShopBrand> list = brandService.getByCateId(category_id);
		return list;
	}

	public static List<TbShopArticleCategory> getSubArticleCategories(String category_id, boolean siblings) {
		ArticleCategoryService articleCategoryService = SpringContextHolder.getBean(ArticleCategoryService.class);
		return articleCategoryService.getSubArticleCategories(category_id, siblings);
	}

	public static List<TbShopArticle> getNextArticles(String article_id, int num) {
		ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
		return articleService.getNextArticles(article_id, num);
	}

	public static List<TbShopArticle> getArticlesByCateCode(String code, int num) {
		Object category_id = CacheUtil.getArticleCategoryIdByCode(code);
		if (category_id == null) {
			return null;
		}
		ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
		return articleService.getArticlesByCate(category_id.toString(), num);
	}

}
