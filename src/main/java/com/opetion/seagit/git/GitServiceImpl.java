package com.opetion.seagit.git;

import com.opetion.seagit.error.SeagitException;
import com.opetion.seagit.git.page.Page;
import com.opetion.seagit.git.page.PageMetadata;
import com.opetion.seagit.git.page.PageRequest;
import com.opetion.seagit.parser.ParserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitServiceImpl implements GitService {

	private final List<GitRepository> repositories = new ArrayList<>();
	// TODO: recreate list from startup

	private final ParserService parser;

	public GitServiceImpl(ParserService parser) {
		this.parser = parser;
	}

	@Override
	public Page<GitRepository> find(PageRequest pageRequest) {
		int size = pageRequest.getSize();
		int page = pageRequest.getPage();
		List<GitRepository> subset = repositories.stream().skip(size * page).limit(size).collect(Collectors.toList());

		boolean hasNext = repositories.size() > (size * page + size);

		return Page.of(subset, PageMetadata.of(page, size, hasNext));
	}

	@Override
	public GitRepository create(GitRepository repository) {
		synchronized (this) {
			if (repositories.contains(repository)) {
				// TODO: improve duplicates
				// even through List contains is actually really slow. I don't feel like this is
				// what matters the most
				// in this exercise. Sets would easily avoid this issue but I would lose the
				// order and pagination.
				return null;
			}
			repository.setId(repositories.size() + 1);
			repositories.add(repository);
		}

		parser.clone(repository);

		return repository;
	}

	@Override
	public Page<RefCommit> details(int id, PageRequest request) {
		if (id >= repositories.size()) {
			return Page.of(List.of(), PageMetadata.of(0, 0, false));
		}

		GitRepository repository = repositories.get(id);
		if (RepositoryStatus.READY != repository.getStatus()) {
			throw new SeagitException();
		}
		return parser.getCommits(repository, request);
	}
}
