package com.process.clash.adapter.web.compete.rival.rival.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "라이벌 신청 요청")
public class ApplyRivalRequestDoc {

    @Schema(description = "라이벌로 추가할 사용자 ID 목록")
    public List<IdDoc> ids;

    public static class IdDoc {
        @Schema(description = "사용자 ID", example = "3")
        public Long id;
    }
}