package com.homlin.module.shop.plugin.payment.wxpay.web;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class CommonUtil {

	public static String CreateNoncestr(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < length; i++) {
			Random rd = new Random();
			res += chars.indexOf(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static String FormatQueryParaMap(HashMap<String, String> parameters)
			throws SDKRuntimeException {

		String buff = "";
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(parameters.entrySet());

			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});

			for (int i = 0; i < infoIds.size(); i++) {
				Map.Entry<String, String> item = infoIds.get(i);
				if (item.getKey() != "") {
					buff += item.getKey() + "="
							+ URLEncoder.encode(item.getValue(), "utf-8") + "&";
				}
			}
			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			throw new SDKRuntimeException(e.getMessage());
		}

		return buff;
	}

	public static String FormatBizQueryParaMap(HashMap<String, String> paraMap,
			boolean urlencode) throws SDKRuntimeException {

		String buff = "";
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(paraMap.entrySet());

			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});

			for (int i = 0; i < infoIds.size(); i++) {
				Map.Entry<String, String> item = infoIds.get(i);
				// System.out.println(item.getKey());
				if (item.getKey() != "") {
					String key = item.getKey();
					String val = item.getValue();
					if (urlencode) {
						val = URLEncoder.encode(val, "UTF-8");
						val = val.replace("+", "%20"); // 注意！进行 urlencode 时要将空格转化为%20 而不是+
					}
					buff += key.toLowerCase() + "=" + val + "&";
				}
			}

			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			throw new SDKRuntimeException(e.getMessage());
		}
		return buff;
	}

	public static boolean IsNumeric(String str) {
		if (str.matches("\\d *")) {
			return true;
		} else {
			return false;
		}
	}

	public static String ArrayToXml(HashMap<String, String> arr) {
		String xml = "<xml>";

		Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			if (IsNumeric(val)) {
				xml += "<" + key + ">" + val + "</" + key + ">";

			} else
				xml += "<" + key + "><![CDATA[" + val + "]]></" + key + ">";
		}

		xml += "</xml>";
		return xml;
	}

}
