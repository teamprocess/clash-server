package com.process.clash.adapter.web.controller.docs.controller;

import com.process.clash.adapter.web.controller.docs.response.HealthResponseDocument;
import com.process.clash.adapter.web.controller.docs.response.PingResponseDocument;
import com.process.clash.adapter.web.controller.dto.HealthResponse;
import com.process.clash.adapter.web.controller.dto.PingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Health Check API", description = "서비스 상태 점검")
public interface HealthCheckControllerDocument {

    @Operation(summary = "Ping 체크", description = "애플리케이션이 응답 가능한 상태인지 간단히 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ping 성공",
                    content = @Content(
                            schema = @Schema(implementation = PingResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "PONG"
                                    }
                                    """)
                    ))
    })
    ResponseEntity<PingResponse> ping();

    @Operation(summary = "상세 헬스 체크", description = "DB, Redis, 애플리케이션 컴포넌트 상태를 포함한 전체 헬스 상태를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 상태 정상",
                    content = @Content(
                            schema = @Schema(implementation = HealthResponseDocument.class),
                            examples = {
                                    @ExampleObject(name = "healthy", value = """
                                            {
                                              "status": "UP",
                                              "db": "UP",
                                              "redis": "UP",
                                              "app": "UP"
                                            }
                                            """),
                                    @ExampleObject(name = "error", value = """
                                            {
                                              "status": "ERROR",
                                              "message": "Health endpoint access failed"
                                            }
                                            """)
                            }
                    )),
            @ApiResponse(responseCode = "503", description = "일부 컴포넌트 장애",
                    content = @Content(
                            schema = @Schema(implementation = HealthResponseDocument.class),
                            examples = @ExampleObject(name = "down", value = """
                                    {
                                      "status": "DOWN",
                                      "db": "UP",
                                      "redis": "DOWN",
                                      "app": "UP"
                                    }
                                    """)
                    ))
    })
    ResponseEntity<HealthResponse> health();
}
