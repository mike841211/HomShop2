package com.homlin.module.shop.service;

import java.util.Map;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbPluginSignin;
import com.homlin.module.shop.model.TbShopMember;

public interface PluginSigninService extends BaseService<TbPluginSignin, String> {

	public Map<String, Object> signin(TbShopMember tbShopMember) throws Exception;

}
