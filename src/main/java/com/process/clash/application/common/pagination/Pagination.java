package com.process.clash.application.common.pagination;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Pagination")
public record Pagination(
    @Schema(description = "현재 페이지", example = "1")
    Integer currentPage,
    @Schema(description = "전체 페이지 수", example = "3")
    Integer totalPages,
    @Schema(description = "전체 항목 수", example = "25")
    Long totalItems,
    @Schema(description = "페이지 크기", example = "10")
    Integer pageSize,
    @Schema(description = "다음 페이지 존재 여부", example = "true")
    Boolean hasNext,
    @Schema(description = "이전 페이지 존재 여부", example = "false")
    Boolean hasPrevious
) {
    public static Pagination from(Integer currentPage, Integer pageSize, Long totalItems) {
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        boolean hasNext = currentPage < totalPages;
        boolean hasPrevious = currentPage > 1;

        return new Pagination(
            currentPage,
            totalPages,
            totalItems,
            pageSize,
            hasNext,
            hasPrevious
        );
    }
}
