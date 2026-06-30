package com.example.springweb.dto;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Transport wrapper exposing a page of results together with pagination metadata
 * (total items, total pages, current page).
 */
public record PagedResponse<T>(
        List<T> content,
        PageMetadata metadata
) {

    public record PageMetadata(
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean first,
            boolean last
    ) {
    }

    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                new PageMetadata(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isFirst(),
                        page.isLast()
                )
        );
    }
}
