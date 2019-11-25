package com.opetion.seagit.parser.cli;

import com.opetion.seagit.error.CliException;
import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.git.RepositoryStatus;
import com.opetion.seagit.git.page.PageRequest;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class CliParser implements GitParser {
	private static final Logger logger = LoggerFactory.getLogger(CliParser.class);
	private static final String WORKSPACE_FOLDER = "workspace/";
	private static final String PRETTY_FORMAT = "--pretty=c-commit:%H\nc-author:%an\nc-email:%aE\nc-date:%aI\nc-subject:%s\nc-body:%b\n";
	private static final String PAGE_SIZE = "--max-count=";
	private static final String PAGE_NUMBER = "--skip=";

	public CliParser() {
	}

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
		builder.command("git", "log", PRETTY_FORMAT, skip, limit);

		List<RefCommit> commits = new ArrayList<>();
		try {
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			RefCommit commit = new RefCommit();
			int x = 0;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("c-")) {
					x++;
				}
				// TODO: Proof of concept. Find a better way to parse
				switch (x) {
					case 0 :
						break;
					case 1 :
						commit.setCommitHash(line.substring("c-commit:".length()));
						break;
					case 2 :
						commit.setAuthor(line.substring("c-author:".length()));
						break;
					case 3 :
						commit.setEmail(line.substring("c-email:".length()));
						break;
					case 4 :
						commit.setDate(OffsetDateTime.parse(line.substring("c-date:".length())).toLocalDateTime());
						break;
					case 5 :
						commit.setSubject(line.substring("c-subject:".length()));
						break;
					case 6 :
						// TODO: body
						commits.add(commit);
						commit = new RefCommit();
						x = 0;
						break;

				}
			}
		} catch (IOException e) {
			logger.error("Process Error", e);
			return ParserResult.error();
		}
		return ParserResult.successful(commits);
	}
}
