package com.homlin.module.shop.controller.shop.weixin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.module.AppConstants;
import com.homlin.module.shop.controller.shop.BaseShopController;
import com.homlin.module.shop.model.TbWxAutoreply;
import com.homlin.module.shop.model.TbWxMenuMsg;
import com.homlin.module.shop.service.WxAutoreplyService;
import com.homlin.module.shop.service.WxMenuMsgService;

@Controller("weixinArticleController")
@RequestMapping("/weixin")
public class WeixinArticleController extends BaseShopController {

	@Autowired
	WxMenuMsgService wxMenuMsgService;

	@Autowired
	WxAutoreplyService wxAutoreplyService;

	@RequestMapping("/menu_msg/{id}")
	public String menu_msg(@PathVariable String id, Model model) {
		TbWxMenuMsg article = wxMenuMsgService.get(id);
		if (article == null || !AppConstants.TRUE.equals(article.getIsShow())) {
			addActionError("没有找到链接");
			return getTemplatePath() + "/error";
		}
		model.addAttribute("article", article);
		return getTemplatePath() + "/weixin/article";
	}

	@RequestMapping("/autoreply/{id}")
	public String autoreply(@PathVariable String id, Model model) {
		TbWxAutoreply article = wxAutoreplyService.get(id);
		if (article == null || !AppConstants.TRUE.equals(article.getIsShow())) {
			addActionError("没有找到链接");
			return getTemplatePath() + "/error";
		}
		model.addAttribute("article", article);
		return getTemplatePath() + "/weixin/article";
	}

	@ResponseBody
	@RequestMapping(value = "/menu_msg/hits/{id}")
	public void menu_msg_hits(@PathVariable String id) {
		wxMenuMsgService.updateHits(id);
	}

	@ResponseBody
	@RequestMapping(value = "/autoreply/hits/{id}")
	public void autoreply_hits(@PathVariable String id) {
		wxAutoreplyService.updateHits(id);
	}

}
