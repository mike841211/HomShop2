package com.homlin.utils;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {
	private static final String regex_v4 = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";

	// /*
	// * ____ = [0-9A-Fa-f]{1,4}
	// *
	// * (::)
	// * (:(:____){1,7})
	// * ((____:){1}(:____){1,6})
	// * ((____:){2}(:____){1,5})
	// * ((____:){3}(:____){1,4})
	// * ((____:){4}(:____){1,3})
	// * ((____:){5}(:____){1,2})
	// * ((____:){6}(:____){1})
	// * ((____:){1,7}:)
	// * ((____:){7}____)
	// *
	// *
	// * (:(:____){0,5}:(regex_v4))
	// * ((____:){1}(:____){1,4}:(regex_v4))
	// * ((____:){2}(:____){1,3}:(regex_v4))
	// * ((____:){3}(:____){1,2}:(regex_v4))
	// * ((____:){4}(:____){1,1}:(regex_v4))
	// * ((____:){1,5}:(regex_v4))
	// * ((____:){6}(regex_v4))
	// */
	// private static final String regex_v61 =
	// "^(::)|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){1}(:[0-9A-Fa-f]{1,4}){1,6})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}){1})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4}))$";
	private static final String regex_v6 = "^(::)|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){1}(:[0-9A-Fa-f]{1,4}){1,6})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}){1})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))|(([0-9A-Fa-f]{1,4}:){1}(:[0-9A-Fa-f]{1,4}){1,4}:(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,3}:(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,2}:(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,1}:(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))|(([0-9A-Fa-f]{1,4}:){1,5}:(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))|(([0-9A-Fa-f]{1,4}:){6}(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))$";

	public static boolean isIPV4(String ip) {
		return Pattern.matches(regex_v4, ip);
	}

	public static boolean isIPV6(String ip) {
		return Pattern.matches(regex_v6, ip);
	}

	/**
	 * 验证格式
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isValid(String ip) {
		return isIPV4(ip) || isIPV6(ip);
	}

	/**
	 * 
	 * @param ip
	 * @return xxxx:xxxx
	 */
	public static String ipv4ToHex(String ip) {
		String[] ips = ip.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ips.length; i++) {
			if (i % 2 == 0) {
				sb.append(":");
			}
			String hex = Integer.toHexString(Integer.parseInt(ips[i]));
			sb.append("00".substring(hex.length()));
			sb.append(hex);
		}
		return sb.toString().substring(1);
	}

	/**
	 * 将IP地址转换为可直接比较的格式
	 * 
	 * @param ip
	 * @return
	 */
	public static String formatIPV6(String ip) {
		if (isIPV4(ip)) {
			ip = "0000:0000:0000:0000:0000:0000:" + ipv4ToHex(ip);
			return ip;
		} else {
			if (ip.indexOf("%") > -1) {
				ip = ip.split("%")[0];
			}

			String[] ips = ip.split(":");
			StringBuilder sb;

			if (ip.indexOf(".") > -1) {// 混合模式
				String[] v4 = ipv4ToHex(ips[ips.length - 1]).split(":");
				sb = new StringBuilder();
				for (int i = 0; i < ips.length - 1; i++) {
					sb.append(ips[i]);
					sb.append(":");
				}
				sb.append(v4[0]);
				sb.append(":");
				sb.append(v4[1]);
				ip = sb.toString();
				ips = ip.split(":");
			}

			if (ips.length < 8) {
				ip = ip.replace("::", ":_:");
			}
			ips = ip.split(":");

			sb = new StringBuilder();
			for (String s : ips) {
				if ("_".equals(s)) {
					for (int i = 0; i < 8 - ips.length + 1; i++) {
						sb.append(":0000");
					}
				} else {
					sb.append(":");
					sb.append("0000".substring(s.length()));
					sb.append(s);
				}
			}
			return sb.toString().substring(1);
		}
	}

	/**
	 * 比较IP大小
	 * 
	 * @param ip1
	 * @param ip2
	 * @return -1: ip1&ltip2<br>
	 *         0: ip1=ip2<br>
	 *         1: ip1>ip2
	 */
	public static int compare(String ip1, String ip2) {
		ip1 = formatIPV6(ip1);
		ip2 = formatIPV6(ip2);
		return ip1.compareToIgnoreCase(ip2);
		// /*
		// * String[] ips1 = ip1.split("\\.");
		// * String[] ips2 = ip2.split("\\.");
		// * for (int i = 0; i < ips1.length; i++) {
		// * int i1 = Integer.parseInt(ips1[i]);
		// * int i2 = Integer.parseInt(ips2[i]);
		// * if (i1 > i2) {
		// * return 1;
		// * } else if (i1 < i2) {
		// * return -1;
		// * }
		// * }
		// * return 0;
		// */
	}

	/**
	 * 判断ip是否在startIP和endIP之间（包含相等）
	 * 
	 * @param ip
	 * @param startIP
	 * @param endIP
	 * @return
	 */
	public static boolean between(String ip, String startIp, String endIp) {
		return compare(ip, startIp) >= 0 && compare(ip, endIp) <= 0;
	}

	/**
	 * 获取远程IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
