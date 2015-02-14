package com.homlin.module.shop.controller.admin;

import java.util.HashMap;
import java.util.Map;

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
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.service.MemberService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller("adminMemberController")
@RequestMapping("/admin/member")
public class MemberController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/member";

	@Autowired
	MemberService memberService;

	@CheckAuthority(AppConstants.MEMBER)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.MEMBER)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("username", "name", "mobile", "email");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = memberService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.MEMBER)
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		if ("".equals(id)) {
			addActionError("参数错误");
			return PAGE_ERROR;
		} else {
			TbShopMember member = memberService.get(id);
			model.addAttribute("member", member);
		}
		return RETURN_PATH + "/edit";
	}

	@ResponseBody
	@CheckAuthority(AppConstants.MEMBER)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TbShopMember member) throws Exception {
		TbShopMember poMember = memberService.get(member.getId());
		if (StringUtils.isNotEmpty(member.getPassword())) {
			poMember.setPassword(Util.md5(member.getPassword(), member.getUsername().toUpperCase()));
		}
		BeanUtils.copyProperties(member, poMember, new String[] { "id", "createDate", "username", "cardno", "password", "deposit",
				"depositAddup", "score", "scoreAddup", "loginCount", "loginDate", "loginIp", "loginFailureCount", "loginFailureDate",
				"registDate", "registIp", "registIparea", "safeQuestion", "safeAnswer", "passwordRecoverKey", "passwordRecoverDate",
				"tbShopOrders", "tbShopOrders", "tbShopMemberDepositLogs", "tbShopMemberScoreLogs", "favoriteProducts", "tbShopCart",
				"tbShopMemberReceivers", "tbShopMemberOauth2s", "tbPluginSignin", "tbPluginSigninLogs" });
		poMember.setModifyDate(Util.getNowDateTimeString());
		// TODO 判断Email，手机号
		try {
			memberService.update(poMember);
			actionlog("编辑会员信息", member.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.MEMBER)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids", required = true, defaultValue = "") String ids) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			memberService.delete(ids.split(","));
			actionlog("删除会员", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("操作成功");
	}

	// == 积分 ==

	@CheckAuthority(AppConstants.MEMBER)
	@RequestMapping(value = "/scorelogs", method = RequestMethod.GET)
	public String score() {
		return RETURN_PATH + "/scorelogs";
	}

	@CheckAuthority(AppConstants.MEMBER)
	@ResponseBody
	@RequestMapping(value = "/scorelogs", method = RequestMethod.POST)
	public String showScoreLogs(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
			@RequestParam(value = "pagesize", defaultValue = "20") int pageSize, String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		Pager pager = new Pager(pageIndex, pageSize, params);
		pager = memberService.getPagedScoreLogs(new TbShopMember(id), pager);
		return MiniUtil.getGridJsonData(pager);
	}

	@CheckAuthority(AppConstants.MEMBER)
	@ResponseBody
	@RequestMapping(value = "/setEnabled", method = RequestMethod.POST)
	public String setInuse(@RequestParam(value = "ids[]", defaultValue = "") String ids[],
			@RequestParam(value = "enabled") String enabled) throws Exception {
		if ("".equals(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}

		try {
			memberService.setEnabled(ids, enabled);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();
	}

}
