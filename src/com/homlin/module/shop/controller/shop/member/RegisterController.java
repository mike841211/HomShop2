package com.homlin.module.shop.controller.shop.member;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.AppConstants;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberGrade;
import com.homlin.module.shop.service.MemberGradeService;
import com.homlin.module.shop.service.MemberService;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.CaptchaServiceSingleton;
import com.homlin.utils.IPUtil;
import com.homlin.utils.Util;
import com.octo.captcha.service.CaptchaServiceException;

@Controller
@RequestMapping(value = "/member/register")
public class RegisterController extends BaseMemberController {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberGradeService memberGradeService;

	@RequestMapping(method = RequestMethod.GET)
	public String register(Model model) throws Exception {
		String referer = getRequest().getHeader("Referer");
		model.addAttribute("returnurl", referer);
		if (isMemberLogined()) {
			if (StringUtils.isBlank(referer)) {
				return "redirect:/";
			}
			return "redirect:" + referer;
		}
		model.addAttribute("returnurl", referer);
		return getTemplatePath() + "/member/register";
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public String save(TbShopMember member, String jcaptcha) throws Exception {

		if (StringUtils.isBlank(member.getUsername())) {
			return ajaxJsonErrorMessage("用户名不可为空");
		}
		if (StringUtils.isBlank(member.getPassword())) {
			return ajaxJsonErrorMessage("密码不可为空");
		}
		if (AppConstants.TRUE.equals(CacheConfigKeys.MEMBER_REQUIRED_EMAIL) && StringUtils.isBlank(member.getEmail())) {
			return ajaxJsonErrorMessage("Email不可为空");
		}
		if (AppConstants.TRUE.equals(CacheConfigKeys.MEMBER_REQUIRED_MOBILE) && StringUtils.isBlank(member.getMobile())) {
			return ajaxJsonErrorMessage("手机号不可为空");
		}

		// 检查验证码
		Boolean isCaptchaCorrect = Boolean.FALSE;
		try {
			String captchaId = getRequest().getSession().getId();
			// String jcaptcha = getRequest().getParameter(JCaptchaEngine.CAPTCHA_INPUT_NAME).toUpperCase();
			jcaptcha = jcaptcha.toUpperCase();
			isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId, jcaptcha);
		} catch (CaptchaServiceException e) {
			// should not happen, may be thrown if the id is not valid
			// e.printStackTrace();
		} catch (Exception e) {
		}
		if (!isCaptchaCorrect) {
			return ajaxJsonErrorCaptchaMessage("验证码输入错误!");
		}

		// 注册会员

		String datetime = Util.getNowDateTimeString();
		member.setCreateDate(datetime);
		member.setModifyDate(datetime);
		member.setPassword(Util.md5(member.getPassword(), member.getUsername().toUpperCase()));
		member.setDeposit(BigDecimal.ZERO);
		member.setDepositAddup(BigDecimal.ZERO);
		member.setScore(BigDecimal.ZERO);
		member.setScoreAddup(BigDecimal.ZERO);
		member.setEnabled(AppConstants.TRUE.equals(CacheUtil.getConfig(CacheConfigKeys.MEMBER_ENABLED)) ? AppConstants.TRUE
				: AppConstants.FALSE);
		member.setLoginCount(0);
		member.setLoginFailureCount(0);
		member.setRegistDate(datetime);
		member.setRegistIp(IPUtil.getRemoteIP(getRequest()));

		TbShopMemberGrade memberGrade = memberGradeService.getFirstLeverGrade();
		member.setTbShopMemberGrade(memberGrade);

		try {
			if (memberService.exists("username", member.getUsername())) {
				return ajaxJsonErrorMessage("用户名已被使用，请重新输入！");
			}
			if (StringUtils.isNotBlank(member.getEmail())) {
				if (memberService.exists("email", member.getEmail())) {
					return ajaxJsonErrorMessage("Email已被使用，请重新输入！");
				}
			}
			if (StringUtils.isNotBlank(member.getMobile())) {
				if (memberService.exists("mobile", member.getMobile())) {
					return ajaxJsonErrorMessage("手机号已被使用，请重新输入！");
				}
			}
			memberService.save(member);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage("注册成功！");
	}
}
