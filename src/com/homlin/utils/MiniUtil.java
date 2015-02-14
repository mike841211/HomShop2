package com.homlin.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成miniui数据格式
 * 
 * @author wuduanpiao
 * @version 1.0.0
 */
public class MiniUtil {

	public static Map<String, Object> getGridData(Pager pager) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pager.getTotalCount());
		map.put("data", pager.getDataList());
		return map;
	}

	public static String getGridJsonData(Pager pager) {
		Map<String, Object> map = getGridData(pager);
		return JacksonUtil.toJsonString(map);
	}

	// ====================

	public static String formatTreeJsonData(List<Map<String, Object>> data) {
		for (Map<String, Object> map : data) {
			if ("false".equalsIgnoreCase(map.get("isLeaf").toString())) {
				map.put("isLeaf", false);
			} else {
				map.put("isLeaf", true);
			}
		}
		return JacksonUtil.toJsonString(data);
	}
}
