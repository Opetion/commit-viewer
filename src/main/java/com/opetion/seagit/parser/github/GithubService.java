package com.opetion.seagit.parser.github;

import com.opetion.seagit.parser.github.schema.GithubCommit;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface GithubService {

	@GET("/repos/{owner}/{repo}/commits")
	Call<List<GithubCommit>> listCommits(@Path("owner") String user, @Path("repo") String repo, @Query("page") int page,
			@Query("per_page") int size);
}
