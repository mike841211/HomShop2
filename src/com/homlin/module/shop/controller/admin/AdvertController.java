package com.homlin.module.shop.controller.admin;

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
import com.homlin.module.shop.model.TbShopAdvert;
import com.homlin.module.shop.service.AdvertService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller("adminAdvertController")
@RequestMapping("/admin/ad")
public class AdvertController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/advert";

	@Autowired
	AdvertService advertService;

	@CheckAuthority(AppConstants.ADVERT)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.ADVERT)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams();

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = advertService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.ADVERT)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() throws Exception {
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.ADVERT)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopAdvert advert) throws Exception {
		if (advert.getDisplayorder() == null) {
			advert.setDisplayorder(0);
		}
		advert.setCreateDate(Util.getNowDateTimeString());
		advert.setModifyDate(Util.getNowDateTimeString());
		try {
			advertService.save(advert);
			actionlog("新建广告位", advert.getKeyword() + ":" + advert.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ADVERT)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		}
		TbShopAdvert advert = advertService.get(id);
		model.addAttribute("advert", advert);
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.ADVERT)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopAdvert advert) throws Exception {
		if (advert.getDisplayorder() == null) {
			advert.setDisplayorder(0);
		}
		advert.setModifyDate(Util.getNowDateTimeString());
		try {
			advertService.update(advert);
			actionlog("编辑广告位", advert.getKeyword() + ":" + advert.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ADVERT)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			advertService.delete(ids.split(","));
			actionlog("删除广告位", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.ADVERT)
	@ResponseBody
	@RequestMapping(value = "/setInuse", method = RequestMethod.POST)
	public String setInuse(@RequestParam(value = "ids[]", defaultValue = "") String ids[],
			@RequestParam(value = "inuse") String inuse) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			advertService.setInuse(ids, inuse);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

}
