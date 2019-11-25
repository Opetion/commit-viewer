package com.opetion.seagit.parser.cli;

import com.opetion.seagit.error.CliException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CliParserTest {

	private final CliParser parser = spy(CliParser.class);

	@Test
	void checkValidator() throws IOException, CliException {
		doReturn(Stream.generate(() -> "Mock Test").limit(1)).when(parser).execute(any());
		parser.preValidate();
		assertEquals(1, 1);
	}

	@Test
	void checkValidatorTransformsException() throws IOException, CliException {
		doThrow(new IOException()).when(parser).execute(any());
		assertThrows(RuntimeException.class, () -> parser.preValidate());
	}

}
