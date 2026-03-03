package com.process.clash.adapter.web.auth.controller;

import com.process.clash.adapter.web.auth.docs.controller.AuthControllerDocument;
import com.process.clash.adapter.web.auth.dto.CheckDuplicateUsernameDto;
import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.auth.dto.SignUpDto;
import com.process.clash.adapter.web.auth.dto.VerifyEmailDto;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.common.util.AccessContextResolver;
import com.process.clash.application.common.data.AccessContext;
import com.process.clash.application.user.user.data.CheckDuplicateUsernameData;
import com.process.clash.application.user.user.data.VerifyEmailData;
import com.process.clash.application.user.user.data.SignInData;
import com.process.clash.application.user.user.port.in.*;
import com.process.clash.application.user.user.data.SignUpData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocument {

	private static final long VERIFICATION_CODE_EXPIRATION_MS = 5 * 60 * 1000L; // 인증 코드 만료: 5분

	private static final Map<String, String> REDIRECT_MAP = Map.of(
			"signin", "/api/auth/sign-in",
			"signup", "/api/auth/sign-up",
			"signout", "/api/auth/sign-out"
	);

	private final SignUpUseCase signUpUseCase;
	private final SignInUseCase signInUseCase;
	private final SignOutUseCase signOutUseCase;
	private final VerifyEmailUseCase verifyEmailUseCase;
	private final CheckDuplicatedUsernameUseCase checkDuplicatedUsernameUseCase;
	private final AccessContextResolver accessContextResolver;

	@PostMapping("/sign-up")
	public ApiResponse<Void> signUp(@Valid @RequestBody SignUpDto.Request request) {
		SignUpData.Command command = request.toCommand();
		String token = signUpUseCase.execute(command);
		ResponseCookie cookie = ResponseCookie.from("signup_token", token)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(VERIFICATION_CODE_EXPIRATION_MS)
				.sameSite("None") // TODO: Lax로 변경
				.build();
		return ApiResponse.success("회원가입 요청 / 이메일 인증 코드 발송이 완료되었습니다.", cookie);
	}

	@PostMapping("/sign-in")
	public ApiResponse<SignInDto.Response> signIn(
			@Valid @RequestBody SignInDto.Request request,
			HttpServletRequest httpRequest
	) {
		AccessContext context = accessContextResolver.extractAccessContext(httpRequest);
		SignInData.Command command = request.toCommand(context);
		SignInData.Result result = signInUseCase.execute(command);
		SignInDto.Response response = SignInDto.Response.fromResult(result);
		return ApiResponse.success(response, "로그인을 성공했습니다.");
	}

	@PostMapping("/sign-out")
	public ApiResponse<Void> signOut(HttpServletRequest httpRequest) {
		AccessContext context = accessContextResolver.extractAccessContext(httpRequest);
		signOutUseCase.execute(context);
		return ApiResponse.success("로그아웃 되었습니다.");
	}

	@GetMapping("/username-duplicate-check")
	public ApiResponse<CheckDuplicateUsernameDto.Response> checkUsername(
			@RequestParam
			@NotBlank(message = "유저네임은 필수 입력값입니다.")
			@Size(min = 3, max = 20, message = "유저네임은 3~20자여야 합니다.")
			@Pattern(
					regexp = "^[a-zA-Z0-9_-]+$",
					message = "유저네임은 영문, 숫자, _, -만 사용 가능합니다."
			)
			String username
	) {
		CheckDuplicateUsernameData.Command command = CheckDuplicateUsernameData.Command.fromString(username);
		boolean duplicate = checkDuplicatedUsernameUseCase.execute(command);
		CheckDuplicateUsernameDto.Response response = new CheckDuplicateUsernameDto.Response(duplicate);
		return ApiResponse.success(response);
	}

	@PostMapping("/verify-email")
	public ApiResponse<Void> verifyEmail(@CookieValue(name = "signup_token") String token, @Valid @RequestBody VerifyEmailDto.Request request) {

		VerifyEmailData.Command command = request.toCommand(token);
		verifyEmailUseCase.execute(command);

		ResponseCookie deleteCookie = ResponseCookie.from("signup_token", "")
				.maxAge(0)
				.path("/")
				.build();

		return ApiResponse.success("이메일 인증을 성공했습니다.", deleteCookie);
	}

	@PostMapping({"/{action:signin|signup|signout}"})
	public ResponseEntity<Void> handleRedirect(@PathVariable String action) {

		String newLocation = REDIRECT_MAP.get(action);

		return ResponseEntity
				.status(HttpStatus.PERMANENT_REDIRECT) // 308
				.header(HttpHeaders.LOCATION, newLocation)
				.build();
	}
}
