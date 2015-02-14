package com.homlin.module.shop.plugin.weixin.exception;

public class AccessTokenErrorException extends WeixinAPIException {

	public AccessTokenErrorException(int errcode, String errmsg) {
		super(errcode, errmsg);
	}

}
