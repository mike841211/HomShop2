package com.homlin.module.shop.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
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
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.module.shop.model.TbShopSpecification;
import com.homlin.module.shop.service.ProductService;
import com.homlin.module.shop.service.SpecificationService;
import com.homlin.module.shop.util.SerialNumberUtil;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller("adminProductController")
@RequestMapping("/admin/product")
public class ProductController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/product";

	@Autowired
	ProductService productService;

	@Autowired
	SpecificationService specificationService;

	@CheckAuthority(AppConstants.PRODUCT)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("cid", "code", "name", "barcode");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = productService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) throws Exception {
		List<TbShopSpecification> specifications = specificationService.getAll();
		model.addAttribute("specifications", specifications);
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopProduct product, @RequestParam(value = "specifications[]", defaultValue = "") String specifications[])
			throws Exception {
		if (product.getTbShopBrand() != null && StringUtils.isBlank(product.getTbShopBrand().getId())) {
			product.setTbShopBrand(null);
		}
		if (product.getTbShopProductBaseinfo() != null && StringUtils.isBlank(product.getTbShopProductBaseinfo().getId())) {
			product.setTbShopProductBaseinfo(null);
		}

		for (String id : specifications) {
			if (StringUtils.isNotBlank(id)) {
				product.getTbShopSpecifications().add(new TbShopSpecification(id));
			}
		}
		product.setSales(0);
		product.setStock(0);
		product.setBlockedStock(0);

		// 评分
		product.setScore(BigDecimal.ZERO);
		product.setScoreCount(0);
		product.setScoreTotal(BigDecimal.ZERO);

		String datetime = Util.getNowDateTimeString();
		product.setCreateDate(datetime);
		product.setModifyDate(datetime);

		String id = "";
		try {
			product.setSyssn(SerialNumberUtil.buildProductSyssn());
			if (productService.exists("sn", product.getSn())) {
				return ajaxJsonErrorMessage("商品编号不能重复");
			}
			if (StringUtils.isEmpty(product.getSn())) {
				product.setSn(product.getSyssn());
			}
			id = productService.save(product);
			actionlog("新建商品", product.getName());
			// } catch (DataIntegrityViolationException e) {
			// return ajaxJsonErrorMessage(e.getCause().getCause().getCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage("", id);
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopProduct product = productService.get(id);
			model.addAttribute("product", product);
			List<TbShopSpecification> specifications = specificationService.getAll();
			model.addAttribute("specifications", specifications);
		}
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopProduct product, @RequestParam(value = "specifications[]", defaultValue = "") String specifications[])
			throws Exception {
		if (product.getTbShopBrand() != null && StringUtils.isBlank(product.getTbShopBrand().getId())) {
			product.setTbShopBrand(null);
		}
		if (product.getTbShopProductBaseinfo() != null && StringUtils.isBlank(product.getTbShopProductBaseinfo().getId())) {
			product.setTbShopProductBaseinfo(null);
		}
		product.setModifyDate(Util.getNowDateTimeString());

		TbShopProduct poProduct = productService.get(product.getId());

		// for (String id : specifications) {
		// for (TbShopSpecification tbShopSpecification : tbShopSpecifications) {
		// if (StringUtils.isNotBlank(id)) {
		// product.getTbShopSpecifications().add(new TbShopSpecification(id));
		// }
		// }
		// }

		if (poProduct.getTbShopSkus().size() == 0) { // 已建立SKU时则不能编辑规格属性
			List<TbShopSpecification> newSpecifications = new ArrayList<TbShopSpecification>();
			List<TbShopSpecification> tbShopSpecifications = poProduct.getTbShopSpecifications();
			Boolean hasChanged = false; // 规格项是否有变化
			// 是有有增加
			for (String id : specifications) {
				if (StringUtils.isBlank(id)) {
					continue;
				}
				boolean isNew = true;
				for (TbShopSpecification tbShopSpecification : tbShopSpecifications) {
					if (tbShopSpecification.getId().equals(id)) {
						newSpecifications.add(tbShopSpecification);
						isNew = false;
						break;
					}
				}
				if (isNew) {
					hasChanged = true;
					newSpecifications.add(new TbShopSpecification(id));
				}
			}
			// 是否有减少
			if (!hasChanged) {
				for (TbShopSpecification tbShopSpecification : tbShopSpecifications) {
					boolean exists = false;
					for (String id : specifications) {
						if (tbShopSpecification.getId().equals(id)) {
							exists = true;
							break;
						}
					}
					if (!exists) {
						hasChanged = true;
						break;
					}
				}
			}
			// hasChanged = hasChanged || specifications.length != tbShopSpecifications.size();
			if (hasChanged) {
				poProduct.setTbShopSpecifications(newSpecifications);
			}
		}

		BeanUtils.copyProperties(product, poProduct,
				new String[] { "id", "createDate", "syssn", "sales", "stock", "blockedStock", "tbShopSkus", "tbShopSpecifications",
						"tbShopMembers", "score", "scoreCount", "scoreTotal" });// , "price"

		if (StringUtils.isEmpty(product.getSn())) {
			product.setSn(product.getSyssn());
		}

		try {
			productService.update(poProduct);
			actionlog("编辑商品", product.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			productService.delete(ids.split(","));
			actionlog("删除商品", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/setStatus", method = RequestMethod.POST)
	public String setStatus(@RequestParam(value = "ids[]", defaultValue = "") String ids[],
			@RequestParam(value = "field", defaultValue = "") String field,
			@RequestParam(value = "status") String status) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}
		String[] fields = new String[] { "isSale", "isNew", "isHot", "isPromotion", "isRecomend", "isFreeShipping" };
		if (!ArrayUtils.contains(fields, field)) {
			return ajaxJsonErrorMessage("field参数错误");
		}

		try {
			productService.setValue(ids, field, status);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	// @CheckAuthority(AppConstants.PRODUCT)
	// @ResponseBody
	// @RequestMapping(value = "/updateFreightTemplate", method = RequestMethod.POST)
	// public String updateFreightTemplate(@RequestParam(value = "ids[]", defaultValue = "") String ids[], String templateId) throws Exception {
	// if ("".equals(ids)) {
	// return ajaxJsonErrorMessage("参数ID错误");
	// }
	// if ("".equals(templateId)) {
	// return ajaxJsonErrorMessage("templatId参数错误");
	// }
	//
	// try {
	// productService.setValue(ids, "tbShopFreightTemplate.id", templateId);
	// } catch (Exception e) {
	// return ajaxJsonErrorMessage(e.getMessage());
	// }
	//
	// return ajaxJsonSuccessMessage();
	// }

}
