package com.homlin.module.shop.controller.shop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.controller.BaseController;
import com.homlin.module.shop.model.TbShopChinaArea;
import com.homlin.module.shop.service.ChinaAreaService;

// TODO 屏蔽站外请求
@Controller
@RequestMapping(value = "/china")
public class ChinaAreaController extends BaseController {

	@Autowired
	ChinaAreaService chinaAreaService;

	@ResponseBody
	@RequestMapping(value = "/provinces")
	public List<TbShopChinaArea> getProvices() {
		// todo 缓存省份
		List<TbShopChinaArea> list = chinaAreaService.getProvices();
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/cities/{code}")
	public List<TbShopChinaArea> getCities(@PathVariable String code) {
		if (code.length() < 2) {
			return new ArrayList<TbShopChinaArea>();
		}
		List<TbShopChinaArea> list = chinaAreaService.getCities(code);
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/districts/{code}")
	public List<TbShopChinaArea> getDistricts(@PathVariable String code) {
		if (code.length() < 4) {
			return new ArrayList<TbShopChinaArea>();
		}
		List<TbShopChinaArea> list = chinaAreaService.getDistricts(code);
		return list;
	}
}
