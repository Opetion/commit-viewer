package com.opetion.seagit.git.page;

import java.util.List;

public class PageUtils {

	public static <T> Page<T> build(List<T> content, int pageNumber, int pageSize) {
		List<T> actualContent = content.subList(0, Math.min(pageSize, content.size()));
		return Page.of(actualContent, PageMetadata.of(content, pageNumber, pageSize));
	}
}
