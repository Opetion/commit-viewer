package com.opetion.seagit.parser.general;

import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.git.page.Page;
import com.opetion.seagit.git.page.PageMetadata;

import java.util.List;

public class ParserResult {
	private final ParserStatus status;
	private final Page<RefCommit> commitList;

	private ParserResult(ParserStatus status) {
		this.status = status;
		this.commitList = Page.of(List.of(), PageMetadata.of(0, 0, false));
	}

	private ParserResult(ParserStatus status, Page<RefCommit> commits) {
		this.status = status;
		this.commitList = commits;
	}

	public static ParserResult error() {
		return new ParserResult(ParserStatus.ERROR);
	}

	public static ParserResult successful(Page<RefCommit> commits) {
		return new ParserResult(ParserStatus.SUCCESSFUL, commits);
	}

	public ParserStatus getStatus() {
		return status;
	}

	public Page<RefCommit> getCommitList() {
		return commitList;
	}

}
