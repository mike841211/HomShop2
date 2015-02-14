package com.homlin.module.shop.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.EhCacheNames;
import com.homlin.module.shop.dao.TbShopShippingMethodDao;
import com.homlin.module.shop.model.TbShopShippingMethod;
import com.homlin.module.shop.model.TbShopShippingMethodDetail;
import com.homlin.module.shop.service.ShippingMethodService;
import com.homlin.utils.Pager;

@Service
public class ShippingMethodServiceImpl extends BaseServiceImpl<TbShopShippingMethod, String> implements ShippingMethodService {

	@Autowired
	public void setBaseDao(TbShopShippingMethodDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopShippingMethodDao shippingMethodDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return shippingMethodDao.getPagedList(pager);
	}

	@Override
	@Cacheable(value = EhCacheNames.common, key = "'ShippingMethods'")
	public List<Map<String, Object>> getAllForSelect() {
		return shippingMethodDao.getAllForSelect();
	}

	// 计算运费
	@Override
	public BigDecimal getShippingFee(String shippingMethodId, Integer totalWeight, String areaCode) {
		BigDecimal shippingFee;
		TbShopShippingMethod shippingMethod = shippingMethodDao.get(shippingMethodId);
		Integer firstWeight = shippingMethod.getFirstWeight();
		BigDecimal firstPrice = shippingMethod.getFirstPrice();
		Integer continueWeight = shippingMethod.getContinueWeight();
		BigDecimal continuePrice = shippingMethod.getContinuePrice();
		if (StringUtils.isNotBlank(areaCode)) { // 检查特定地区
			String province = areaCode.substring(0, 2) + "0000";
			Set<TbShopShippingMethodDetail> details = shippingMethod.getTbShopShippingMethodDetails();
			for (TbShopShippingMethodDetail detail : details) {
				if (detail.getAreaCode().indexOf(province) > -1) {
					firstWeight = detail.getFirstWeight();
					firstPrice = detail.getFirstPrice();
					continueWeight = detail.getContinueWeight();
					continuePrice = detail.getContinuePrice();
					break;
				}
			}
		}
		if (totalWeight <= firstWeight || continuePrice.compareTo(BigDecimal.ZERO) == 0) {
			shippingFee = firstPrice;
		} else {
			double d = Math.ceil((totalWeight.intValue() - firstWeight.intValue()) / continueWeight.doubleValue());// doubleValue
			shippingFee = firstPrice.add(continuePrice.multiply(new BigDecimal(d)));
		}
		return shippingFee;
	}

	// ---

	@Override
	@CacheEvict(value = EhCacheNames.common, key = "'ShippingMethods'")
	public String save(TbShopShippingMethod model) throws Exception {
		return super.save(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.common, key = "'ShippingMethods'")
	public void update(TbShopShippingMethod model) throws Exception {
		super.update(model);
	}

	@Override
	@CacheEvict(value = EhCacheNames.common, key = "'ShippingMethods'")
	public void delete(String[] ids) throws Exception {
		super.delete(ids);
	}

}
