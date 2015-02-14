package com.homlin.module.shop.controller.shop.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberOauth2;
import com.homlin.module.shop.model.TbShopMemberOauth2.OAuthType;
import com.homlin.module.shop.plugin.weixin.WeixinUtil;
import com.homlin.module.shop.service.MemberOauth2Service;
import com.homlin.module.shop.service.MemberService;
import com.homlin.utils.JacksonUtil;
import com.homlin.utils.Util;

@Controller
@RequestMapping({ "/member" })
public class BindController extends BaseMemberController {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberOauth2Service memberOauth2Service;

	// 填写订单明细
	@RequestMapping(value = "/bind")
	public String info(Model model) throws Exception {
		Map<String, Object> bindOauth2s = new HashMap<String, Object>();
		TbShopMember member = loadMember();
		Set<TbShopMemberOauth2> oauth2s = member.getTbShopMemberOauth2s();
		for (TbShopMemberOauth2 tbShopMemberOauth2 : oauth2s) {
			bindOauth2s.put(tbShopMemberOauth2.getOAuthType().name(), true);
		}
		model.addAttribute("bindOauth2s", bindOauth2s);
		return getTemplatePath() + "/member/bind";
	}

	// 取消返回 GET：/member/bind.htm?state=&code=authdeny&reason=weixin
	// 绑定微信账号
	@RequestMapping(value = "/bind", params = { "state=weixin", "code" })
	public String bind(String code) throws Exception {
		String retString = null;
		try {
			retString = WeixinUtil.getSnsAccessToken(code);
			// System.err.println(retString);
		} catch (Exception e) {
			return errorPage(e.getMessage());
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JacksonUtil.toObject(retString, Map.class);

		TbShopMember tbShopMember = loadMember();
		Set<TbShopMemberOauth2> oauth2s = tbShopMember.getTbShopMemberOauth2s();
		// 是否已绑定过
		String openid = map.get("openid").toString();
		for (TbShopMemberOauth2 tbShopMemberOauth2 : oauth2s) {
			if (tbShopMemberOauth2.getOAuthType() != OAuthType.WEIXIN) {
				continue;
			}
			if (tbShopMemberOauth2.getOpenid().equals(openid)) {
				return getTemplatePath("/member/bind");
			}
		}

		TbShopMemberOauth2 tbShopMemberOauth2 = new TbShopMemberOauth2();
		tbShopMemberOauth2.setOAuthType(OAuthType.WEIXIN);
		String datatime = Util.getNowDateTimeString();
		tbShopMemberOauth2.setCreateDate(datatime);
		tbShopMemberOauth2.setModifyDate(datatime);
		tbShopMemberOauth2.setOpenid(map.get("openid").toString());
		tbShopMemberOauth2.setAccessToken(map.get("access_token").toString());
		tbShopMemberOauth2.setRefreshToken(map.get("refresh_token").toString());
		tbShopMemberOauth2.setTbShopMember(tbShopMember);

		memberOauth2Service.save(tbShopMemberOauth2);

		return "redirect:/member/bind.htm";
	}

}
