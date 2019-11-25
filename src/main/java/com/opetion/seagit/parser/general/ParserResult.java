package com.opetion.seagit.parser.general;

import com.opetion.seagit.git.RefCommit;

import java.util.List;

public class ParserResult {
	private final ParserStatus status;
	private final List<RefCommit> commitList;

	private ParserResult(ParserStatus status) {
		this.status = status;
		this.commitList = List.of();
	}

	private ParserResult(ParserStatus status, List<RefCommit> commits) {
		this.status = status;
		this.commitList = commits;
	}

	public static ParserResult error() {
		return new ParserResult(ParserStatus.ERROR);
	}

	public static ParserResult successful(List<RefCommit> commits) {
		return new ParserResult(ParserStatus.SUCCESSFUL, commits);
	}

	public ParserStatus getStatus() {
		return status;
	}

	public List<RefCommit> getCommitList() {
		return commitList;
	}

}
