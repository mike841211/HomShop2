package com.homlin.utils;

import java.util.HashMap;
import java.util.Map;

public class ExtUtil {

	public static Map<String, Object> getGridJsonData(Pager pager) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pager.getTotalCount());
		map.put("records", pager.getDataList());
		return map;
	}

}
