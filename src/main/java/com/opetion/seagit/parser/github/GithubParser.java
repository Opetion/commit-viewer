package com.opetion.seagit.parser.github;

import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.git.RepositoryStatus;
import com.opetion.seagit.git.page.PageRequest;
import com.opetion.seagit.git.page.PageUtils;
import com.opetion.seagit.parser.general.GitParser;
import com.opetion.seagit.parser.general.ParserResult;
import com.opetion.seagit.parser.github.schema.GithubCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GithubParser implements GitParser {
	private static final Logger logger = LoggerFactory.getLogger(GithubParser.class);

	private static final String URL_MATCH = "github.com";
	private static final String END_URL_MATCH = ".git";

	private GithubService service;

	public GithubParser(GithubService service) {
		this.service = service;
	}

	@Override
	public boolean process(GitRepository repository) {
		String[] project = parseUrl(repository.getUrl());
		if (project != null) {
			repository.setStatus(RepositoryStatus.READY);
		}
		return false;
	}

	@Override
	public ParserResult getCommits(GitRepository repository, PageRequest request) {
		String[] project = parseUrl(repository.getUrl());
		if (project == null) {
			return ParserResult.error();
		}

		Call<List<GithubCommit>> listCall = service.listCommits(project[0], project[1], request.getPage(),
				request.getSize());

		List<GithubCommit> result;
		try {
			Response<List<GithubCommit>> response = listCall.execute();
			result = response.body();
		} catch (IOException e) {
			logger.error("Error on Github connection", e);
			return ParserResult.error();
		}
		if (result == null || result.isEmpty()) {
			return ParserResult.error();
		}

		int pageSize = request.getSize();
		int pageNumber = request.getPage();

		List<RefCommit> commits = result.stream().map(this::map).collect(Collectors.toList());

		return ParserResult.successful(PageUtils.build(commits, pageNumber, pageSize));
	}

	/**
	 * Parse the url (HTTPS/SSH) to retrieve the project owner and the project name.
	 *
	 * @param url
	 *            git clone url
	 * @return String[0] - project owner; [1] - project name
	 */
	String[] parseUrl(String url) {
		String repo = url.substring(url.indexOf(URL_MATCH) + URL_MATCH.length() + 1);
		String[] ownerProject = repo.split("/");
		if (ownerProject.length != 2) {
			return null;
		}

		if (ownerProject[1].endsWith(END_URL_MATCH)) {
			ownerProject[1] = ownerProject[1].substring(0, ownerProject[1].length() - END_URL_MATCH.length());
		}

		return ownerProject;
	}

	private RefCommit map(GithubCommit commit) {
		RefCommit result = new RefCommit();
		result.setAuthor(commit.getDetails().getAuthorName());
		result.setEmail(commit.getDetails().getAuthorEmail());
		result.setDate(commit.getDetails().getAuthorDate());
		result.setSubject(commit.getDetails().getMessage());
		result.setCommitHash(commit.getSha());
		return result;
	}

}
