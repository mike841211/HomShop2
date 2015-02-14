package com.homlin.module.shop.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.homlin.app.SpringContextHolder;
import com.homlin.module.shop.controller.admin.weixin.WxGroupController;
import com.homlin.utils.Util;

@Component
@Lazy(false)
public class WeixinJob {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	// 定期更新微信用户组
	@Scheduled(cron = "${job.weixin_loadGroups.cron}")
	public void loadGroups() {
		logger.info("定期更新微信用户组:" + Util.getNowDateTimeString());
		try {
			SpringContextHolder.getBean(WxGroupController.class).loadGroups();
		} catch (Exception e) {
			logger.error("WeixinJob.loadGroups 失败 - 定期更新微信用户组:" + Util.getNowDateTimeString());
			e.printStackTrace();
		}
	}

	// 关闭，通过手工点击下载用户同步
	// // 定期再次同步微信关注用户
	// @Scheduled(cron = "${job.weixin_loadUsers.cron}")
	// public void loadUsers() {
	// logger.info("定期再次同步微信关注用户:" + Util.getNowDateTimeString());
	// try {
	// SpringContextHolder.getBean(WxUserController.class).loadUsers();
	// } catch (Exception e) {
	// logger.error("WeixinJob.loadUsers 失败 - 定期再次同步微信关注用户:" + Util.getNowDateTimeString());
	// e.printStackTrace();
	// }
	// }

}
