package com.process.clash.adapter.web.auth.electron.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Electron 유저네임 중복 확인 응답")
public class ElectronAuthUsernameCheckResponseDocument extends SuccessResponseDocument {

    @Schema(description = "중복 확인 데이터")
    public UsernameCheckData data;

    public static class UsernameCheckData {
        @Schema(description = "중복 여부", example = "false")
        public Boolean isDuplicate;
    }
}
