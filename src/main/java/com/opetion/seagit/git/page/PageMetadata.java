package com.opetion.seagit.git.page;

public class PageMetadata {
	private final boolean hasNext;
	private final int pageNumber;
	private final int pageSize;

	private PageMetadata(int pageNumber, int pageSize, boolean hasNext) {
		this.hasNext = hasNext;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	public static PageMetadata of(int pageNumber, int pageSize, boolean hasNext) {
		return new PageMetadata(pageNumber, pageSize, hasNext);
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	@Override
	public String toString() {
		return "PageMetadata{" + "hasNext=" + hasNext + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + '}';
	}
}
