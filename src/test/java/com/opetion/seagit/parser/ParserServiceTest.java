package com.opetion.seagit.parser;

import com.opetion.seagit.error.SeagitException;
import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.git.page.Page;
import com.opetion.seagit.git.page.PageMetadata;
import com.opetion.seagit.git.page.PageRequest;
import com.opetion.seagit.parser.general.GitParser;
import com.opetion.seagit.parser.mock.BadMockParser;
import com.opetion.seagit.parser.mock.MockParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserServiceTest {
	private static final int PAGE_SIZE = 1;
	private static final int PAGE_NUMBER = 1;

	private static final List<GitParser> parserList = new ArrayList<>();
	private static ParserService service;

	@BeforeAll
	static void setup() {
		parserList.add(new MockParser());
		parserList.add(new BadMockParser());
		service = new ParserService(parserList);
	}

	@Test
	void checkBadClone() {
		GitRepository repository = new GitRepository();
		repository.setUrl("BAD");
		assertThrows(SeagitException.class, () -> service.clone(repository));
	}

	@Test
	void checkClone() {
		GitRepository repository = new GitRepository();
		repository.setUrl("OK");
		service.clone(repository);
	}

	@Test
	void checkParsersHasContent() {
		GitRepository mockRepository = new GitRepository();
		Page<RefCommit> commits = service.getCommits(mockRepository, new PageRequest(PAGE_SIZE, PAGE_NUMBER));
		assertEquals(PAGE_SIZE, commits.getContent().size());
		PageMetadata metadata = commits.getMetadata();
		assertEquals(PAGE_NUMBER, metadata.getPageNumber());
		assertEquals(PAGE_SIZE, metadata.getPageSize());
		assertTrue(metadata.isHasNext());
	}

}
