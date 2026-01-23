package com.process.clash.adapter.web.compete.my.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 활동 비교 응답")
public class CompareMyActivityResponseDoc extends SuccessResponseDoc {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "내 기록을 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "획득한 경험치", example = "1250.5")
        public Double earnedExp;

        @Schema(description = "학습 시간 (분)", example = "480.0")
        public Double studyTime;

        @Schema(description = "GitHub 기여도", example = "85.3")
        public Double gitHubAttribution;
    }
}