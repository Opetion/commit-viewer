package com.opetion.seagit;

import com.opetion.seagit.parser.ParserRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppStart {
	private static final Logger logger = LoggerFactory.getLogger(AppStart.class);
	private static final String TEST_URL = "https://gitlab.com/RobertZenz/jLuaScript.git";

	public static void main(String[] args) {
		// TODO: spotless plugin
		if (args.length != 1) {
			logger.error("Requires 1 argument of a git url");
			System.exit(-1);
		}
		ParserRouter router = new ParserRouter();
		router.validate();
		router.route(args[0]);
	}
}
