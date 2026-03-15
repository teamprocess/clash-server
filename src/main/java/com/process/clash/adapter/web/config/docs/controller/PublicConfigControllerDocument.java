package com.process.clash.adapter.web.config.docs.controller;

import com.process.clash.adapter.web.config.dto.PublicConfigDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "공개 설정 API", description = "클라이언트에 필요한 공개 설정 정보")
public interface PublicConfigControllerDocument {

    @Operation(summary = "공개 설정 조회", description = "reCAPTCHA 사이트 키 등 클라이언트에서 필요한 공개 설정 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "recaptcha": {
                                        "siteKey": "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI"
                                      }
                                    }
                                    """)
                    ))
    })
    PublicConfigDto.Response getPublicConfig();
}
