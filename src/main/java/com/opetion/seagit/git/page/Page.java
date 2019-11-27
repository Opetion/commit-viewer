package com.opetion.seagit.git.page;

import java.util.List;

public class Page<T> {
	private List<T> content;
	private PageMetadata metadata;

	private Page(List<T> content, PageMetadata metadata) {
		this.content = content;
		this.metadata = metadata;
	}

	public static <T> Page<T> of(List<T> content, PageMetadata metadata) {
		return new Page<>(content, metadata);
	}

	public List<T> getContent() {
		return content;
	}

	public PageMetadata getMetadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return "Page{" + "content=" + content + ", metadata=" + metadata + '}';
	}
}
