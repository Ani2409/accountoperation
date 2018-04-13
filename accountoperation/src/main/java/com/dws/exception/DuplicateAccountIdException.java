package com.dws.exception;

public class DuplicateAccountIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 56576576565L;

	public DuplicateAccountIdException(String message) {
		super(message);
	}
}
