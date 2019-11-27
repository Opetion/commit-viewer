package com.opetion.seagit.parser.cli;

import com.opetion.seagit.error.CliException;
import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.parser.util.FileParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CliParserTest {

	private final CliParser cliParser = spy(CliParser.class);

	@Test
	void checkParser() throws IOException {
		Stream<String> textFile = FileParser.parse("parser/gitlog-sample.txt");
		List<RefCommit> commits = cliParser.parse(textFile);
		assertEquals(12, commits.size());
		assertEquals(commits.get(0), expectedLastCommit());
		assertEquals(commits.get(11), expectedFirstCommit());
	}

	@Test
	void checkValidator() throws IOException, CliException {
		doReturn(Stream.generate(() -> "Mock Test").limit(1)).when(cliParser).execute(any());
		cliParser.preValidate();
		assertEquals(1, 1);
	}

	@Test
	void checkValidatorTransformsException() throws IOException, CliException {
		doThrow(new IOException()).when(cliParser).execute(any());
		assertFalse(cliParser::preValidate);
	}

	private RefCommit expectedFirstCommit() {
		RefCommit expected = new RefCommit();
		expected.setCommitHash("7e4caa2591e6ef3bdadc9e6aa981b0741aa2b08b");
		expected.setAuthor("Joao Costa");
		expected.setEmail("jfc.costa91@gmail.com");
		expected.setDate(OffsetDateTime.parse("2019-11-22T00:06:59+00:00").toLocalDateTime());
		expected.setSubject(":tada: initial commit");
		return expected;
	}

	private RefCommit expectedLastCommit() {
		RefCommit expected = new RefCommit();
		expected.setCommitHash("a9e16d83b6a5f772e5df25c59d6d8ca92a17b40a");
		expected.setAuthor("Opetion");
		expected.setEmail("jfc.costa91@gmail.com");
		expected.setDate(OffsetDateTime.parse("2019-11-27T00:28:35+00:00").toLocalDateTime());
		expected.setSubject(":white_check_mark: add parser service tests");
		return expected;
	}

}
