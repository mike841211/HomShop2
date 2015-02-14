package com.homlin.module.shop.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.homlin.module.shop.model.TbShopSku;
import com.homlin.module.shop.model.TbShopSkuSpecificationAndValue;
import com.homlin.module.shop.model.TbShopSpecification;
import com.homlin.module.shop.model.TbShopSpecificationValue;
import com.homlin.module.shop.service.ProductService;
import com.homlin.module.shop.service.SkuService;
import com.homlin.module.shop.service.SpecificationService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Util;

@Controller("adminSkuController")
@RequestMapping("/admin/sku")
public class SkuController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/sku";

	@Autowired
	ProductService productService;

	@Autowired
	SkuService skuService;

	@Autowired
	SpecificationService specificationService;

	@CheckAuthority(AppConstants.PRODUCT)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数ID错误");
			return PAGE_ERROR;
		}
		TbShopProduct product = productService.get(id);
		model.addAttribute("product", product);
		List<TbShopSpecification> specifications = specificationService.findAll();
		model.addAttribute("specifications", specifications);
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "id", defaultValue = "") String id) throws Exception {
		List<Map<String, Object>> list = skuService.getProductSkuList(id);
		return JacksonUtil.toJsonString(list);
	}

	private void checkNull(TbShopSku sku) {
		if (null == sku.getCost()) {
			sku.setCost(BigDecimal.ZERO);
		}
		if (null == sku.getPrice()) {
			sku.setPrice(BigDecimal.ZERO);
		}
		if (null == sku.getMarketPrice()) {
			sku.setMarketPrice(BigDecimal.ZERO);
		}
		if (null == sku.getStock()) {
			sku.setStock(0);
		}
		if (null == sku.getBlockedStock()) {
			sku.setBlockedStock(0);
		}
		if (null == sku.getSales()) {
			sku.setSales(0);
		}
		if (null == sku.getWeight()) {
			sku.setWeight(0);
		}
		if (null == sku.getLength()) {
			sku.setLength(0);
		}
		if (null == sku.getWidth()) {
			sku.setWidth(0);
		}
		if (null == sku.getHeight()) {
			sku.setHeight(0);
		}

	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopSku sku, HttpServletRequest request) throws Exception {

		checkNull(sku);

		TbShopProduct product = productService.get(sku.getTbShopProduct().getId());
		// 更新库存
		product.setStock(product.getStock() + sku.getStock());
		// // 最低价
		// if (sku.getPrice().compareTo(product.getPrice()) == -1) {
		// product.setPrice(sku.getPrice());
		// }

		// [[ sku syssn ]]
		String productSyssn = product.getSyssn();
		Set<TbShopSku> skus = product.getTbShopSkus();
		String syssn = "00";
		for (TbShopSku tbShopSku : skus) {
			String _syssn = tbShopSku.getSyssn();
			_syssn = _syssn.substring(_syssn.length() - 2);
			if (syssn.compareTo(_syssn) < 0) {
				syssn = _syssn;
			}
		}
		Integer num = Integer.valueOf(syssn) + 1;
		String skuSyssn = productSyssn.substring(0, productSyssn.length() - 2) + (num < 10 ? "0" : "") + num;
		sku.setSyssn(skuSyssn);
		if (StringUtils.isEmpty(sku.getSn())) {
			sku.setSn(sku.getSyssn());
		}
		// ]]

		List<String> specList = new ArrayList<String>();
		List<Map<String, Object>> specMaps = JacksonUtil.toMapList(sku.getSpecificationJson());
		for (Map<String, Object> map : specMaps) {
			String id = map.get("id").toString();
			String name = map.get("name").toString();
			String value_id = map.get("value_id").toString();
			String value_name = map.get("value_name").toString();
			if (StringUtils.isBlank(value_id)) {
				return ajaxJsonErrorMessage(name + " 没有选择");
			}
			// 规格值串
			specList.add(name + ":" + value_name);
			// 规格
			TbShopSkuSpecificationAndValue specificationAndValue = new TbShopSkuSpecificationAndValue();
			specificationAndValue.setTbShopSku(sku);
			specificationAndValue.setTbShopSpecification(new TbShopSpecification(id));
			specificationAndValue.setTbShopSpecificationValue(new TbShopSpecificationValue(value_id));
			sku.getTbShopSkuSpecificationAndValues().add(specificationAndValue);
		}
		sku.setSpecificationValueName(StringUtils.join(specList, ","));

		String datetime = Util.getNowDateTimeString();
		sku.setCreateDate(datetime);
		sku.setModifyDate(datetime);
		try {
			skuService.save(sku);
			actionlog("新建商品SKU", sku.getSn() + " " + sku.getSpecificationValueName());
			// } catch (DataIntegrityViolationException e) {
			// return ajaxJsonErrorMessage(e.getCause().getCause().getCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.PRODUCT)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopSku sku, HttpServletRequest request) throws Exception {

		checkNull(sku);

		TbShopProduct product = productService.get(sku.getTbShopProduct().getId());
		TbShopSku poSku = skuService.get(sku.getId());

		// 更新库存
		product.setStock(product.getStock() + sku.getStock() - poSku.getStock());
		// // 最低价
		// if (sku.getPrice().compareTo(product.getPrice()) == -1) {
		// product.setPrice(sku.getPrice());
		// }

		Set<TbShopSkuSpecificationAndValue> specificationAndValues = poSku.getTbShopSkuSpecificationAndValues();

		List<String> specList = new ArrayList<String>();
		List<Map<String, Object>> specMaps = JacksonUtil.toMapList(sku.getSpecificationJson());
		for (Map<String, Object> map : specMaps) {
			String id = map.get("id").toString();
			String name = map.get("name").toString();
			String value_id = map.get("value_id").toString();
			String value_name = map.get("value_name").toString();
			if (StringUtils.isBlank(value_id)) {
				return ajaxJsonErrorMessage(name + " 没有选择");
			}
			// 规格值串
			specList.add(name + ":" + value_name);
			// 规格
			boolean isNew = true;
			for (TbShopSkuSpecificationAndValue tbShopSkuSpecificationAndValue : specificationAndValues) {
				if (tbShopSkuSpecificationAndValue.getTbShopSpecification().getId().equals(id)
						&& tbShopSkuSpecificationAndValue.getTbShopSpecificationValue().getId().equals(value_id)) {
					isNew = false;
					sku.getTbShopSkuSpecificationAndValues().add(tbShopSkuSpecificationAndValue);
					break;
				}
			}
			if (isNew) {
				TbShopSkuSpecificationAndValue specificationAndValue = new TbShopSkuSpecificationAndValue();
				specificationAndValue.setTbShopSku(sku);
				specificationAndValue.setTbShopSpecification(new TbShopSpecification(id));
				specificationAndValue.setTbShopSpecificationValue(new TbShopSpecificationValue(value_id));
				sku.getTbShopSkuSpecificationAndValues().add(specificationAndValue);
			}
		}
		sku.setSpecificationValueName(StringUtils.join(specList, ","));

		String datetime = Util.getNowDateTimeString();
		sku.setModifyDate(datetime);

		BeanUtils.copyProperties(sku, poSku, new String[] { "id", "createDate", "syssn", "sales", "blockedStock" });
		if (StringUtils.isEmpty(sku.getSn())) {
			sku.setSn(sku.getSyssn());
		}
		try {
			skuService.update(poSku);
			actionlog("新建商品SKU", sku.getSn() + " " + sku.getSpecificationValueName());
			// } catch (DataIntegrityViolationException e) {
			// return ajaxJsonErrorMessage(e.getCause().getCause().getCause().getMessage());
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
			skuService.delete(ids.split(","));
			actionlog("删除商品", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

}
