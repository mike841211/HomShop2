package com.homlin.module.shop.controller.shop.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.service.MemberService;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller
@RequestMapping({ "/member" })
public class MemberInfoController extends BaseMemberController {

	@Autowired
	MemberService memberService;

	// 填写订单明细
	@RequestMapping(value = "/info")
	public String info(Model model) throws Exception {
		if (!isMemberLogined()) {
			return "redirect:login.htm";
		}
		TbShopMember member = loadMember();
		model.addAttribute("member", member);
		return getTemplatePath() + "/member/info";
	}

	@ResponseBody
	@RequestMapping(value = "/info/update", method = RequestMethod.POST)
	public String update(TbShopMember voMember) throws Exception {
		TbShopMember member = loadMember();
		if (!Util.md5(voMember.getPassword(), member.getUsername().toUpperCase()).equals(member.getPassword())) {
			return ajaxJsonErrorMessage("密码错误");
		}
		member.setName(voMember.getName());
		member.setMobile(voMember.getMobile());
		member.setPhone(voMember.getPhone());
		member.setEmail(voMember.getEmail());
		member.setAreaCode(voMember.getAreaCode());
		member.setProvince(voMember.getProvince());
		member.setCity(voMember.getCity());
		member.setDistrict(voMember.getDistrict());
		member.setAddress(voMember.getAddress());
		member.setModifyDate(Util.getNowDateTimeString());
		try {
			memberService.update(member);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@RequestMapping(value = "/score")
	public String score(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
			@RequestParam(value = "pagesize", defaultValue = "10") int pageSize, Model model) {
		Map<String, Object> params = new HashMap<String, Object>();
		Pager pager = new Pager(pageIndex, pageSize, params);
		pager = memberService.getPagedScoreLogs(getQueryMember(), pager);
		model.addAttribute("pager", pager);
		return getTemplatePath() + "/member/score";
	}

}
