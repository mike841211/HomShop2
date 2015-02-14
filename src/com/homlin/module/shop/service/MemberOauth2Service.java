package com.homlin.module.shop.service;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberOauth2;

public interface MemberOauth2Service extends BaseService<TbShopMemberOauth2, String> {

	TbShopMember getTbShopMember(String openid);

}
