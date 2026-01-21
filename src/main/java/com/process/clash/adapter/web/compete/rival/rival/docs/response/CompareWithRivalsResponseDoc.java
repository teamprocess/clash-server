package com.process.clash.adapter.web.compete.rival.rival.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "라이벌과 비교 응답")
public class CompareWithRivalsResponseDoc extends SuccessMessageResponseDoc {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "라이벌과의 비교 정보를 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "비교 카테고리", example = "CLASH")
        public String category;

        @Schema(description = "비교 기간", example = "WEEKLY")
        public String period;

        @Schema(description = "전체 데이터 (본인 + 라이벌)")
        public List<TotalDataDoc> totalData;
    }

    public static class TotalDataDoc {
        @Schema(description = "사용자 ID", example = "1")
        public Long userId;

        @Schema(description = "이름", example = "홍길동")
        public String name;

        @Schema(description = "사용자명", example = "gildong")
        public String username;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/1.png")
        public String profileImage;

        @Schema(description = "총 활동 시간 (초)", example = "37800")
        public Long totalTime;

        @Schema(description = "순위", example = "2")
        public Integer rank;
    }
}