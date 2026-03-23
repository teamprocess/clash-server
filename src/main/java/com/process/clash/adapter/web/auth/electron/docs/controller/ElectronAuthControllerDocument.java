package com.process.clash.adapter.web.auth.electron.docs.controller;

import com.process.clash.adapter.web.auth.electron.docs.response.ElectronAuthExchangeResponseDocument;
import com.process.clash.adapter.web.auth.electron.docs.response.ElectronAuthRedirectUrlResponseDocument;
import com.process.clash.adapter.web.auth.electron.docs.response.ElectronAuthStartResponseDocument;
import com.process.clash.adapter.web.auth.electron.docs.response.ElectronAuthStartSignupResponseDocument;
import com.process.clash.adapter.web.auth.electron.docs.response.ElectronAuthUsernameCheckResponseDocument;
import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "Electron 인증 API", description = "Electron 앱용 로그인/회원가입 인증")
public interface ElectronAuthControllerDocument {

    @Operation(summary = "Electron 로그인 시작", description = "Electron 로그인 페이지 진입에 필요한 URL과 state를 발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "발급 성공",
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthStartResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "loginUrl": "https://auth.clash.kr/electron/sign-in?state=abc123&redirectUri=clashapp%3A%2F%2Fauth%2Fcallback",
                                        "state": "abc123"
                                      }
                                    }
                                    """)
                    ))
    })
    ApiResponse<ElectronAuthDto.StartResponse> start();

    @Operation(summary = "Electron 로그인", description = "아이디/비밀번호로 Electron 로그인 딥링크 URL을 발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 URL 발급 성공",
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthRedirectUrlResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "redirectUrl": "clashapp://auth/callback?code=one-time-code&state=abc123"
                                      }
                                    }
                                    """)
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 redirect URI 또는 state",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "invalid_redirect_uri", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_REDIRECT_URI",
                                                "message": "허용되지 않은 리다이렉트 URI입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "invalid_state", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_STATE",
                                                "message": "유효하지 않거나 만료된 state입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """)
                            }
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 올바르지 않음",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "INVALID_CREDENTIALS",
                                        "message": "아이디 또는 비밀번호가 올바르지 않습니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    ApiResponse<Map<String, String>> login(
            @RequestBody(description = "Electron 로그인 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthDto.LoginRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "username": "process123",
                                      "password": "qwer1234",
                                      "state": "abc123",
                                      "redirectUri": "clashapp://auth/callback"
                                    }
                                    """)
                    ))
            @Valid @org.springframework.web.bind.annotation.RequestBody ElectronAuthDto.LoginRequest req
    );

    @Operation(summary = "Electron 로그인 (리캡차 없음)", description = "리캡차 없이 Electron 로그인 딥링크 URL을 발급합니다. 내부 테스트용 엔드포인트입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 URL 발급 성공",
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthRedirectUrlResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "redirectUrl": "clashapp://auth/callback?code=one-time-code&state=abc123"
                                      }
                                    }
                                    """)
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 redirect URI 또는 state",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "invalid_redirect_uri", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_REDIRECT_URI",
                                                "message": "허용되지 않은 리다이렉트 URI입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "invalid_state", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_STATE",
                                                "message": "유효하지 않거나 만료된 state입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """)
                            }
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 올바르지 않음",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "INVALID_CREDENTIALS",
                                        "message": "아이디 또는 비밀번호가 올바르지 않습니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    ApiResponse<Map<String, String>> noRecaptchaLogin(
            @RequestBody(description = "Electron 로그인 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthDto.LoginRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "username": "process123",
                                      "password": "qwer1234",
                                      "state": "abc123",
                                      "redirectUri": "clashapp://auth/callback"
                                    }
                                    """)
                    ))
            @Valid @org.springframework.web.bind.annotation.RequestBody ElectronAuthDto.LoginRequest req
    );

    @Operation(summary = "Electron 로그인 코드 교환", description = "일회성 code와 state를 세션 로그인 정보로 교환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthExchangeResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로그인을 성공했습니다.",
                                      "data": {
                                        "userId": 1,
                                        "username": "process123",
                                        "role": "USER"
                                      }
                                    }
                                    """)
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 인증 코드 또는 state 불일치",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "invalid_auth_code", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_AUTH_CODE",
                                                "message": "유효하지 않거나 만료된 인증 코드입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "state_mismatch", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "STATE_MISMATCH",
                                                "message": "state가 일치하지 않습니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """)
                            }
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "인증 코드에 해당하는 사용자를 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "USER_NOT_FOUND_IN_AUTH",
                                        "message": "인증 코드에 해당하는 사용자를 찾을 수 없습니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    ApiResponse<ElectronAuthDto.ExchangeResponse> exchange(
            @RequestBody(description = "인증 코드 교환 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthDto.ExchangeRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "code": "one-time-code",
                                      "state": "abc123"
                                    }
                                    """)
                    ))
            @Valid @org.springframework.web.bind.annotation.RequestBody ElectronAuthDto.ExchangeRequest req,
            @Parameter(hidden = true) HttpServletRequest httpRequest
    );

    @Operation(summary = "Electron 회원가입 시작", description = "Electron 회원가입 페이지 진입에 필요한 URL과 state를 발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "발급 성공",
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthStartSignupResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "signupUrl": "https://auth.clash.kr/electron/sign-up?state=abc123&redirectUri=clashapp%3A%2F%2Fauth%2Fcallback",
                                        "state": "abc123"
                                      }
                                    }
                                    """)
                    ))
    })
    ApiResponse<ElectronAuthDto.StartSignupResponse> startSignup();

    @Operation(summary = "Electron 회원가입 요청", description = "회원가입 정보를 저장하고 이메일 인증 코드를 발송합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 요청 성공",
                    content = @Content(
                            schema = @Schema(implementation = SuccessMessageResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "회원가입 요청이 완료되었습니다. 이메일 인증을 진행해주세요."
                                    }
                                    """)
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 redirect URI 또는 state",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "invalid_redirect_uri", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_REDIRECT_URI",
                                                "message": "허용되지 않은 리다이렉트 URI입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "invalid_state", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_STATE",
                                                "message": "유효하지 않거나 만료된 state입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """)
                            }
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 유저네임",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "USERNAME_ALREADY_EXIST",
                                        "message": "이미 존재하는 username입니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    ApiResponse<Void> signup(
            @RequestBody(description = "Electron 회원가입 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthDto.SignupRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "username": "process123",
                                      "email": "process@example.com",
                                      "name": "프로세스",
                                      "password": "qwer1234",
                                      "state": "abc123",
                                      "redirectUri": "clashapp://auth/callback"
                                    }
                                    """)
                    ))
            @Valid @org.springframework.web.bind.annotation.RequestBody ElectronAuthDto.SignupRequest req
    );

    @Operation(summary = "Electron 이메일 인증 완료", description = "이메일 인증 코드를 검증하고 앱으로 돌아갈 딥링크 URL을 발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증 성공",
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthRedirectUrlResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "redirectUrl": "clashapp://auth/callback?code=one-time-code&state=abc123"
                                      }
                                    }
                                    """)
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 redirect URI, state, 또는 인증 코드",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "invalid_redirect_uri", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_REDIRECT_URI",
                                                "message": "허용되지 않은 리다이렉트 URI입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "invalid_state", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_STATE",
                                                "message": "유효하지 않거나 만료된 state입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "invalid_auth_code", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_AUTH_CODE",
                                                "message": "유효하지 않거나 만료된 인증 코드입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """)
                            }
                    ))
    })
    ApiResponse<Map<String, String>> verifyEmail(
            @RequestBody(description = "이메일 인증 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthDto.VerifyEmailRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "verificationCode": "123456",
                                      "state": "abc123",
                                      "redirectUri": "clashapp://auth/callback"
                                    }
                                    """)
                    ))
            @Valid @org.springframework.web.bind.annotation.RequestBody ElectronAuthDto.VerifyEmailRequest req
    );

    @Operation(summary = "Electron 유저네임 중복 확인", description = "회원가입 시 사용할 유저네임의 중복 여부를 확인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "확인 성공",
                    content = @Content(
                            schema = @Schema(implementation = ElectronAuthUsernameCheckResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "isDuplicate": false
                                      }
                                    }
                                    """)
                    ))
    })
    ApiResponse<Map<String, Boolean>> checkUsername(
            @Parameter(description = "확인할 유저네임", required = true, example = "process123")
            @RequestParam
            @NotBlank(message = "유저네임은 필수 입력값입니다.")
            @Size(min = 3, max = 20, message = "유저네임은 3~20자여야 합니다.")
            @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "유저네임은 영문, 숫자, _, -만 사용 가능합니다.")
            String username
    );
}
