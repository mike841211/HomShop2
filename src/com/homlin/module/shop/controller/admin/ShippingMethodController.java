package com.homlin.module.shop.controller.admin;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopShippingMethod;
import com.homlin.module.shop.model.TbShopShippingMethodDetail;
import com.homlin.module.shop.service.ChinaAreaService;
import com.homlin.module.shop.service.ShippingMethodService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller("adminShippingMethodController")
@RequestMapping("/admin/shipping_method")
public class ShippingMethodController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/shipping_method";

	@Autowired
	ShippingMethodService shippingMethodService;

	@Autowired
	ChinaAreaService chinaAreaService;

	@CheckAuthority(AppConstants.SHIPPING_METHOD)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.SHIPPING_METHOD)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("name");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = shippingMethodService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.SHIPPING_METHOD)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) throws Exception {
		model.addAttribute("province", chinaAreaService.getProvices());
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.SHIPPING_METHOD)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopShippingMethod shippingMethod, String[] areaCodes, String[] areaNames, String[] firstWeights,
			String[] firstPrices, String[] continueWeights, String[] continuePrices) throws Exception {

		String datetime = Util.getNowDateTimeString();
		if (areaCodes != null) {
			for (int i = 0; i < areaCodes.length; i++) {
				TbShopShippingMethodDetail detail = new TbShopShippingMethodDetail();
				detail.setTbShopShippingMethod(shippingMethod);
				detail.setCreateDate(datetime);
				detail.setModifyDate(datetime);
				detail.setAreaCode(areaCodes[i]);
				detail.setAreaName(areaNames[i]);
				detail.setFirstWeight(Integer.valueOf(firstWeights[i]));
				detail.setFirstPrice(new BigDecimal(firstPrices[i]));
				detail.setContinueWeight(Integer.valueOf(continueWeights[i]));
				detail.setContinuePrice(new BigDecimal(continuePrices[i]));
				shippingMethod.getTbShopShippingMethodDetails().add(detail);
			}
		}
		shippingMethod.setCreateDate(datetime);
		shippingMethod.setModifyDate(datetime);
		try {
			shippingMethodService.save(shippingMethod);
			actionlog("新建配送方式", shippingMethod.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.SHIPPING_METHOD)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		}

		TbShopShippingMethod shippingMethod = shippingMethodService.get(id);
		model.addAttribute("shippingMethod", shippingMethod);
		model.addAttribute("province", chinaAreaService.getProvices());
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.SHIPPING_METHOD)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopShippingMethod shippingMethod, String[] areaCodes, String[] areaNames, String[] firstWeights,
			String[] firstPrices, String[] continueWeights, String[] continuePrices) throws Exception {

		String datetime = Util.getNowDateTimeString();
		if (areaCodes != null) {
			for (int i = 0; i < areaCodes.length; i++) {
				TbShopShippingMethodDetail detail = new TbShopShippingMethodDetail();
				detail.setTbShopShippingMethod(shippingMethod);
				detail.setCreateDate(datetime);
				detail.setModifyDate(datetime);
				detail.setAreaCode(areaCodes[i]);
				detail.setAreaName(areaNames[i]);
				detail.setFirstWeight(Integer.valueOf(firstWeights[i]));
				detail.setFirstPrice(new BigDecimal(firstPrices[i]));
				detail.setContinueWeight(Integer.valueOf(continueWeights[i]));
				detail.setContinuePrice(new BigDecimal(continuePrices[i]));
				shippingMethod.getTbShopShippingMethodDetails().add(detail);
			}
		}
		shippingMethod.setModifyDate(datetime);
		try {
			shippingMethodService.update(shippingMethod);
			actionlog("编辑配送方式", shippingMethod.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.SHIPPING_METHOD)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			shippingMethodService.delete(ids.split(","));
			actionlog("删除配送方式", ids);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.SHIPPING_METHOD)
	@ResponseBody
	@RequestMapping(value = "/getAllForSelect")
	public String getAllForSelect() throws Exception {
		return JacksonUtil.toJsonString(shippingMethodService.getAllForSelect());
	}

}
