package com.rainpoetry.common.utils.error;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */

public class CommonException extends RuntimeException{

	private final static long serialVersionUID = 1L;

	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonException(String message) {
		super(message);
	}

	public CommonException(Throwable cause) {
		super(cause);
	}

	public CommonException() {
		super();
	}
}
