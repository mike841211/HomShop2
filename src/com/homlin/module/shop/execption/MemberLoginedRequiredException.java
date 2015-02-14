package com.homlin.module.shop.execption;

/**
 * 要求会员登入
 * 
 * @author Administrator
 * 
 */
public class MemberLoginedRequiredException extends Exception {

	private static final long serialVersionUID = 1L;

	public MemberLoginedRequiredException(String message) {
		super(message);
	}

	public MemberLoginedRequiredException(String message, Throwable cause) {
		super(message, cause);
	}

}
