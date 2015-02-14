package com.homlin.module.shop.controller.shop.member;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.service.MemberOauth2Service;
import com.homlin.module.shop.service.MemberService;
import com.homlin.utils.CaptchaServiceSingleton;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Util;
import com.octo.captcha.service.CaptchaServiceException;

@Controller
@RequestMapping(value = "/member")
public class LoginController extends BaseMemberController {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberOauth2Service memberOauth2Service;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		String referer = getRequest().getHeader("Referer");
		// if (isMemberLogined()) {
		// if (StringUtils.isBlank(referer)) {
		// return "redirect:/";
		// }
		// return "redirect:" + referer;
		// }
		model.addAttribute("returnurl", referer);
		return getTemplatePath() + "/member/login";

	}

	private void loginSucceed(TbShopMember member) {

		member.setLoginCount(member.getLoginCount() + 1);
		member.setLoginDate(Util.getNowDateTimeString());
		member.setLoginIp(getRequest().getRemoteAddr());
		member.setLoginFailureCount(0);
		memberService.updateLoginInfo(member);

		// 登录HttpSession
		HttpSession session = getRequest().getSession();
		session.setAttribute(AppConstants.SESSION_MEMBER_ISLOGIN, true);
		session.setAttribute(AppConstants.SESSION_MEMBER_ID, member.getId());
		session.setAttribute(AppConstants.SESSION_MEMBER_USERNAME, member.getUsername());
		session.setAttribute(AppConstants.SESSION_MEMBER_NAME, member.getName());
		// session.setAttribute(AppConstants.SESSION_MEMBER, member);
		// session.setAttribute(AppConstants.SESSION_MEMBER_GRADE, member.getTbShopMemberGrade());
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(TbShopMember member, String jcaptcha, @RequestParam(defaultValue = "") String loginType) throws Exception {

		if (StringUtils.isBlank(jcaptcha)) {
			return ajaxJsonErrorMessage("验证码不可为空");
		}

		// [[ 登入方式 ]]

		boolean b = false; // 指定登入方式
		boolean loginByUsername = false;
		if (!b) {
			loginByUsername = loginType.equals("username");
			if (loginByUsername) {
				if (StringUtils.isBlank(member.getUsername())) {
					return ajaxJsonErrorMessage("用户名不可为空");
				}
				b = true;
			}
		}
		boolean loginByCardno = false;
		if (!b) {
			loginByCardno = loginType.equals("cardno");
			if (loginByCardno) {
				if (StringUtils.isBlank(member.getCardno())) {
					return ajaxJsonErrorMessage("卡号不可为空");
				}
				b = true;
			}
		}
		boolean loginByEmail = false;
		if (!b) {
			loginByEmail = loginType.equals("email");
			if (loginByEmail) {
				if (StringUtils.isBlank(member.getEmail())) {
					return ajaxJsonErrorMessage("电子邮箱不可为空");
				}
				b = true;
			}
		}
		boolean loginByMobile = false;
		if (!b) {
			loginByMobile = loginType.equals("mobile");
			if (loginByMobile) {
				if (StringUtils.isBlank(member.getMobile())) {
					return ajaxJsonErrorMessage("手机号不可为空");
				}
				b = true;
			}
		}

		if (!b) {
			return ajaxJsonErrorMessage("参数错误：loginType");
		}

		// ]] 登入方式 ]]

		String password = member.getPassword();
		if (StringUtils.isBlank(password)) {
			return ajaxJsonErrorMessage("密码不可为空");
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
			// 注意：<Context sessionCookieDomain="xxx" 是否设置了
			// e.printStackTrace();
		} catch (Exception e) {
		}
		if (!isCaptchaCorrect) {
			return ajaxJsonErrorMessage("验证码输入错误!");
		}

		// 登入
		try {
			if (loginByUsername) {
				member = memberService.getByUsername(member.getUsername());
			} else if (loginByCardno) {
				member = memberService.getByCardno(member.getCardno());
			} else if (loginByEmail) {
				member = memberService.getByEmail(member.getEmail());
			} else if (loginByMobile) {
				member = memberService.getByMobile(member.getMobile());
			}
			if (member == null) {
				return ajaxJsonErrorMessage("登入账号或密码错误");
			} else if (!Util.md5(password, member.getUsername() == null ? null : member.getUsername().toUpperCase())
					.equals(member.getPassword())) {
				return ajaxJsonErrorMessage("密码或登入账号错误");
			} else if (!AppConstants.TRUE.equals(member.getEnabled())) {
				return ajaxJsonErrorMessage("当前账号帐号已禁用");
			}
			loginSucceed(member);
		} catch (Exception e) {
			return ajaxJsonErrorMessage(e.getMessage());
		}

		return ajaxJsonSuccessMessage();

	}

	@RequestMapping(value = "/logout")
	public String logout() throws Exception {
		// String referer = getRequest().getHeader("Referer");
		getSession().invalidate();
		return "redirect:/";
	}

	@RequestMapping(value = "/login", params = { "state=weixin", "code" })
	public String oauth2_login(String code, HttpServletResponse response) throws Exception {
		String retString = null;
		try {
			retString = WeixinUtil.getSnsAccessToken(code);
			// System.err.println(retString);
		} catch (Exception e) {
			return errorPage(e.getMessage());
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JacksonUtil.toObject(retString, Map.class);
		String openid = map.get("openid").toString();

		TbShopMember tbShopMember = memberOauth2Service.getTbShopMember(openid);
		if (null == tbShopMember) {
			return errorPage("登入失败：当前微信号未绑定会员");
		} else if (!AppConstants.TRUE.equals(tbShopMember.getEnabled())) {
			return errorPage("登入失败：当前账号帐号已禁用");
		}
		loginSucceed(tbShopMember);

		return "redirect:/member/index.htm";
		// return getTemplatePath("/member/index");
	}

}
