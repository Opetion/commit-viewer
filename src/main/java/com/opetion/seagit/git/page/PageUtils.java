package com.opetion.seagit.git.page;

import java.util.List;

public class PageUtils {

	/**
	 * Build a Page based on the content provided, if the data provided is bigger
	 * that the supposed page size it will create a sublist with the provided
	 * pageSize excluding the later items that exceed the capacity.
	 *
	 * @param content
	 *            items to be paginated
	 * @param pageNumber
	 *            number of current page
	 * @param pageSize
	 *            number of items that should be on the page
	 * @return page of the provided <T>
	 */
	public static <T> Page<T> build(List<T> content, int pageNumber, int pageSize) {
		List<T> actualContent = content.subList(0, Math.min(pageSize, content.size()));
		return Page.of(actualContent, PageMetadata.of(content, pageNumber, pageSize));
	}
}
