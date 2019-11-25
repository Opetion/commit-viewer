package com.opetion.seagit.parser.github.schema;

import java.time.LocalDateTime;

public class GithubCommitDetails {

	private GithubAuthor author;
	private String message;

	public GithubCommitDetails() {
	}

	public void setAuthor(GithubAuthor author) {
		this.author = author;
	}

	public String getAuthorName() {
		return author.getName();
	}

	public String getAuthorEmail() {
		return author.getEmail();
	}

	public LocalDateTime getAuthorDate() {
		return author.getDate();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
