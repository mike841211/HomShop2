package com.homlin.module.shop.plugin.weixin.exception;

public class WeixinAPIException extends Exception {

	private int errcode;

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public WeixinAPIException(int errcode, String message) {
		super(message);
		this.errcode = errcode;
	}

}
