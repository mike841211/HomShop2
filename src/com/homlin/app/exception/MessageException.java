package com.homlin.app.exception;

/**
 * 用于抛出提示消息
 * 
 * @author Administrator
 * 
 */
public class MessageException extends Exception {

	private static final long serialVersionUID = 7195677918989637304L;

	public MessageException(String message) {
		super(message);
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
