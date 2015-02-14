package com.homlin.module.shop.controller.shop.member;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.commom.mail.MailService;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.service.MemberGradeService;
import com.homlin.module.shop.service.MemberService;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.Util;

@Controller
@RequestMapping(value = "/member/password")
public class PasswordController extends BaseMemberController {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberGradeService memberGradeService;

	@Autowired
	MailService mailService;

	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String reset(Model model) throws Exception {
		return getTemplatePath() + "/member/password/reset";
	}

	@ResponseBody
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String reset(String password, String newpassword) throws Exception {

		if (StringUtils.isBlank(password)) {
			return ajaxJsonErrorMessage("当前密码不可为空");
		}
		if (StringUtils.isBlank(newpassword)) {
			return ajaxJsonErrorMessage("新密码不可为空");
		}

		// 注册会员

		TbShopMember member = loadMember();
		password = Util.md5(password, member.getUsername().toUpperCase());
		if (!password.equals(member.getPassword())) {
			return ajaxJsonErrorMessage("当前密码输入错误");
		}
		newpassword = Util.md5(newpassword, member.getUsername().toUpperCase());

		if (!password.equals(newpassword)) {
			member.setModifyDate(Util.getNowDateTimeString());
			member.setPassword(newpassword);
			try {
				memberService.update(member);
			} catch (Exception e) {
				e.printStackTrace();
				return ajaxJsonErrorMessage(e.getMessage());
			}
		}
		return ajaxJsonSuccessMessage("密码修改成功！");
	}

	@RequestMapping(value = "/forget", method = RequestMethod.GET)
	public String forget(Model model) throws Exception {
		return getTemplatePath() + "/member/password/forget";
	}

	@ResponseBody
	@RequestMapping(value = "/forget", method = RequestMethod.POST)
	public String forget(String username, String email) throws Exception {

		if (StringUtils.isBlank(username)) {
			return ajaxJsonErrorMessage("请输入用户名！");
		}
		if (StringUtils.isBlank(email)) {
			return ajaxJsonErrorMessage("您注册时留下的电子邮箱！");
		}

		TbShopMember member = memberService.getByUsername(username);
		if (member == null || !email.equalsIgnoreCase(member.getEmail())) {
			return ajaxJsonErrorMessage("用户名和电子邮箱不匹配");
		}

		String token = UUID.randomUUID().toString();
		String expired = Util.getDateTimeString(DateUtils.addHours(new Date(), 24));

		String SHOP_NAME = CacheUtil.getConfig(CacheConfigKeys.SHOP_NAME);
		String url = CacheUtil.getConfig(CacheConfigKeys.SHOP_URL) + getRequest().getContextPath();
		url = url + "/member/password/forget_reset.htm?username=" + username + "&token=" + token;
		String subject = SHOP_NAME + " 密码重置";
		String content = SHOP_NAME + "密码重置，用户名：" + username + "，重置链接：<a href=\"" + url + "\" target=\"_blank\">" + url + "</a><br>链接24小时内有效";
		String sentto = email;
		mailService.send(subject, content, sentto);

		member.setPasswordRecoverKey(token);
		member.setPasswordRecoverDate(expired);
		memberService.update(member);
		return ajaxJsonSuccessMessage();
	}

	@RequestMapping(value = "/forget_reset", method = RequestMethod.GET)
	public String forget_reset(Model model) throws Exception {
		return getTemplatePath() + "/member/password/forget_reset";
	}

	@ResponseBody
	@RequestMapping(value = "/forget_reset", method = RequestMethod.POST)
	public String forget_reset(String username, String token, String newpassword) throws Exception {

		if (StringUtils.isBlank(username) || StringUtils.isBlank(token)) {
			return ajaxJsonErrorMessage("不安全的链接");
		}
		if (StringUtils.isBlank(newpassword)) {
			return ajaxJsonErrorMessage("新密码不可为空");
		}

		// 会员
		TbShopMember member = memberService.getByUsername(username);
		if (member == null || !token.equalsIgnoreCase(member.getPasswordRecoverKey())) {
			return ajaxJsonErrorMessage("链接已失效");
		}
		if (Util.getNowDateTimeString().compareTo(member.getPasswordRecoverDate()) > 0) {
			return ajaxJsonErrorMessage("当前链接已失效");
		}

		newpassword = Util.md5(newpassword, member.getUsername().toUpperCase());
		member.setPassword(newpassword);
		member.setModifyDate(Util.getNowDateTimeString());
		member.setPasswordRecoverDate(null);
		member.setPasswordRecoverKey(null);
		try {
			memberService.update(member);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage("密码修改成功！");
	}

}
