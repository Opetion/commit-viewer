package com.opetion.seagit.parser.github.schema;

import java.time.LocalDateTime;

class GithubAuthor {
	private String name;
	private String email;
	private LocalDateTime date;

	public GithubAuthor() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}
