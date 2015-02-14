package com.homlin.module.shop.controller.admin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.commom.mail.MailService;
import com.homlin.module.shop.model.TbShopConfig;
import com.homlin.module.shop.plugin.payment.alipay.AlipayConfig;
import com.homlin.module.shop.plugin.payment.tenpay.TenpayConfig;
import com.homlin.module.shop.plugin.payment.unionpay.upmp.conf.UpmpConfig;
import com.homlin.module.shop.plugin.payment.unionpay.upop.UpopConfig;
import com.homlin.module.shop.plugin.payment.wxpay.WxpayConfig;
import com.homlin.module.shop.plugin.weixin.WeixinConfig;
import com.homlin.module.shop.service.ConfigService;
import com.homlin.module.shop.util.CacheUtil;

@Controller("adminConfigController")
@RequestMapping("/admin/config")
public class ConfigController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/config";

	@Autowired
	ConfigService configService;

	@Autowired
	MailService mailService;

	@CheckAuthority(AppConstants.CONFIG)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String config(Model model) throws Exception {
		Map<String, TbShopConfig> configMap = new HashMap<String, TbShopConfig>();
		List<TbShopConfig> list = configService.findAll();
		for (TbShopConfig config : list) {
			configMap.put(config.getCfgKey(), config);
		}
		model.addAttribute("config", configMap);
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.CONFIG)
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateconfig(@RequestParam("cfgKey") String[] cfgKeys, @RequestParam("cfgValue") String[] cfgValues) throws Exception {
		Set<TbShopConfig> configs = new HashSet<TbShopConfig>();
		for (int i = 0; i < cfgKeys.length; i++) {
			TbShopConfig config = new TbShopConfig();
			config.setCfgKey(cfgKeys[i]);
			config.setCfgValue(cfgValues[i]);
			configs.add(config);
			CacheUtil.setConfig(cfgKeys[i], cfgValues[i]); // 重新缓存
		}
		configService.updateConfigs(configs);
		// SysConfigUtil.initSysConfigCache(); // 重新缓存
		return ajaxJsonSuccessMessage();
	}

	@CheckAuthority(AppConstants.CONFIG)
	@ResponseBody
	@RequestMapping(value = "/testEmailConfig", method = RequestMethod.POST)
	public String testEmailConfig(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword,
			String toMail) {
		try {
			mailService.testEmailConfig(smtpFromMail, smtpHost, smtpPort, smtpUsername, smtpPassword, toMail);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage("发送失败，请检查配置！");
		}
		return ajaxJsonSuccessMessage();
	}

	// 如果修改了config.properties对应配置请执行此操作
	@CheckAuthority(AppConstants.CONFIG)
	@ResponseBody
	@RequestMapping(value = "/initConfig")
	public String initConfig(String config) {
		try {
			if ("WeixinConfig".equals(config)) {
				// System.err.println(WxpayConfig.appId);
				// System.err.println(WxpayConfig.appSecret);
				// System.err.println(WxpayConfig.partnerId);
				// System.err.println(WxpayConfig.partnerKey);
				// System.err.println(WxpayConfig.paySignKey);
				WeixinConfig.initConfig();
				// if (WxpayConfig.enabled) {
				WxpayConfig.initConfig();
				// }
			} else if ("AlipayConfig".equals(config)) {
				AlipayConfig.initConfig();
			} else if ("UpmpConfig".equals(config)) {
				UpmpConfig.initConfig();
			} else if ("UpopConfig".equals(config)) {
				UpopConfig.initConfig();
			} else if ("TenpayConfig".equals(config)) {
				TenpayConfig.initConfig();
			} else if ("WxpayConfig".equals(config)) {
				WxpayConfig.initConfig();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxJsonErrorMessage(e.getMessage());
		}
		return ajaxJsonSuccessMessage();
	}

}
