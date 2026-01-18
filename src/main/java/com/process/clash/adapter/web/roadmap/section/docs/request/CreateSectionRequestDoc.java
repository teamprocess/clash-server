package com.process.clash.adapter.web.roadmap.section.docs.request;

import com.process.clash.domain.common.enums.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "로드맵 생성 요청")
public class CreateSectionRequestDoc {

    @Schema(description = "전공")
    public Major major;

    @Schema(description = "제목")
    public String title;

    @Schema(description = "카테고리 ID")
    public Long categoryId;

    @Schema(description = "설명")
    public String description;

    @Schema(description = "핵심 포인트")
    public List<String> keyPoints;
}
