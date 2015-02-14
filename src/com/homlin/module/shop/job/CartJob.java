package com.homlin.module.shop.job;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.homlin.module.shop.service.CartService;

@Component
@Lazy(false)
public class CartJob {

	@Autowired
	CartService cartService;

	// 定时清理过期购物车
	@Scheduled(cron = "${job.cart_evict_expired.cron}")
	public void evictExpired() {
		Date expiredDate = DateUtils.addMonths(new Date(), -3);
		cartService.evictExpired(expiredDate);
	}

}
