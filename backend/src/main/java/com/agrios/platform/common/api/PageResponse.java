package com.agrios.platform.common.api;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(List<T> content, PageMetadata page) {
    public static <T> PageResponse<T> from(Page<T> source) {
        return new PageResponse<>(
                source.getContent(),
                new PageMetadata(source.getNumber(), source.getSize(),
                        source.getTotalElements(), source.getTotalPages()));
    }

    public record PageMetadata(int number, int size, long totalElements, int totalPages) {}
}
