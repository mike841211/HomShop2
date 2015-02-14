package com.homlin.module.shop.service;

import java.util.List;

import com.homlin.app.service.BaseService;
import com.homlin.module.shop.model.TbWxGroup;

public interface WxGroupService extends BaseService<TbWxGroup, Integer> {

	void merge(List<TbWxGroup> groups);

}
