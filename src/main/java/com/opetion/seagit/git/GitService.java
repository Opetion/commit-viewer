package com.opetion.seagit.git;

import com.opetion.seagit.git.page.Page;
import com.opetion.seagit.git.page.PageRequest;

public interface GitService {

	Page<GitRepository> find(PageRequest pageRequest);

	GitRepository create(GitRepository repository);

	Page<RefCommit> details(int id, PageRequest request);
}
