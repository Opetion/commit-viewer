package com.opetion.seagit.parser.mock;

import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.git.page.Page;
import com.opetion.seagit.git.page.PageMetadata;
import com.opetion.seagit.git.page.PageRequest;
import com.opetion.seagit.parser.general.GitParser;
import com.opetion.seagit.parser.general.ParserResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockParser implements GitParser {
	@Override
	public boolean process(GitRepository repository) {
		return "OK".equals(repository.getUrl());
	}

	@Override
	public ParserResult getCommits(GitRepository repository, PageRequest request) {
		int size = request.getSize();
		int page = request.getPage();
		List<RefCommit> commits = new ArrayList<>();
		for (int x = 0; x <= size; x++) {
			commits.add(buildMockCommit());
		}
		List<RefCommit> actualContent = commits.subList(0, Math.min(commits.size(), size));
		Page<RefCommit> pageContent = Page.of(actualContent, PageMetadata.of(commits, page, size));
		return ParserResult.successful(pageContent);
	}

	private RefCommit buildMockCommit() {
		RefCommit commit = new RefCommit();
		commit.setEmail("mock@mock.com");
		commit.setDate(LocalDateTime.now());
		commit.setAuthor("Mock");
		return commit;
	}

	@Override
	public int getPriority() {
		return Integer.MAX_VALUE;
	}
}
