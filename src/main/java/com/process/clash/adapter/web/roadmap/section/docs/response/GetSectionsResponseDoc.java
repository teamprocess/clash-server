package com.process.clash.adapter.web.roadmap.section.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.roadmap.section.dto.GetSectionsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로드맵 목록 조회 응답")
public class GetSectionsResponseDoc extends SuccessResponseDoc {

    @Schema(description = "로드맵 목록", implementation = GetSectionsDto.Response.class)
    public GetSectionsDto.Response data;
}
