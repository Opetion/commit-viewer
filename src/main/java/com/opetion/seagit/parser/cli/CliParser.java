package com.opetion.seagit.parser.cli;

import com.opetion.seagit.git.RefCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class CliParser {
	private static final Logger logger = LoggerFactory.getLogger(CliParser.class);
	private static final String BASE_FOLDER = "workspace/";
	private static final String PRETTY_FORMAT = "--pretty=c-commit:%H\nc-author:%an\nc-email:%aE\nc-date:%aI\nc-subject:%s\nc-body:%b\n";

	public void process(String url) {
		logger.info("Start Cli Process for: {}", url);

		String uuid = UUID.randomUUID().toString();
		String folder = BASE_FOLDER + uuid;

		clone(url, folder);
		log(folder);
		cleanup(folder);
	}

	public void validate() {
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("git", "--version");
		try {
			execute(builder).forEach(logger::info);
		} catch (IOException e) {
			logger.error("Error executing git command. Please check if git is installed and on the Path environment");
			throw new SeagitException();
		}

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
	Stream<String> execute(ProcessBuilder builder) throws IOException {
		Process process = builder.start();
		return new BufferedReader(new InputStreamReader(process.getInputStream())).lines();
	}

	void clone(String url, String folder) {
		logger.info("Start Clone for: {}", url);
		ProcessBuilder builder = new ProcessBuilder();

		// TODO: should I use git clone --depth? for a faster first response and make a
		// background process to unshallow?

		// TODO: Risk! Git Server down.
		builder.command("git", "clone", url, folder);

		// TODO: make this a background process?
		try {
			execute(builder).forEach(System.out::println);
		} catch (IOException e) {
			logger.error("Process Error", e);
		}
	}

	List<RefCommit> log(String folder) {
		logger.info("Start Log for: {}", folder);
		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(new File(folder));
		builder.command("git", "log", PRETTY_FORMAT);

		List<RefCommit> commits = new ArrayList<>();
		try {
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			RefCommit commit = new RefCommit();
			commits.add(commit);
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
						commit = new RefCommit();
						commits.add(commit);
						x = 0;
						break;

				}
			}
		} catch (IOException e) {
			logger.error("Process Error", e);
		}
		return commits;
	}

	/**
	 * Recursive delete the folders to cleanup Solution of:
	 * https://www.baeldung.com/java-delete-directory
	 *
	 * @param folder
	 */
	void cleanup(String folder) {
		try {
			Files.walk(Paths.get(folder)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
