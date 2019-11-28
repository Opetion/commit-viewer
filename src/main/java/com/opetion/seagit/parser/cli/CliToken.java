package com.opetion.seagit.parser.cli;

public enum CliToken {
	COMMIT("c-commit"), EMAIL("c-email"), AUTHOR("c-author"), DATE("c-date"), SUBJECT("c-subject"), NONE("");

	private String token;

	CliToken(String token) {
		this.token = token;
	}

	public static CliToken of(String value) {
		for (CliToken token : values()) {
			if (token.getToken().equals(value)) {
				return token;
			}
		}
		return NONE;
	}

	public String getToken() {
		return token;
	}

	public int length() {
		return getToken().length();
	}
}
