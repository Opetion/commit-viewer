package com.opetion.seagit.git;

import java.time.LocalDateTime;

public class RefCommit {
	private String commitHash;
	private String author;
	private String email;
	private LocalDateTime date;
	private String subject;
	private String body;

	public RefCommit() {
	}

	public String getCommitHash() {
		return commitHash;
	}

	public void setCommitHash(String commitHash) {
		this.commitHash = commitHash;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "RefCommit{" + "commitHash='" + commitHash + '\'' + '}';
	}
}
