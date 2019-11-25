package com.opetion.seagit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opetion.seagit.parser.github.GithubService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class GithubConfig {

	private static final String API_URL = "https://api.github.com";

	@Bean
	GithubService getService(ObjectMapper mapper) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL)
				.addConverterFactory(JacksonConverterFactory.create(mapper)).build();

		return retrofit.create(GithubService.class);
	}
}
