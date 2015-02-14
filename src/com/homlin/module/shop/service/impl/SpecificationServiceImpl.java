package com.homlin.module.shop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.exception.MessageException;
import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopSpecificationDao;
import com.homlin.module.shop.dao.TbShopSpecificationValueDao;
import com.homlin.module.shop.model.TbShopSpecification;
import com.homlin.module.shop.model.TbShopSpecificationValue;
import com.homlin.module.shop.service.SpecificationService;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Service
public class SpecificationServiceImpl extends BaseServiceImpl<TbShopSpecification, String> implements SpecificationService {

	@Autowired
	public void setBaseDao(TbShopSpecificationDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopSpecificationDao specificationDao;

	@Autowired
	TbShopSpecificationValueDao specificationValueDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return specificationDao.getPagedList(pager);
	}

	@Override
	public void update(TbShopSpecification specification) throws Exception {
		TbShopSpecification poSpecification = get(specification.getId());
		List<TbShopSpecificationValue> voValues = specification.getTbShopSpecificationValues();
		List<TbShopSpecificationValue> poValues = poSpecification.getTbShopSpecificationValues();

		List<TbShopSpecificationValue> values = new ArrayList<TbShopSpecificationValue>();
		String datetime = Util.getNowDateTimeString();

		// 规格值，新增或修改
		for (TbShopSpecificationValue value : voValues) {
			boolean isNew = true;
			for (TbShopSpecificationValue poValue : poValues) {
				if (poValue.getId().equalsIgnoreCase(value.getId()) || poValue.getName().equalsIgnoreCase(value.getName())) {
					poValue.setModifyDate(datetime);
					poValue.setName(value.getName());
					poValue.setImage(value.getImage());
					poValue.setDisplayorder(value.getDisplayorder());
					values.add(poValue);
					isNew = false;
					break;
				}
			}
			if (isNew) {
				value.setCreateDate(datetime);
				value.setModifyDate(datetime);
				values.add(value);
			}
		}
		specification.setTbShopSpecificationValues(values);

		// 规格值，删除
		// name值不存在的就删除
		for (TbShopSpecificationValue poValue : poValues) {
			boolean delete = true;
			for (TbShopSpecificationValue value : values) {
				if (poValue.getId().equals(value.getId())) {
					delete = false;
					break;
				}
			}
			if (delete) {
				if (poValue.getTbShopSkuSpecificationAndValues().size() > 0) { // 判断是否正被使用
					throw new MessageException("删除失败：规格值【" + poValue.getName() + "】正被使用！");
				}
				specificationValueDao.delete(poValue);
			}
		}
		specificationDao.merge(specification);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbShopSpecification> getAll() {
		String hql = "from TbShopSpecification order by displayorder";
		return (List<TbShopSpecification>) specificationDao.find(hql);
	}

}
