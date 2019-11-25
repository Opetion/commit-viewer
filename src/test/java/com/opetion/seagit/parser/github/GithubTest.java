package com.opetion.seagit.parser.github;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class GithubTest {
	private final GithubService service = mock(GithubService.class);
	private final GithubParser parser = new GithubParser(service);

	@Test
	void checkParseHTTPs() {
		String url = "https://github.com/Opetion/commit-viewer.git";
		String[] parsed = parser.parseUrl(url);
		assertEquals(2, parsed.length);
		assertEquals("Opetion", parsed[0]);
		assertEquals("commit-viewer", parsed[1]);
	}

	@Test
	void checkParseSSH() {
		String url = "git@github.com:Opetion/commit-viewer.git";
		String[] parsed = parser.parseUrl(url);
		assertEquals(2, parsed.length);
		assertEquals("Opetion", parsed[0]);
		assertEquals("commit-viewer", parsed[1]);
	}

	@Test
	void checkInvalidDomain() {
		String url = "https://gitlab.com/Opetion/commit-viewer.git";
		String[] parsed = parser.parseUrl(url);
		assertNull(parsed);
	}

}
