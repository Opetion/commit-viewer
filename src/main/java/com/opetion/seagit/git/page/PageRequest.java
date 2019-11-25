package com.opetion.seagit.git.page;

public class PageRequest {
	private final int size;
	private final int page;

	public PageRequest(int size, int page) {
		this.size = size;
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public int getPage() {
		return page;
	}

	@Override
	public String toString() {
		return "PageRequest{" + "size=" + size + ", page=" + page + '}';
	}
}
