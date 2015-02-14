package com.homlin.module.shop.controller.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.AppConstants;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.controller.shop.member.BaseMemberController;
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.module.shop.model.TbShopProductBaseinfo;
import com.homlin.module.shop.model.TbShopSku;
import com.homlin.module.shop.model.TbShopSpecification;
import com.homlin.module.shop.model.TbShopSpecificationValue;
import com.homlin.module.shop.service.ProductBaseinfoService;
import com.homlin.module.shop.service.ProductCommentService;
import com.homlin.module.shop.service.ProductService;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Pager;

@Controller
@RequestMapping({ "/product" })
public class ProductController extends BaseMemberController {

	@Autowired
	ProductService productService;

	@Autowired
	ProductBaseinfoService productBaseinfoService;

	@Autowired
	ProductCommentService productCommentService;

	private Pager getPager(Integer pageIndex, Integer pageSize) {
		Map<String, Object> params = getAllQueryParams();
		params.put("isSale", "1");
		if (!AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.SHOP_SHOWOUTOFSTOCK))) {
			params.put("hasStock", "1");
		}

		// [[ 价格 ]]
		String key;
		key = "price";// 价格范围：100,199
		if (params.containsKey(key)) {
			String value[] = (params.get(key).toString() + ",").split(",");
			params.put("minprice", value[0]);
			params.put("maxprice", value[1]);
			params.remove(key);
		}
		key = "minprice";// 最低价
		if (params.containsKey(key)) {
			String value = params.get(key).toString();
			Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
			if (!pattern.matcher(value).matches()) {
				params.remove(key);
			}
		}
		key = "maxprice"; // 最高价
		if (params.containsKey(key)) {
			String value = params.get(key).toString();
			Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
			if (!pattern.matcher(value).matches()) {
				params.remove(key);
			}
		}
		// ]] 价格 ]]

		Pager pager = new Pager(pageIndex, pageSize, params);
		pager = productService.getPagedList(pager);
		return pager;
	}

	@RequestMapping({ "/list" })
	public String list() {
		String prodlist = CacheUtil.getConfig(CacheConfigKeys.TEMPLATE_MOBILE_PRODLIST);
		if (StringUtils.isBlank(prodlist)) {
			prodlist = "list";
		}
		return getTemplatePath() + "/product/" + prodlist;
	}

	@RequestMapping("/search")
	public String search() {
		return getTemplatePath() + "/product/list";
	}

	@RequestMapping({ "/swiperlist" })
	public String swiperlist() {
		return getTemplatePath() + "/product/list_swiper";
	}

	@ResponseBody
	@RequestMapping(value = "/list/json", method = RequestMethod.POST)
	public String list_json(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
			@RequestParam(value = "pagesize", defaultValue = "10") int pageSize) throws Exception {

		Pager pager = getPager(pageIndex, pageSize);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(STATUS, SUCCESS);
		map.put("pageCount", pager.getPageCount());
		map.put("pageIndex", pager.getPageIndex());
		map.put("pageSize", pager.getPageSize());
		map.put("totalCount", pager.getTotalCount());
		map.put("data", pager.getDataList());
		return JacksonUtil.toJsonString(map);

	}

	@RequestMapping({ "/item/{id}" })
	public String item(@PathVariable String id, Model model) throws Exception {

		TbShopProduct product = productService.get(id);

		String errorMessage = null;
		if (null == product) {
			errorMessage = "当前商品已删除！";
		} else if (!AppConstants.TRUE.equals(product.getIsSale())) {
			errorMessage = "当前商品已下架！";
		} else if (product.getStock() <= 0 && !AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.SHOP_SHOWOUTOFSTOCK))) {
			errorMessage = "当前商品已下架，库存不足！";
		}
		if (errorMessage != null) {
			addActionError(errorMessage);
			return getTemplatePath() + "/error";
		}

		Set<TbShopSku> skus = product.getTbShopSkus();

		Map<String, Object> specificationValues = new HashMap<String, Object>(); // 有sku的规格值集合

		List<TbShopSpecification> specifications = product.getTbShopSpecifications();
		for (TbShopSpecification specification : specifications) {
			ArrayList<TbShopSpecificationValue> _values = new ArrayList<TbShopSpecificationValue>(); // temp
			List<TbShopSpecificationValue> values = specification.getTbShopSpecificationValues();
			for (TbShopSpecificationValue value : values) { // 遍历规格值是否有SKU使用
				for (TbShopSku sku : skus) {
					boolean show = true;
					if (sku.getStock() <= 0 && !AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.SHOP_SHOWOUTOFSTOCK))) {
						show = false;
					}
					if (show) {
						if (sku.getSpecificationJson().indexOf(value.getId()) > -1) { // 是否有包含此规格的SKU
							_values.add(value);
							break;
						}
					}
				}

			}
			specificationValues.put(specification.getId(), _values);
		}

		model.addAttribute("specificationValues", specificationValues);
		model.addAttribute("product", product);
		return getTemplatePath() + "/product/item";
	}

	@ResponseBody
	@RequestMapping({ "/item_introduction" })
	public String item_introduction(String id, Model model) throws Exception {

		TbShopProduct product = productService.get(id);

		if (null == product) {
			return "当前商品已删除！";
		}

		String content = product.getIntroduction();
		TbShopProductBaseinfo baseinfo = product.getTbShopProductBaseinfo();
		// 无缓存
		// if (baseinfo != null && baseinfo.getContent() != null) {
		// content += baseinfo.getContent();
		// }
		// 有缓存
		if (baseinfo != null) {
			String baseinfoContent = productBaseinfoService.getContent(baseinfo.getId());
			content += baseinfoContent;
		}

		return content;
	}

	@ResponseBody
	@RequestMapping(value = "/comments/json")
	public String commons_json(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
			@RequestParam(value = "pagesize", defaultValue = "10") int pageSize, String product_id) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("product_id", product_id);
		params.put("isShow", "1");

		Pager pager = new Pager(pageIndex, pageSize, params);
		pager = productCommentService.getPagedList(pager);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(STATUS, SUCCESS);
		map.put("pageCount", pager.getPageCount());
		map.put("pageIndex", pager.getPageIndex());
		map.put("pageSize", pager.getPageSize());
		map.put("totalCount", pager.getTotalCount());
		map.put("data", pager.getDataList());
		return JacksonUtil.toJsonString(map);

	}

}
