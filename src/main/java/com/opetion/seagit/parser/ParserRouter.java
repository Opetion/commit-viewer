package com.opetion.seagit.parser;

import com.opetion.seagit.parser.cli.CliParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserRouter {
	private static final Logger logger = LoggerFactory.getLogger(ParserRouter.class);
	private CliParser parser = new CliParser();

	public void validate() {
		parser.validate();
	}

	public void route(String url) {

		// TODO: Create Process to use the API vs CLI
		parser.process(url);
		// TODO: Fallback of API

	}

}
