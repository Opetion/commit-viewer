package com.opetion.seagit.error;

public class CliException extends Exception {
	private final int status;

	public CliException(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
