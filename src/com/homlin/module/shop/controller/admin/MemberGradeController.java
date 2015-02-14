package com.homlin.module.shop.controller.admin;

import java.util.List;
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
import com.homlin.module.shop.model.TbShopMemberGrade;
import com.homlin.module.shop.service.MemberGradeService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Util;

@Controller
@RequestMapping("/admin/member_grade")
public class MemberGradeController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/member_grade";

	@Autowired
	MemberGradeService memberGradeService;

	@CheckAuthority(AppConstants.MEMBER_GRADE)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.MEMBER_GRADE)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist() throws Exception {
		List<Map<String, Object>> list = memberGradeService.getAll();
		return JacksonUtil.toJsonString(list);
	}

	@CheckAuthority(AppConstants.MEMBER_GRADE)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() throws Exception {
		return RETURN_PATH + "/add";
	}

	@ResponseBody
	@CheckAuthority(AppConstants.MEMBER_GRADE)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TbShopMemberGrade memberGrade) throws Exception {
		memberGrade.setCreateDate(Util.getNowDateTimeString());
		memberGrade.setModifyDate(Util.getNowDateTimeString());

		try {
			if (memberGradeService.exists("name", memberGrade.getName())) {
				return ajaxJsonErrorMessage("等级名称重复，请重新设置");
			}
			if (memberGradeService.exists("lever", memberGrade.getLever())) {
				return ajaxJsonErrorMessage("级别重复，请重新设置");
			}
			memberGradeService.save(memberGrade);
			actionlog("新建会员等级", memberGrade.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.MEMBER_GRADE)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopMemberGrade memberGrade = memberGradeService.get(id);
			model.addAttribute("grade", memberGrade);
		}
		return RETURN_PATH + "/edit";
	}

	@CheckAuthority(AppConstants.MEMBER_GRADE)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopMemberGrade memberGrade) throws Exception {
		memberGrade.setModifyDate(Util.getNowDateTimeString());

		try {
			TbShopMemberGrade poMemberGrade = memberGradeService.get(memberGrade.getId());
			if (!memberGradeService.isUnique("name", poMemberGrade.getName(), memberGrade.getName())) {
				return ajaxJsonErrorMessage("等级名称重复，请重新设置");
			}
			if (!memberGradeService.isUnique("lever", poMemberGrade.getLever(), memberGrade.getLever())) {
				return ajaxJsonErrorMessage("级别重复，请重新设置");
			}
			memberGradeService.update(memberGrade);
			actionlog("编辑会员等级", memberGrade.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.MEMBER_GRADE)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			memberGradeService.delete(ids.split(","));
			actionlog("删除会员等级", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

	@CheckAuthority(AppConstants.MEMBER_GRADE)
	@ResponseBody
	@RequestMapping(value = "/getAllForSelect")
	public String getAllForSelect() throws Exception {
		return JacksonUtil.toJsonString(memberGradeService.getAllForSelect());
	}

}
