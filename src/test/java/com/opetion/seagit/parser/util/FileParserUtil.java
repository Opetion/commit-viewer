package com.opetion.seagit.parser.util;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileParserUtil {
	public static Stream<String> parse(String path) throws IOException {
		URL resource = FileParserUtil.class.getClassLoader().getResource(path);
		File file = new File(resource.getFile());
		return Files.readAllLines(Path.of(file.toURI())).stream();
	}
}
