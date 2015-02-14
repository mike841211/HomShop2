package com.homlin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

public class PropertyHelper {

	// public static final String CLASSPATH = ClassLoader.getSystemResource("").toString().substring(6);
	public static final String CLASSPATH = Thread.currentThread().getContextClassLoader().getResource("").toString().substring(6);

	public static Properties getPropertyFile(String propertyFile) {
		Properties p = new Properties();
		try {
			File file = new File(CLASSPATH + propertyFile);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			InputStream is = new FileInputStream(file);
			p.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public static String getProperty(String propertyFile, String key) {
		Properties p = getPropertyFile(propertyFile);
		return p.getProperty(key);
	}

	public static void setProperty(String propertyFile, String key, String value) {
		try {
			Properties p = getPropertyFile(propertyFile);
			p.put(key, value);
			p.store(new FileOutputStream(CLASSPATH + propertyFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 测试代码
	public static void main(String[] args) throws InterruptedException {
		String propertyFile = "sync-timestamp-log.properties";
		System.err.println(getProperty(propertyFile, "jd"));
		setProperty(propertyFile, "jd", String.valueOf(Calendar.getInstance().getTimeInMillis()));
		System.err.println(getProperty(propertyFile, "jd"));

		setProperty(propertyFile, "jd2", "adfa=ada=fsdf");
		System.err.println(getProperty(propertyFile, "jd2"));
		setProperty(propertyFile, "jd3", "11111");
		setProperty(propertyFile, "jd4", "11111");
		setProperty(propertyFile, "jd5", "11111");
		setProperty(propertyFile, "jd6", "11111");
		// Thread.sleep(1000 * 5);
		setProperty(propertyFile, "jd7", "11111"); //
		setProperty(propertyFile, "jd8", "11111"); //
		System.out.println("操作完成");
	}

}