package com.process.clash.adapter.web.auth.controller;

import com.process.clash.adapter.web.auth.docs.controller.AuthControllerDocument;
import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.auth.dto.SignUpDto;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.application.common.data.AccessContext;
import com.process.clash.application.common.exception.exception.EndpointMovedException;
import com.process.clash.application.user.user.data.SignInData;
import com.process.clash.application.user.user.port.in.SignOutUseCase;
import com.process.clash.application.user.user.data.SignUpData;
import com.process.clash.application.user.user.port.in.SignInUseCase;
import com.process.clash.application.user.user.port.in.SignUpUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocument {

	private static final Map<String, String> REDIRECT_MAP = Map.of(
			"signin", "/api/auth/sign-in",
			"signup", "/api/auth/sign-up",
			"signout", "/api/auth/sign-out"
	);

	private final SignUpUseCase signUpUseCase;
	private final SignInUseCase signInUseCase;
	private final SignOutUseCase signOutUseCase;

	@PostMapping("/sign-up")
	public ApiResponse<Void> signUp(@Valid @RequestBody SignUpDto.Request request) {
		SignUpData.Command command = request.toCommand();
		signUpUseCase.execute(command);
		return ApiResponse.success("회원가입이 완료되었습니다.");
	}

	@PostMapping("/sign-in")
	public ApiResponse<SignInDto.Response> signIn(
			@Valid @RequestBody SignInDto.Request request,
			HttpServletRequest httpRequest
	) {
		AccessContext context = extractAccessContext(httpRequest);

		SignInData.Command command = new SignInData.Command(
				request.username(),
				request.password(),
				request.rememberMe(),
				context
		);
		SignInData.Result result = signInUseCase.execute(command);

		SignInDto.Response response = SignInDto.Response.fromResult(result);
		return ApiResponse.success(response, "로그인을 성공했습니다.");
	}

	@PostMapping("/sign-out")
	public ApiResponse<Void> signOut(HttpServletRequest httpRequest) {
		AccessContext context = extractAccessContext(httpRequest);
		signOutUseCase.execute(context);
		return ApiResponse.success("로그아웃 되었습니다.");
	}

	@PostMapping({"/{action:signin|signup|signout}"})
	public void handleRedirect(@PathVariable String action) {
		throw new EndpointMovedException(REDIRECT_MAP.get(action));
	}

	private AccessContext extractAccessContext(HttpServletRequest request) {
		String ip = extractIpAddress(request);
		String userAgent = request.getHeader("User-Agent");
		return AccessContext.of(ip, userAgent);
	}

	private String extractIpAddress(HttpServletRequest request) {
		String xff = request.getHeader("X-Forwarded-For");
		if (xff != null && !xff.isEmpty()) {
			return xff.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}
}
