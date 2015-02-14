package com.homlin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CookieUtil {

	public static void addCookie(HttpServletResponse response, String name, String value, Integer maxAge, String path, String domain,
			Boolean secure) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
			value = URLEncoder.encode(value, "UTF-8");
			Cookie localCookie = new Cookie(name, value);
			if (maxAge != null)
				localCookie.setMaxAge(maxAge.intValue());
			if (StringUtils.isNotEmpty(path))
				localCookie.setPath(path);
			if (StringUtils.isNotEmpty(domain))
				localCookie.setDomain(domain);
			if (secure != null)
				localCookie.setSecure(secure.booleanValue());
			response.addCookie(localCookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void addCookie(HttpServletResponse response, String name, String value, Integer maxAge) {
		addCookie(response, name, value, maxAge, "/", null, null);
	}

	public static void addCookie(HttpServletResponse response, String name, String value) {
		addCookie(response, name, value, null);
	}

	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] arrayOfCookie1 = request.getCookies();
		if (arrayOfCookie1 != null) {
			try {
				name = URLEncoder.encode(name, "UTF-8");
				for (Cookie localCookie : arrayOfCookie1) {
					if (name.equals(localCookie.getName())) {
						return URLDecoder.decode(localCookie.getValue(), "UTF-8");
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void removeCookie(HttpServletResponse response, String name) {
		addCookie(response, name, "", 0);
	}

}
