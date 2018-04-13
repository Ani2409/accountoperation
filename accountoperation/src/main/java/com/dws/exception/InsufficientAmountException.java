package com.dws.exception;

public class InsufficientAmountException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 767656576565L;

	public InsufficientAmountException(String message) {
		super(message);
	}
}
