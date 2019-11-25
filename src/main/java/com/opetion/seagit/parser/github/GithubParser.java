package com.opetion.seagit.parser.github;

import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.page.PageRequest;
import com.opetion.seagit.parser.general.GitParser;
import com.opetion.seagit.parser.general.ParserResult;

public class GithubParser implements GitParser {
	@Override
	public boolean process(GitRepository url) {
		return false;
	}

	@Override
	public ParserResult getCommits(GitRepository repository, PageRequest request) {
		return ParserResult.error();
	}
}
