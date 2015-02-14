package com.homlin.module.shop.job;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.homlin.module.shop.service.OrderService;

@Component
@Lazy(false)
public class OrderJob {

	@Autowired
	OrderService orderService;

	// 定期清理未付款订单释放冻结库存
	@Scheduled(cron = "${job.order_auto_cancelled.cron}")
	public void evictPaymentExpiredOrders() {
		Date expiredDate = DateUtils.addDays(new Date(), -3);
		orderService.evictPaymentExpiredOrders(expiredDate);
	}

	// 超期订单自动完成
	@Scheduled(cron = "${job.order_auto_confirmed.cron}")
	public void autoCompletedOrders() {
		Date expiredDate = DateUtils.addDays(new Date(), -15);
		orderService.autoCompletedOrders(expiredDate);
	}

}
