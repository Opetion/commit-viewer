package com.opetion.seagit.parser;

import com.opetion.seagit.error.SeagitException;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ParserService {
	private static final Logger logger = LoggerFactory.getLogger(ParserService.class);

	private final List<GitParser> parsers;

	public ParserService(List<GitParser> parsers) {
		this.parsers = new ArrayList<>(parsers);
		this.parsers.sort(Comparator.comparing(GitParser::getPriority));
	}

	@Async
	public void clone(GitRepository repository) {
		for (GitParser parser : parsers) {
			boolean processResult = parser.process(repository);

			if (processResult) {
				return;
			}
		}

		throw new SeagitException("Not possible to parse: " + repository.getUrl());
	}

	public Page<RefCommit> getCommits(GitRepository repository, PageRequest request) {
		Page<RefCommit> commits = Page.of(List.of(), PageMetadata.of(0, 0, false));
		for (GitParser parser : parsers) {
			ParserResult result = parser.getCommits(repository, request);

			if (ParserStatus.SUCCESSFUL == result.getStatus()) {
				commits = result.getCommitList();
				break;
			}
		}

		return commits;
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
