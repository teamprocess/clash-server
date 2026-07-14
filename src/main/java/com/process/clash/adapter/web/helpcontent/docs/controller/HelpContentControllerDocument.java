package com.process.clash.adapter.web.helpcontent.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;

@Tag(name = "도움말 API", description = "클라이언트 도움말 조회")
public interface HelpContentControllerDocument {

    @Operation(summary = "도움말 조회", description = "If-None-Match 헤더가 현재 ETag와 같으면 304를, 다르면 도움말 전체 텍스트와 ETag를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = "쿠키는 상점에서 사용 할 수 있어요!\\n\\n로드맵 챕터 클리어: 100 쿠키")
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "304", description = "변경 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "도움말을 찾을 수 없음")
    })
    ResponseEntity<String> getHelpContent(@PathVariable String key, WebRequest request);
}
