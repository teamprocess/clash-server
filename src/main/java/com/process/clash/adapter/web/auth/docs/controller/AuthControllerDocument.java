package com.process.clash.adapter.web.auth.docs.controller;

import com.process.clash.adapter.web.auth.docs.request.SignInRequestDocument;
import com.process.clash.adapter.web.auth.docs.response.SignInResponseDocument;
import com.process.clash.adapter.web.auth.docs.response.SignOutResponseDocument;
import com.process.clash.adapter.web.auth.docs.response.SignUpResponseDocument;
import com.process.clash.adapter.web.auth.dto.CheckDuplicateUsernameDto;
import com.process.clash.adapter.web.auth.dto.ResetPasswordDto;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "인증 API", description = "회원가입/로그인/로그아웃")
public interface AuthControllerDocument {

    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 이름으로 회원가입합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignUpResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "회원가입 요청 / 이메일 인증 코드 발송이 완료되었습니다."
                                    }
                                    """)
                    )
            )
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> signUp(
            @RequestBody(description = "회원가입 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = SignUpDto.Request.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "username": "gildong123",
                                      "email": "gildong@example.com",
                                      "password": "qwer1234",
                                      "name": "홍길동"
                                    }
                                    """)
                    ))
            @Valid SignUpDto.Request request
    );

    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignInResponseDocument.class),
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
                            schema = @Schema(implementation = SignInRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "username": "process",
                                      "password": "qwer1234",
                                      "rememberMe": true
                                    }
                                    """)
                    ))
            @Valid SignInDto.Request request,
            @Parameter(hidden = true) HttpServletRequest httpRequest
    );

    @Operation(summary = "로그인 (리캡차 없음)", description = "리캡차 없이 아이디/비밀번호로 로그인합니다. 개발 환경에서만 허용되는 내부 테스트용 엔드포인트입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignInResponseDocument.class),
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
    com.process.clash.adapter.web.common.ApiResponse<SignInDto.Response> noRecaptchaSignIn(
            @RequestBody(description = "로그인 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = SignInRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "username": "process",
                                      "password": "qwer1234",
                                      "rememberMe": true
                                    }
                                    """)
                    ))
            @Valid SignInDto.Request request,
            @Parameter(hidden = true) HttpServletRequest httpRequest
    );

    @Operation(summary = "로그아웃", description = "현재 세션을 로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignOutResponseDocument.class),
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

    @Operation(summary = "아이디 중복 확인", description = "회원가입 시 아이디 중복 여부를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "확인 완료",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "duplicated": false
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CheckDuplicateUsernameDto.Response> checkUsername(
            @Parameter(description = "확인할 아이디 (영문, 숫자, _, - 가능, 3~20자)", required = true, example = "gildong123")
            @RequestParam
            @NotBlank(message = "유저네임은 필수 입력값입니다.")
            @Size(min = 3, max = 20, message = "유저네임은 3~20자여야 합니다.")
            @Pattern(
                    regexp = "^[a-zA-Z0-9_-]+$",
                    message = "유저네임은 영문, 숫자, _, -만 사용 가능합니다."
            )
            String username
    );

    @Operation(summary = "비밀번호 재설정 이메일 발송", description = "입력한 이메일로 비밀번호 재설정 링크를 발송합니다. 가입된 이메일이 아니어도 동일한 응답을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발송 완료 (이메일 존재 여부 무관)",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "비밀번호 재설정 이메일을 발송했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> sendPasswordReset(
            @RequestBody(description = "비밀번호 재설정 이메일 발송 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ResetPasswordDto.SendRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "email": "gildong@example.com"
                                    }
                                    """)
                    ))
            @Valid ResetPasswordDto.SendRequest request
    );

    @Operation(summary = "비밀번호 재설정 토큰 유효성 검사", description = "재설정 링크의 토큰이 유효한지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유효한 토큰",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "유효한 비밀번호 초기화 토큰입니다."
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "400", description = "유효하지 않거나 만료된 토큰",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "INVALID_PASSWORD_RESET_TOKEN",
                                        "message": "유효하지 않거나 만료된 비밀번호 재설정 링크입니다."
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> validatePasswordResetToken(
            @Parameter(description = "재설정 토큰", required = true, example = "a1b2c3d4e5f6...")
            @RequestParam String token
    );

    @Operation(summary = "비밀번호 변경", description = "토큰을 검증하고 새 비밀번호로 변경합니다. 토큰은 1회 사용 후 만료됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "비밀번호가 변경되었습니다.",
                                      "data": {
                                        "state": "eionbosdb",
                                        "redirectUri": "clashapp://auth"
                                      }
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "400", description = "유효하지 않거나 만료된 토큰",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "INVALID_PASSWORD_RESET_TOKEN",
                                        "message": "유효하지 않거나 만료된 비밀번호 재설정 링크입니다."
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<ResetPasswordDto.ResetResponse> resetPassword(
            @RequestBody(description = "비밀번호 변경 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ResetPasswordDto.ResetRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "token": "a1b2c3d4e5f6...",
                                      "newPassword": "newPassword123"
                                    }
                                    """)
                    ))
            @Valid ResetPasswordDto.ResetRequest request
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
                            schema = @Schema(implementation = SignInResponseDocument.class),
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
            @CookieValue(name = "signup_token", required = true) String token,
            @RequestBody(description = "이메일 인증 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = VerifyEmailDto.Request.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "code": "123456"
                                    }
                                    """)
                    ))
            @Valid VerifyEmailDto.Request request
    );
}
