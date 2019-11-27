package com.opetion.seagit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opetion.seagit.parser.github.GithubService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class GithubConfig {

	@Value("${github.url}")
	private String API_URL;

	@Bean
	GithubService getService(ObjectMapper mapper) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL)
				.addConverterFactory(JacksonConverterFactory.create(mapper)).build();

		return retrofit.create(GithubService.class);
	}
}
