package com.process.clash.adapter.web.auth.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 테스트 성공 응답")
public class TestAuthResponseDocument extends SuccessMessageResponseDocument {
}
