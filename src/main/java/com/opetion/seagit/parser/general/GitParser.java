package com.opetion.seagit.parser.general;

import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.page.PageRequest;

public interface GitParser {

	/**
	 * Validate if the parser has everything required to startup.
	 *
	 * @return true if the parser is ready to be used; false otherwise
	 */
	default boolean preValidate() {
		return true;
	}

	/**
	 * Process a request to parse git commands.
	 *
	 * @param repository
	 *            git repository
	 * @return list of requested commits
	 */
	boolean process(GitRepository repository);

	/**
	 * Get a page of commits from the repository
	 *
	 * @param repository
	 *            git repository
	 * @param request
	 *            page request with size and number of page
	 * @return Result of the command with the commits if successful
	 */
	ParserResult getCommits(GitRepository repository, PageRequest request);

	/**
	 * This defines the priority of the parsers, the higher the number the later its
	 * called
	 *
	 * @return int priority
	 */
	default int getPriority() {
		return 0;
	}
}
