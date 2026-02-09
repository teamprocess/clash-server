package com.process.clash.adapter.web.roadmap.section.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "로드맵 수정 요청")
public class UpdateSectionRequestDocument {

    @Schema(description = "제목")
    public String title;

    @Schema(description = "카테고리 ID")
    public Long categoryId;

    @Schema(description = "설명")
    public String description;

    @Schema(description = "정렬 순서")
    public Integer orderIndex;

    @Schema(description = "핵심 포인트")
    public List<String> keyPoints;

    @Schema(description = "선행 로드맵 ID 목록")
    public List<Long> prerequisiteSectionIds;
}
