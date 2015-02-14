package com.homlin.module.shop.controller.admin;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopSpecification;
import com.homlin.module.shop.model.TbShopSpecificationValue;
import com.homlin.module.shop.service.SpecificationService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller
@RequestMapping("/admin/specification")
public class SpecificationController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/specification";

	@Autowired
	SpecificationService specificationService;

	@CheckAuthority(AppConstants.SPECIFICATION)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.SPECIFICATION)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams();

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = specificationService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.SPECIFICATION)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() throws Exception {
		return RETURN_PATH + "/add";
	}

	@CheckAuthority(AppConstants.SPECIFICATION)
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopSpecification specification, RedirectAttributes redirectAttributes) throws Exception {

		String datetime = Util.getNowDateTimeString();
		Iterator<TbShopSpecificationValue> localIterator = specification.getTbShopSpecificationValues().iterator();
		while (localIterator.hasNext()) {
			TbShopSpecificationValue localSpecificationValue = localIterator.next();
			if (localSpecificationValue == null || StringUtils.isBlank(localSpecificationValue.getName())) {
				localIterator.remove();
			} else {
				localSpecificationValue.setCreateDate(datetime);
				localSpecificationValue.setModifyDate(datetime);
				localSpecificationValue.setTbShopSpecification(specification);
			}
		}

		if (specification.getTbShopSpecificationValues().size() < 1) {
			return ajaxJsonErrorMessage("必须至少保留一个规格值");
		}

		if (specification.getDisplayorder() == null) {
			specification.setDisplayorder(0);
		}
		specification.setCreateDate(datetime);
		specification.setModifyDate(datetime);
		try {
			specificationService.save(specification);
			actionlog("新建规格", specification.getName() + "[" + specification.getRemark() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.SPECIFICATION)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopSpecification specification = specificationService.get(id);
			model.addAttribute("specification", specification);
			model.addAttribute("valueSize", specification.getTbShopSpecificationValues().size()); // 规格值数量
		}
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.SPECIFICATION)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopSpecification specification) throws Exception {

		Iterator<TbShopSpecificationValue> localIterator = specification.getTbShopSpecificationValues().iterator();
		while (localIterator.hasNext()) {
			TbShopSpecificationValue localSpecificationValue = localIterator.next();
			if (localSpecificationValue == null || StringUtils.isBlank(localSpecificationValue.getName())) {
				localIterator.remove();
			} else {
				localSpecificationValue.setTbShopSpecification(specification);
			}
		}

		if (specification.getTbShopSpecificationValues().size() < 1) {
			return ajaxJsonErrorMessage("必须至少保留一个规格值");
		}

		if (specification.getDisplayorder() == null) {
			specification.setDisplayorder(0);
		}
		specification.setModifyDate(Util.getNowDateTimeString());

		try {
			specificationService.update(specification);
			actionlog("编辑规格", specification.getName() + "[" + specification.getRemark() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.SPECIFICATION)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			specificationService.delete(ids.split(","));
			actionlog("删除规格", ids);
		} catch (DataIntegrityViolationException e) {
			return ajaxJsonErrorMessage("删除失败：当前规格正被使用！");
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

}
