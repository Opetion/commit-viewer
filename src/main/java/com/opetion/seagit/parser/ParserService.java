package com.opetion.seagit.parser;

import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.git.page.Page;
import com.opetion.seagit.git.page.PageMetadata;
import com.opetion.seagit.git.page.PageRequest;
import com.opetion.seagit.parser.general.GitParser;
import com.opetion.seagit.parser.general.ParserResult;
import com.opetion.seagit.parser.general.ParserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

@Service
public class ParserService {
	private static final Logger logger = LoggerFactory.getLogger(ParserService.class);

	private final List<GitParser> parsers;

	public ParserService(List<GitParser> parsers) {
		this.parsers = parsers;
		parsers.sort(Comparator.comparing(GitParser::getPriority));
	}

	@Async
	public void clone(GitRepository repository) {
		for (GitParser parser : parsers) {
			boolean processResult = parser.process(repository);

			if (processResult) {
				return;
			}
		}

		logger.error("Not possible to parse: {}", repository.getUrl());
	}

	public Page<RefCommit> getCommits(GitRepository repository, PageRequest request) {
		List<RefCommit> commits = List.of();
		for (GitParser parser : parsers) {
			ParserResult result = parser.getCommits(repository, request);

			if (ParserStatus.SUCCESSFUL == result.getStatus()) {
				commits = result.getCommitList();
				break;
			}
		}

		int pageSize = request.getSize();
		int pageNumber = request.getPage();

		List<RefCommit> actualContent = commits.subList(0, Math.min(pageSize, commits.size()));
		return Page.of(actualContent, PageMetadata.of(pageNumber, pageSize, commits.size() > pageSize));
	}

	@PostConstruct
	void validate() {
		for (GitParser parser : parsers) {
			if (!parser.preValidate()) {
				continue;
			}
			logger.info(parser.getClass().getSimpleName() + " Started");
		}
	}

}
