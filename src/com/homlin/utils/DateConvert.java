package com.homlin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;

public class DateConvert implements Converter {
	@SuppressWarnings("unchecked")
	public Object convert(@SuppressWarnings("rawtypes") Class arg0, Object arg1) {
		String p = (String) arg1;
		if (p == null || p.trim().length() == 0) {
			return null;
		}
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.parse(p.trim());
		} catch (Exception e) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				return df.parse(p.trim());
			} catch (ParseException ex) {
				return null;
			}
		}
	}
}
