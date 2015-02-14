package com.homlin.module.shop.util;

import org.apache.commons.lang.StringUtils;

import com.homlin.app.SpringContextHolder;
import com.homlin.module.shop.service.OrderService;
import com.homlin.module.shop.service.ProductService;

/**
 * 工具类 - 编号生成
 */
public class SerialNumberUtil {

	// 货号
	public static final String PRODUCT_SYSSN_PREFIX = "";// 编号前缀_
	public static final long PRODUCT_SYSSN_FIRST = 140200000L;// 编号起始数
	public static final long PRODUCT_SYSSN_STEP = 100L;// 订单编号步长
	public static Long lastProductSyssnNumber;

	// 订单编号
	public static final String ORDER_SN_PREFIX = "";
	public static final long ORDER_SN_FIRST = 201405000L;
	public static final long ORDER_SN_STEP = 1L;// 订单编号步长
	public static Long lastOrderSnNumber;

	static {

		// 商品货号
		ProductService productService = SpringContextHolder.getBean(ProductService.class);
		String lastProductSn = productService.getLastProductSyssn();
		if (StringUtils.isNotBlank(lastProductSn)) {
			lastProductSyssnNumber = Long.parseLong(StringUtils.removeStartIgnoreCase(lastProductSn, PRODUCT_SYSSN_PREFIX));
		} else {
			lastProductSyssnNumber = PRODUCT_SYSSN_FIRST;
		}

		// 订单编号
		OrderService orderService = SpringContextHolder.getBean(OrderService.class);
		String lastOrderSn = orderService.getLastOrderSn();
		if (StringUtils.isNotBlank(lastOrderSn)) {
			lastOrderSnNumber = Long.parseLong(StringUtils.removeStartIgnoreCase(lastOrderSn, ORDER_SN_PREFIX));
		} else {
			lastOrderSnNumber = ORDER_SN_FIRST;
		}
	}

	/**
	 * 生成货号，连续数字
	 * 
	 * @return 货号
	 */
	public synchronized static String buildProductSyssn() {
		lastProductSyssnNumber += PRODUCT_SYSSN_STEP;
		return PRODUCT_SYSSN_PREFIX + lastProductSyssnNumber;
	}

	/**
	 * 生成订单编号
	 * 
	 * @return 订单编号
	 */
	public synchronized static String buildOrderSn() {
		lastOrderSnNumber += ORDER_SN_STEP;
		return ORDER_SN_PREFIX + lastOrderSnNumber;
	}

}