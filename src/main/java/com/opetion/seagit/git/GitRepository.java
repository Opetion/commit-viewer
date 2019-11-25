package com.opetion.seagit.git;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class GitRepository {
	private Integer id;
	private String url;
	private String folder;
	private RepositoryStatus status = RepositoryStatus.NOT_READY;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime lastSynchronization;

	public GitRepository() {
		// Empty jackson constructor
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GitRepository that = (GitRepository) o;
		return url.equals(that.url);
	}

	public LocalDateTime getLastSynchronization() {
		return lastSynchronization;
	}

	public void setLastSynchronization(LocalDateTime lastSynchronization) {
		this.lastSynchronization = lastSynchronization;
	}

	public RepositoryStatus getStatus() {
		return status;
	}

	public void setStatus(RepositoryStatus status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(url);
	}

	@Override
	public String toString() {
		return "GitRepository{" + "id=" + id + ", url='" + url + '\'' + '}';
	}
}
