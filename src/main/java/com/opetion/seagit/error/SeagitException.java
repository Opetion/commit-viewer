package com.opetion.seagit.error;

public class SeagitException extends RuntimeException {
	private String message;

	public SeagitException(String message) {
		super(message);
	}
}
