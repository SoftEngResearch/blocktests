package com.atlantbh.test.reporter.utils.services;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.StreamUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Services utils
 *
 * @author Kenan Klisura
 */
public final class ServicesUtils {
	public static final Sort CREATED_AT_DESC_SORT = new Sort(new Sort.Order(Sort.Direction.DESC, "createdAt"));
	public static final Sort CREATED_AT_ASC_SORT = new Sort(new Sort.Order(Sort.Direction.ASC, "createdAt"));

	public final static Collection<String> CASE_SENSITIVE_PROPERTIES = new ArrayList<>();

	private ServicesUtils() {
		// Empty ctor.
	}

	/**
	 * Create pageable request with default sort.
	 *
	 * @param page Page.
	 * @param defaultSort Default sort object.
	 * @param caseSensitiveSortProperties List of case sensitive properties or {@link #CASE_SENSITIVE_PROPERTIES} for
	 *                                       all properties to be case sensitive.
	 * @return Page object.
	 */
	public static PageRequest createPageRequest(Pageable page, Sort defaultSort,
												Collection<String> caseSensitiveSortProperties) {
		Sort sort = page.getSort();
		sort = sort == null ? defaultSort : sort.and(defaultSort);

		List<Sort.Order> ignoreCaseOrderList = Collections.emptyList();
		if (sort != null) {
			ignoreCaseOrderList = StreamUtils.createStreamFromIterator(sort.iterator())
					.map(order -> {
						// BLOCKTEST EVAL: https://github.com/ATLANTBH/owl/blob/f808feae5b021ce37e7a937ab03bf3327be386ad/src/main/java/com/atlantbh/test/reporter/utils/services/ServicesUtils.java#L46-L52
						blocktest().given(caseSensitiveSortProperties, new ArrayList<>()).given(order, new Sort.Order("foo")).checkEq(methodReturn.toString(), "foo: ASC");
						blocktest().given(caseSensitiveSortProperties, new ArrayList<>(Arrays.asList("hi", "bye"))).given(order, new Sort.Order("bye")).checkEq(methodReturn.toString(), "bye: ASC, ignoring case");
						if (caseSensitiveSortProperties == CASE_SENSITIVE_PROPERTIES ||
								caseSensitiveSortProperties.contains(order.getProperty())) {
							return order.ignoreCase();
						}
						return order;
					})
					.collect(Collectors.toList());
		}

		if (ignoreCaseOrderList.isEmpty()) {
			return new PageRequest(page.getPageNumber(), page.getPageSize());
		}
		return new PageRequest(page.getPageNumber(), page.getPageSize(), new Sort(ignoreCaseOrderList));
	}

	/**
	 * Create pageable request with default sort.
	 *
	 * @param page Page.
	 * @param defaultSort Default sort object.
	 * @return Page object.
	 */
	public static PageRequest createPageRequest(Pageable page, Sort defaultSort) {
		return createPageRequest(page, defaultSort, CASE_SENSITIVE_PROPERTIES);
	}

	/**
	 * Create pageable request with default sort.
	 *
	 * @param page Page.
	 * @return Page object.
	 */
	public static PageRequest createPageRequest(Pageable page) {
		return createPageRequest(page, null);
	}
}
