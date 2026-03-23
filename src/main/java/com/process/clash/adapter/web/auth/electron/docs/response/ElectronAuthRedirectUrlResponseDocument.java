package com.process.clash.adapter.web.auth.electron.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Electron 리다이렉트 URL 응답")
public class ElectronAuthRedirectUrlResponseDocument extends SuccessResponseDocument {

    @Schema(description = "리다이렉트 URL 데이터")
    public RedirectUrlData data;

    public static class RedirectUrlData {
        @Schema(description = "앱 딥링크 URL", example = "clashapp://auth/callback?code=one-time-code&state=abc123")
        public String redirectUrl;
    }
}
