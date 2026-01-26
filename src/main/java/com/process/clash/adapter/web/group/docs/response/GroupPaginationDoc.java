package com.process.clash.adapter.web.group.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "페이지네이션 정보")
public class GroupPaginationDoc {

    @Schema(description = "현재 페이지", example = "1")
    public Integer currentPage;

    @Schema(description = "전체 페이지 수", example = "3")
    public Integer totalPages;

    @Schema(description = "전체 항목 수", example = "25")
    public Long totalItems;

    @Schema(description = "페이지 크기", example = "10")
    public Integer pageSize;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    public Boolean hasNext;

    @Schema(description = "이전 페이지 존재 여부", example = "false")
    public Boolean hasPrevious;
}
