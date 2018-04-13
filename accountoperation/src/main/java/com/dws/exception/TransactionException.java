package com.dws.exception;

public class TransactionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 767656576565L;

	public TransactionException(String message) {
		super(message);
	}
}
