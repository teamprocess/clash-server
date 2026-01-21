package com.process.clash.adapter.web.compete.rival.rival.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "라이벌 후보 조회 응답")
public class GetAllAbleRivalsResponseDoc {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "라이벌 등록 가능한 유저 목록을 성공적으로 조회했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "라이벌 등록 가능한 유저 목록")
        public List<UserDoc> users;
    }

    public static class UserDoc {
        @Schema(description = "사용자 ID", example = "3")
        public Long id;

        @Schema(description = "이름", example = "성춘향")
        public String name;

        @Schema(description = "깃허브 아이디", example = "chunhyang")
        public String githubId;
    }
}