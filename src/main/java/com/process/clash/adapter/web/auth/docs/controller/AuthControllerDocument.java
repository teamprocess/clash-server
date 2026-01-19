package com.process.clash.adapter.web.auth.docs.controller;

import com.process.clash.adapter.web.auth.docs.request.SignInRequestDoc;
import com.process.clash.adapter.web.auth.docs.request.SignUpRequestDoc;
import com.process.clash.adapter.web.auth.docs.response.SignInResponseDoc;
import com.process.clash.adapter.web.auth.docs.response.SignOutResponseDoc;
import com.process.clash.adapter.web.auth.docs.response.SignUpResponseDoc;
import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.auth.dto.SignUpDto;
import com.process.clash.adapter.web.auth.dto.VerifyEmailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "인증 API", description = "회원가입/로그인/로그아웃")
public interface AuthControllerDocument {

    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 이름으로 회원가입합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignUpResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "회원가입이 완료되었습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> signUp(
            @RequestBody(description = "회원가입 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = SignUpRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "username": "process",
                                      "password": "qwer1234",
                                      "name": "홍길동",
                                      "email": "process@example.com"
                                    }
                                    """)
                    ))
            SignUpDto.Request request
    );

    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignInResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로그인을 성공했습니다.",
                                      "data": {
                                        "id": 1,
                                        "username": "process123",
                                        "name": "홍길동"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<SignInDto.Response> signIn(
            @RequestBody(description = "로그인 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = SignInRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "username": "process",
                                      "password": "qwer1234",
                                      "rememberMe": true
                                    }
                                    """)
                    ))
            SignInDto.Request request,
            @Parameter(hidden = true) HttpServletRequest httpRequest
    );

    @Operation(summary = "로그아웃", description = "현재 세션을 로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignOutResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로그아웃 되었습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> signOut(
            @Parameter(hidden = true) HttpServletRequest httpRequest
    );

    @Operation(summary = "이전 엔드포인트 리다이렉트", description = "기존 엔드포인트 요청을 신규 경로로 이동합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "308", description = "신규 엔드포인트로 이동")
    })
    ResponseEntity<Void> handleRedirect(
            @Parameter(description = "이전 액션", example = "signin", required = true)
            @PathVariable String action
    );

    @Operation(summary = "이메일 인증", description = "회원가입 후 발송된 6자리 코드로 계정을 활성화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignInResponseDoc.class),
                            examples = @ExampleObject(value = """
                                {
                                  "success": true,
                                  "message": "이메일 인증을 성공했습니다."
                                }
                                """)
                    )),
            @ApiResponse(responseCode = "400", description = "인증 실패 (코드 불일치 또는 만료)",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                {
                                  "success": false,
                                  "message": "인증 코드가 일치하지 않거나 만료되었습니다."
                                }
                                """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> verifyEmail(
            @RequestBody(description = "이메일 인증 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = VerifyEmailDto.Request.class),
                            examples = @ExampleObject(value = """
                                {
                                  "email": "process@example.com",
                                  "code": "123456"
                                }
                                """)
                    ))
            VerifyEmailDto.Request request
    );
}
