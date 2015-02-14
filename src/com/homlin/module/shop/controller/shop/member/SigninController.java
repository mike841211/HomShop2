package com.homlin.module.shop.controller.shop.member;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.shop.execption.SignedException;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.service.MemberOauth2Service;
import com.homlin.module.shop.service.PluginSigninService;
import com.homlin.utils.CookieUtil;

@Controller
@RequestMapping(value = "/member")
public class SigninController extends BaseMemberController {

	@Autowired
	MemberOauth2Service memberOauth2Service;

	@Autowired
	PluginSigninService pluginSigninService;

	@ResponseBody
	@RequestMapping(value = "/signin")
	public String signin(HttpServletResponse response) throws Exception {
		try {
			TbShopMember tbShopMember = loadMember();
			Map<String, Object> map = pluginSigninService.signin(tbShopMember);
			signCookie(response);
			return ajaxJsonSuccessMessage("", map.get("score"));
		} catch (SignedException e) {
			signCookie(response);
			return ajaxJsonErrorMessage("今日已签到过！");
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}
	}

	private void signCookie(HttpServletResponse response) {
		Date now = new Date();
		Date tomorrow = DateUtils.ceiling(now, 5);
		int maxAge = (int) ((tomorrow.getTime() - now.getTime()) / 1000);
		CookieUtil.addCookie(response, "SIGNED", "1", maxAge);
	}

}
