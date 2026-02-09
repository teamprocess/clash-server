package com.process.clash.adapter.web.compete.rival.rival.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "내 라이벌 조회 응답")
public class GetMyRivalActingResponseDocument extends SuccessMessageResponseDocument {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "라이벌의 현재 상태 정보를 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "내 라이벌 목록")
        public List<MyRivalDoc> myRivals;
    }

    public static class MyRivalDoc {
        @Schema(description = "이름", example = "이몽룡")
        public String name;

        @Schema(description = "사용자명", example = "mongryong")
        public String username;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/2.png")
        public String profileImage;

        @Schema(description = "활동 시간 (초)", example = "9000")
        public Long activeTime;

        @Schema(description = "사용 중인 앱", example = "CLASH")
        public String usingApp;

        @Schema(description = "현재 상태", example = "ONLINE")
        public String status;
    }
}
