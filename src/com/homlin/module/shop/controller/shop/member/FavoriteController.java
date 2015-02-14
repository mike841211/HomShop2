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
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.module.shop.service.MemberService;
import com.homlin.module.shop.service.ProductService;
import com.homlin.utils.Pager;

@Controller
@RequestMapping({ "/member/favorite" })
public class FavoriteController extends BaseMemberController {

	@Autowired
	MemberService memberService;

	@Autowired
	ProductService productService;

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(String id) throws Exception {
		TbShopProduct product = productService.get(id);
		if (product == null) {
			return ajaxJsonErrorMessage("商品不存在");
		}
		TbShopMember member = loadMember();
		if (member.getFavoriteProducts().contains(product)) {
			return ajaxJsonErrorMessage("您已收藏过此商品");
		}
		member.getFavoriteProducts().add(product);
		memberService.update(member);
		return ajaxJsonSuccessMessage("收藏成功");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) throws Exception {
		TbShopProduct product = productService.get(id);
		if (product == null) {
			return ajaxJsonErrorMessage("商品不存在");
		}
		TbShopMember member = loadMember();
		if (!member.getFavoriteProducts().contains(product)) {
			return ajaxJsonErrorMessage("您未收藏过此商品");
		}
		member.getFavoriteProducts().remove(product);
		memberService.update(member);
		return ajaxJsonSuccessMessage("取消收藏成功");
	}

	@RequestMapping(value = "/list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
			@RequestParam(value = "pagesize", defaultValue = "20") int pageSize, Model model) {
		Map<String, Object> params = new HashMap<String, Object>();
		// params.put("member", new TbShopMember(getMemberId()));
		// params.put("member", loadMember());
		Pager pager = new Pager(pageIndex, pageSize, params);
		pager = productService.getFavoriteProducts(getQueryMember(), pager);
		model.addAttribute("pager", pager);
		return getTemplatePath() + "/member/favorite";
	}

}
