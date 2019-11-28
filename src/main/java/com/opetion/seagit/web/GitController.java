package com.opetion.seagit.web;

import com.opetion.seagit.git.GitRepository;
import com.opetion.seagit.git.GitService;
import com.opetion.seagit.git.RefCommit;
import com.opetion.seagit.git.page.Page;
import com.opetion.seagit.git.page.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repositories")
public class GitController {

	private final GitService service;

	public GitController(GitService service) {
		this.service = service;
	}

	@GetMapping
	Page<GitRepository> getRepositories(@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "0") int page) {
		size = (size <= 0) ? 5 : size;
		page = (page <= 0) ? 0 : page;
		return service.findRepositories(new PageRequest(size, page));
	}

	@PostMapping
	GitRepository cloneRepository(@RequestBody GitRepository clone) {
		return service.createRepository(clone);
	}

	@GetMapping("/{id}/commits")
	Page<RefCommit> getCommits(@PathVariable int id, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "0") int page) {
		id = (id <= 0) ? 0 : id;
		size = (size <= 0) ? 5 : size;
		page = (page <= 0) ? 0 : page;

		return service.getCommits(id, new PageRequest(size, page));
	}

}
