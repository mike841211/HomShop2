package com.homlin.module.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbWxGroupDao;
import com.homlin.module.shop.model.TbWxGroup;
import com.homlin.module.shop.service.WxGroupService;

@Service
public class WxGroupServiceImpl extends BaseServiceImpl<TbWxGroup, Integer> implements WxGroupService {

	@Autowired
	public void setBaseDao(TbWxGroupDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbWxGroupDao tbWxGroupDao;

	@Override
	public void merge(List<TbWxGroup> groups) {

		tbWxGroupDao.removeAll(); // 先清空

		for (TbWxGroup group : groups) {
			TbWxGroup tbWxGroup = tbWxGroupDao.get(group.getId());
			if (tbWxGroup == null) {
				tbWxGroupDao.save(group);
			} else {
				tbWxGroup.setName(group.getName());
				tbWxGroup.setCount(group.getCount());
			}
		}
	}

}
