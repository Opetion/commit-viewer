package com.opetion.seagit.parser.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.git.page.PageRequest;
import com.opetion.seagit.parser.general.ParserResult;
import com.opetion.seagit.parser.util.FileParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GithubIntegrationTest {

	@Autowired
	private GithubParser parser;

	public static final WireMockServer wireMockServer = new WireMockServer();

	@BeforeAll
	static void createWireMockStub() throws IOException {
		wireMockServer.start();

		String responseBody = FileParser.parse("parser/github-sample.json").collect(Collectors.joining());

		wireMockServer.stubFor(get(anyUrl()).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(responseBody)));
	}

	@AfterAll
	static void stopWiremock() {
		wireMockServer.stop();
	}
	@Test
	void checkRequest() {
		GitRepository repository = new GitRepository();
		repository.setUrl("https://github.com/Opetion/commit-viewer.git");
		ParserResult commits = parser.getCommits(repository, new PageRequest(2, 0));
		List<RefCommit> commitList = commits.getCommitList().getContent();
		assertEquals(2, commitList.size());
	}

}
