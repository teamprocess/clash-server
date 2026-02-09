package com.process.clash.adapter.web.major.docs.request;

import com.process.clash.domain.common.enums.Major;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전공 성향 검사 제출 요청")
public class MajorTestSubmitRequestDocument {

    @Schema(description = "선택한 전공")
    public Major major;
}
