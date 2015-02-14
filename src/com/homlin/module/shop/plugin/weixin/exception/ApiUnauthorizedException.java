package com.homlin.module.shop.plugin.weixin.exception;

public class ApiUnauthorizedException extends WeixinAPIException {

	public ApiUnauthorizedException(int errcode, String message) {
		super(errcode, message);
	}

}
