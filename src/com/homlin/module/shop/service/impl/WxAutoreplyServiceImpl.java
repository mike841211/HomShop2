package com.homlin.module.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.dao.TbWxAutoreplyDao;
import com.homlin.module.shop.model.TbWxAutoreply;
import com.homlin.module.shop.service.WxAutoreplyService;
import com.homlin.utils.Pager;

@Service
public class WxAutoreplyServiceImpl extends BaseServiceImpl<TbWxAutoreply, String> implements WxAutoreplyService {

	@Autowired
	public void setBaseDao(TbWxAutoreplyDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbWxAutoreplyDao tbWxAutoreplyDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return tbWxAutoreplyDao.getPagedList(pager);
	}

	@Override
	@CacheEvict(value = EhCacheNames.wx_autoreply, allEntries = true)
	public void setValue(String[] ids, String field, String value) {
		tbWxAutoreplyDao.setValue(ids, field, value);
	}

	@Override
	@Cacheable(value = EhCacheNames.wx_autoreply, unless = "#result eq null")
	public List<TbWxAutoreply> findByKeyword(String keyword, int num) {
		return tbWxAutoreplyDao.findByKeyword(keyword, num);
	}

	// ---

	@Override
	@CacheEvict(value = EhCacheNames.wx_autoreply, allEntries = true)
	public String save(TbWxAutoreply model) throws Exception {
		return super.save(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.wx_autoreply, allEntries = true)
	public void update(TbWxAutoreply model) throws Exception {
		super.update(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.wx_autoreply, allEntries = true)
	public void delete(String[] ids) throws Exception {
		super.delete(ids);
	}

	@Override
	public void updateHits(String id) {
		tbWxAutoreplyDao.updateHits(id);
	}

}
