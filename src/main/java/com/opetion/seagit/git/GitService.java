package com.opetion.seagit.git;

import com.opetion.seagit.git.page.Page;
import com.opetion.seagit.git.page.PageRequest;

public interface GitService {

	/**
	 * Get a Page of the already cloned repositories.
	 *
	 * @param pageRequest
	 *            Pagination request with size and page
	 * @return Page of Repositories
	 */
	Page<GitRepository> findRepositories(PageRequest pageRequest);

	/**
	 * Store and clone a git repository since it is asynchronous the actual
	 * repository won't be available immediately and no errors will be returned.
	 *
	 * @param repository
	 * @return created repository
	 */
	GitRepository createRepository(GitRepository repository);

	/**
	 * Get the commits of a git repository.
	 *
	 * @param id
	 *            id of the repository
	 * @param pageRequest
	 *            Pagination request with size and page
	 * @return Page of Commits
	 */
	Page<RefCommit> getCommits(int id, PageRequest pageRequest);
}
