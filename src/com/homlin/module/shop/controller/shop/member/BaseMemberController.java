package com.homlin.module.shop.controller.shop.member;

import com.homlin.app.SpringContextHolder;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.controller.shop.BaseShopController;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.service.MemberService;

public class BaseMemberController extends BaseShopController {

	// ============================
	// [[ 读取 Member Session 值 ]]
	// ============================

	/**
	 * 获取当前会员ID
	 * 
	 * @return
	 */
	public String getMemberId() {
		return (String) getSession(AppConstants.SESSION_MEMBER_ID);
	}

	/**
	 * 获取当前会员登入账号
	 * 
	 * @return
	 */
	public String getMemberUsername() {
		return (String) getSession(AppConstants.SESSION_MEMBER_USERNAME);
	}

	/**
	 * 获取当前会员姓名
	 * 
	 * @return
	 */
	public String getMemberName() {
		return (String) getSession(AppConstants.SESSION_MEMBER_NAME);
	}

	/**
	 * 当前是否已登入
	 * 
	 * @return
	 */
	public boolean isMemberLogined() {
		Object object = getSession(AppConstants.SESSION_MEMBER_ISLOGIN);
		if (object == null) {
			return false;
		}
		return (Boolean) object;
	}

	public boolean isNotMemberLogined() {
		return !isMemberLogined();
	}

	// /**
	// * 从Session获取当前登入会员
	// *
	// * @return
	// */
	// public TbShopMember getMember() {
	// return (TbShopMember) getSession(AppConstants.SESSION_MEMBER);
	// }

	/**
	 * 仅限查询参数ID使用
	 */
	public TbShopMember getQueryMember() {
		return new TbShopMember(getMemberId());
	}

	/**
	 * 载入当前登入会员
	 * 
	 * @return
	 */
	public TbShopMember loadMember() {
		String id = getMemberId();
		if (id != null) {
			return SpringContextHolder.getBean(MemberService.class).get(id);
		}
		return null;
	}

	// ============================
	// ]] 读取 Member Session 值 ]]
	// ============================

}