package com.opetion.seagit.parser.cli;

import com.opetion.seagit.error.CliException;
import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.git.RepositoryStatus;
import com.opetion.seagit.git.page.PageRequest;
import com.opetion.seagit.git.page.PageUtils;
import com.opetion.seagit.parser.general.GitParser;
import com.opetion.seagit.parser.general.ParserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class CliParser implements GitParser {
	private static final Logger logger = LoggerFactory.getLogger(CliParser.class);
	private static final String WORKSPACE_FOLDER = "workspace/";
	private final String cliFormat = "--pretty=c-commit:%H\nc-author:%an\nc-email:%aE\nc-date:%aI\nc-subject:%s\n";
	private static final String PAGE_SIZE = "--max-count=";
	private static final String PAGE_NUMBER = "--skip=";

	@Override
	public boolean process(GitRepository repository) {
		logger.info("Start Cli Process for: {}", repository.getUrl());

		String uuid = UUID.randomUUID().toString();
		String folder = WORKSPACE_FOLDER + uuid;
		repository.setFolder(folder);

		boolean isCloned = clone(repository);
		repository.setStatus(RepositoryStatus.BROKEN);
		if (isCloned) {
			repository.setStatus(RepositoryStatus.READY);
		}
		return isCloned;
	}

	@Override
	public boolean preValidate() {
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("git", "--version");
		try {
			execute(builder).forEach(logger::debug);
		} catch (IOException | CliException e) {
			logger.error("Error executing git command. Please check if git is installed and on the Path environment");
			return false;
		}
		return true;

	}

	/**
	 * This method serves mostly as a way to allow to mock the execution of a
	 * process itself.
	 *
	 * @param builder
	 *            process to be executed
	 * @return cli process output
	 * @throws IOException
	 *             if something is wrong with the process itself. E.g. command
	 *             doesn't exist.
	 */
	Stream<String> execute(ProcessBuilder builder) throws IOException, CliException {
		Process process = builder.start();
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			logger.error("Unexpected process start", e);
		}
		if (process.exitValue() != 0) {
			throw new CliException(process.exitValue());
		}
		return new BufferedReader(new InputStreamReader(process.getInputStream())).lines();
	}

	private boolean clone(GitRepository repository) {
		String url = repository.getUrl();
		String folder = repository.getFolder();
		logger.info("Start Clone for: {}", url);

		ProcessBuilder builder = new ProcessBuilder();

		// TODO: should I use git clone --depth? for a faster first response and make a
		// background process to unshallow?

		builder.command("git", "clone", url, folder);

		try {
			execute(builder).forEach(logger::info);
		} catch (IOException | CliException e) {
			logger.error("Processing Error in {}", repository.getUrl(), e);
			return false;
		}

		return Files.exists(Path.of(folder));
	}

	@Override
	public ParserResult getCommits(GitRepository repository, PageRequest request) {
		String folder = repository.getFolder();
		logger.info("Start Log for: {}", folder);
		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(new File(folder));
		int page = request.getPage();
		int size = request.getSize();
		String skip = PAGE_NUMBER + page * size;
		String limit = PAGE_SIZE + size;
		builder.command("git", "log", cliFormat, skip, limit);

		List<RefCommit> commits;

		try {
			Process process = builder.start();
			InputStreamReader in = new InputStreamReader(process.getInputStream());

			try (var bufferedReader = new BufferedReader(in)) {
				Stream<String> lines = bufferedReader.lines();
				commits = parse(lines);
			}

		} catch (IOException e) {
			logger.error("Process Error", e);
			return ParserResult.error();
		}

		int pageSize = request.getSize();
		int pageNumber = request.getPage();

		return ParserResult.successful(PageUtils.build(commits, pageNumber, pageSize));
	}

	List<RefCommit> parse(Stream<String> text) {
		List<RefCommit> commits = new ArrayList<>();
		RefCommit commit = new RefCommit();
		Iterator<String> iterator = text.iterator();

		while (iterator.hasNext()) {
			String line = iterator.next();
			CliToken token = parseToken(line);

			switch (token) {
				case COMMIT :
					commit.setCommitHash(line.substring(token.length() + 1));
					break;
				case AUTHOR :
					commit.setAuthor(line.substring(token.length() + 1));
					break;
				case EMAIL :
					commit.setEmail(line.substring(token.length() + 1));
					break;
				case DATE :
					commit.setDate(OffsetDateTime.parse(line.substring(token.length() + 1)).toLocalDateTime());
					break;
				case SUBJECT :
					commit.setSubject(line.substring(token.length() + 1));
					break;
				case NONE :
					commits.add(commit);
					commit = new RefCommit();
					break;
			}

		}
		if (commit.getCommitHash() != null) {
			commits.add(commit);
		}
		return commits;
	}

	/**
	 * Parse a line to find a token
	 *
	 * @param line
	 *            line to match
	 * @return CliToken with None by default
	 */
	private CliToken parseToken(String line) {
		CliToken token = CliToken.NONE;
		int index = line.indexOf(":");
		if (index != -1) {
			token = CliToken.of(line.substring(0, index));
		}
		return token;
	}

	@Override
	public int getPriority() {
		return Integer.MAX_VALUE;
	}
}
