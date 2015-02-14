package com.homlin.utils;

import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * 生成验证码图片服务
 */
public class CaptchaServiceSingleton {

	// private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService();
	private static ImageCaptchaService instance;
	static {
		instance = new DefaultManageableImageCaptchaService(
				new FastHashMapCaptchaStore(),
				new JCaptchaEngine(),
				180,
				100000,
				75000);
	}

	public static ImageCaptchaService getInstance() {
		return instance;
	}
}