package com.opetion.seagit.git;

import java.time.LocalDateTime;
import java.util.Objects;

public class RefCommit {
	private String commitHash;
	private String author;
	private String email;
	private LocalDateTime date;
	private String subject;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RefCommit commit = (RefCommit) o;
		return Objects.equals(commitHash, commit.commitHash) && Objects.equals(author, commit.author)
				&& Objects.equals(email, commit.email) && Objects.equals(date, commit.date)
				&& Objects.equals(subject, commit.subject);
	}

	@Override
	public int hashCode() {
		return Objects.hash(commitHash, author, email, date, subject);
	}

	@Override
	public String toString() {
		return "RefCommit{" + "commitHash='" + commitHash + '\'' + ", author='" + author + '\'' + ", email='" + email
				+ '\'' + ", date=" + date + ", subject='" + subject + '\'' + '}';
	}
}
