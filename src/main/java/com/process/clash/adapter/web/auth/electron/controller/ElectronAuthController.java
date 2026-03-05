package com.process.clash.adapter.web.auth.electron.controller;

import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.common.util.AccessContextResolver;
import com.process.clash.application.auth.electron.port.in.ElectronLoginUseCase;
import com.process.clash.application.auth.electron.service.ElectronAuthService;
import com.process.clash.application.common.data.AccessContext;
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
	private final ElectronLoginUseCase electronSignInService;
	private final ElectronLoginUseCase noRecapchaElectronSignInService;
	private final AccessContextResolver accessContextResolver;

	@PostMapping("/sign-in/start")
	public ApiResponse<ElectronAuthDto.StartResponse> start() {
		ElectronAuthService.StartResult result = electronAuthService.start();
		return ApiResponse.success(new ElectronAuthDto.StartResponse(result.loginUrl(), result.state()));
	}

	// fetch API는 custom scheme(clashapp://)으로 302 리다이렉트할 수 없음
	// 200 + JSON으로 deep link URL을 반환하여 클라이언트가 명시적으로 이동하도록 함
	@PostMapping("/sign-in")
	public ApiResponse<Map<String, String>> login(@Valid @RequestBody ElectronAuthDto.LoginRequest req) {
		String redirectUrl = electronSignInService.execute(
				req.username(), req.password(), req.state(), req.redirectUri());
		return ApiResponse.success(Map.of("redirectUrl", redirectUrl));
	}

	@PostMapping("/no-recapcha-sign-in")
	public ApiResponse<Map<String, String>> noRecapchaLogin(@Valid @RequestBody ElectronAuthDto.LoginRequest req) {
		String redirectUrl = noRecapchaElectronSignInService.execute(
				req.username(), req.password(), req.state(), req.redirectUri());
		return ApiResponse.success(Map.of("redirectUrl", redirectUrl));
	}

	@PostMapping("/sign-in/exchange")
	public ApiResponse<ElectronAuthDto.ExchangeResponse> exchange(
			@Valid @RequestBody ElectronAuthDto.ExchangeRequest req,
			HttpServletRequest httpRequest
	) {
		AccessContext accessContext = accessContextResolver.extractAccessContext(httpRequest);
		ElectronAuthService.ExchangeResult result = electronAuthService.exchange(
				req.code(), req.state(), accessContext);
		return ApiResponse.success(
				new ElectronAuthDto.ExchangeResponse(result.userId(), result.username(), result.role()),
				"로그인을 성공했습니다.");
	}

	// ========== 회원가입 관련 엔드포인트 ==========

	@PostMapping("/sign-up/start")
	public ApiResponse<ElectronAuthDto.StartSignupResponse> startSignup() {
		ElectronAuthService.StartSignupResult result = electronAuthService.startSignup();
		return ApiResponse.success(new ElectronAuthDto.StartSignupResponse(result.signupUrl(), result.state()));
	}

	@PostMapping("/sign-up")
	public ApiResponse<Void> signup(@Valid @RequestBody ElectronAuthDto.SignupRequest req) {
		electronAuthService.signupAndSendEmail(
				req.username(), req.email(), req.name(), req.password(), req.state(), req.redirectUri());
		return ApiResponse.success("회원가입 요청이 완료되었습니다. 이메일 인증을 진행해주세요.");
	}

	@PostMapping("/sign-up/verify-email")
	public ApiResponse<Map<String, String>> verifyEmail(@Valid @RequestBody ElectronAuthDto.VerifyEmailRequest req) {
		String redirectUrl = electronAuthService.verifyEmailAndRedirect(
				req.verificationCode(), req.state(), req.redirectUri());
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
