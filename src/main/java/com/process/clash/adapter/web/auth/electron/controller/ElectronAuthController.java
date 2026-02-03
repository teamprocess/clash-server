package com.process.clash.adapter.web.auth.electron.controller;

import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.adapter.web.auth.electron.service.ElectronAuthService;
import com.process.clash.adapter.web.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/auth/electron")
@RequiredArgsConstructor
public class ElectronAuthController {

	private final ElectronAuthService electronAuthService;

	@PostMapping("/sign-in/start")
	public ApiResponse<ElectronAuthDto.StartResponse> start() {
		return ApiResponse.success(electronAuthService.start());
	}

	@PostMapping("/sign-in")
	public ApiResponse<Map<String, String>> login(@Valid @RequestBody ElectronAuthDto.LoginRequest req) {
		String redirectUrl = electronAuthService.loginAndRedirect(req);
		// fetch API는 custom scheme(clashapp://)으로 302 리다이렉트할 수 없음
		// 200 + JSON으로 deep link URL을 반환하여 클라이언트가 명시적으로 이동하도록 함
		return ApiResponse.success(Map.of("redirectUrl", redirectUrl));
    }

	@PostMapping("/sign-in/exchange")
	public ApiResponse<ElectronAuthDto.ExchangeResponse> exchange(
			@Valid @RequestBody ElectronAuthDto.ExchangeRequest req,
			HttpServletRequest httpRequest
	) {
		ElectronAuthDto.ExchangeResponse response = electronAuthService.exchange(req, httpRequest);
		return ApiResponse.success(response, "로그인을 성공했습니다.");
	}

	// ========== 회원가입 관련 엔드포인트 ==========

	@PostMapping("/sign-up/start")
	public ApiResponse<ElectronAuthDto.StartSignupResponse> startSignup() {
		return ApiResponse.success(electronAuthService.startSignup());
	}

	@PostMapping("/sign-up")
	public ApiResponse<Void> signup(@Valid @RequestBody ElectronAuthDto.SignupRequest req) {
		electronAuthService.signupAndSendEmail(req);
		return ApiResponse.success("회원가입 요청이 완료되었습니다. 이메일 인증을 진행해주세요.");
	}

	@PostMapping("/sign-up/verify-email")
	public ApiResponse<Map<String, String>> verifyEmail(@Valid @RequestBody ElectronAuthDto.VerifyEmailRequest req) {
		String redirectUrl = electronAuthService.verifyEmailAndRedirect(req);
		return ApiResponse.success(Map.of("redirectUrl", redirectUrl));
	}

	@GetMapping("/sign-up/username-check")
	public ApiResponse<Map<String, Boolean>> checkUsername(
			@RequestParam
			@NotBlank(message = "유저네임은 필수 입력값입니다.")
			@Size(min = 3, max = 20, message = "유저네임은 3~20자여야 합니다.")
			@Pattern(
					regexp = "^[a-zA-Z0-9_-]+$",
					message = "유저네임은 영문, 숫자, _, -만 사용 가능합니다."
			)
			String username
	) {
		boolean isDuplicate = electronAuthService.checkUsernameDuplicate(username);
		return ApiResponse.success(Map.of("isDuplicate", isDuplicate));
	}
}
