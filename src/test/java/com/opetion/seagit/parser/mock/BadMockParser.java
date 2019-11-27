package com.opetion.seagit.parser.mock;

import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.page.PageRequest;
import com.opetion.seagit.parser.general.GitParser;
import com.opetion.seagit.parser.general.ParserResult;

public class BadMockParser implements GitParser {
	@Override
	public boolean process(GitRepository repository) {
		return false;
	}

	@Override
	public ParserResult getCommits(GitRepository repository, PageRequest request) {
		return ParserResult.error();
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
