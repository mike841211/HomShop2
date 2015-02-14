package com.homlin.utils;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

public class HqlHelper {

	public static final String DBMS_MSSQL = "MSSQL";
	public static final String DBMS_MYSQL = "MYSQL";

	private static String DBMS = DBMS_MSSQL;

	private static final ResourceBundle bundle = ResourceBundle.getBundle("config");

	static {
		String dialect = bundle.getString("hibernate.dialect");
		if ("org.hibernate.dialect.SQLServer2005Dialect".equals(dialect)) {
			DBMS = DBMS_MSSQL;
		} else if ("org.hibernate.dialect.MySQL5Dialect".equals(dialect)) {
			DBMS = DBMS_MYSQL;
		}
	}

	private HqlHelper() {
	}

	// /**
	// * 根据不同数据库类型处理hql中使用的函数
	// *
	// * @param hql
	// * @return
	// */
	// public static String prepareHql(String hql) {
	// if (DBMS.equals(DBMS_MSSQL)) {
	// hql = hql.replace("$ISNULL$", "isnull");
	// } else if (DBMS.equals(DBMS_MYSQL)) {
	// hql = hql.replace("$ISNULL$", "ifnull");
	// }
	// return hql;
	// }

	/**
	 * 
	 * @param fieldString
	 * @param models
	 *            可直接映射的对象名： <br>
	 *            例：models包含goods.type，则goods.type.name 可直接映射为 name
	 * @return
	 */
	public static String mapping(String fieldString, String... models) {
		String[] fields = fieldString.split(",");
		ArrayList<String> list = new ArrayList<String>();
		for (String field : fields) {
			if (field.toLowerCase().indexOf(" as ") < 1) {
				field = StringUtils.trim(field);
				String mappingName = field;
				if (field.indexOf(".") > 0) {
					if (models == null) {
						System.err.println("需手动设置映射值：" + field);
						return null;
					}
					boolean exists = false;
					String t = field.substring(0, field.lastIndexOf("."));
					for (String model : models) {
						if (t.equals(model)) {
							mappingName = field.substring(field.lastIndexOf(".") + 1);// *???
							exists = true;
							break;
						}
					}
					if (!exists) {
						System.err.println("需手动设置映射值：" + field);
						return null;
					}
				}
				field += " as " + mappingName;
			}
			list.add(field);
		}
		return StringUtils.join(list, ",");
	}

	/**
	 * 简化select new map，v1.0
	 * 
	 * @param fieldString
	 * @return
	 */
	public static String selectMap(String fieldString) {
		return selectMap(fieldString, null);
	}

	public static String selectMap(String fieldString, String[] models) {
		return "select new map(" + mapping(fieldString, models) + ") ";
	}

	// 数据库函数

	public static String isnull(String field, String nullvalue) {
		String retString = "";
		if (DBMS.equals(DBMS_MSSQL)) {
			retString = "isnull(" + field + "," + nullvalue + ")";
		} else if (DBMS.equals(DBMS_MYSQL)) {
			retString = "ifnull(" + field + "," + nullvalue + ")";
		}
		return retString;
	}

}
