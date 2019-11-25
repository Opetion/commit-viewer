package com.opetion.seagit.parser.github.schema;

import java.util.List;

public class GithubCommit {
	private String sha;
	private GithubCommitDetails commit;
	private List<GithubCommit> parents;

	public GithubCommit() {
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public GithubCommitDetails getDetails() {
		return commit;
	}

	public void setCommit(GithubCommitDetails commit) {
		this.commit = commit;
	}

	public List<GithubCommit> getParents() {
		return parents;
	}

	public void setParents(List<GithubCommit> parents) {
		this.parents = parents;
	}
}
