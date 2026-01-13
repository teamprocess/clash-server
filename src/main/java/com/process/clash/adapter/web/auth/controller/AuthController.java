package com.process.clash.adapter.web.auth.controller;

import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.auth.dto.SignUpDto;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.application.user.data.SignInData;
import com.process.clash.application.user.data.SignUpData;
import com.process.clash.application.user.port.in.SignInUseCase;
import com.process.clash.application.user.port.in.SignUpUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final SignUpUseCase signUpUseCase;
	private final SignInUseCase signInUseCase;

	@PostMapping("/signup")
	public ApiResponse<Void> signUp(@Valid @RequestBody SignUpDto.Request request) {
		SignUpData.Command command = SignUpData.Command.fromRequest(request);
		signUpUseCase.execute(command);
		return ApiResponse.success("회원가입이 완료되었습니다.");
	}

	@PostMapping("/signin")
	public ApiResponse<SignInDto.Response> signIn(
			@Valid @RequestBody SignInDto.Request request,
			HttpServletRequest httpRequest
	) {
		SignInData.Command command = new SignInData.Command(request.username(), request.password());
		SignInData.Result result = signInUseCase.execute(command);

		// 인증 토큰을 생성
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				result.username(),
				null,
				List.of(new SimpleGrantedAuthority("ROLE_USER"))
		);

		// 빈 보안 컨텍스트 가져오기
		SecurityContext context = SecurityContextHolder.getContext();
		// 컨텍스트에 토큰 저장
		context.setAuthentication(token);

		HttpSession oldSession = httpRequest.getSession(false);
		if (oldSession != null) {
			oldSession.invalidate(); // 기존 세션 무효화
		}
		// 요청에 대한 세션 가져오기(없으면 생성, true 부분)
		HttpSession session = httpRequest.getSession(true);
		// 세션에 보안 정보(context) 저장
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

		SignInDto.Response response = SignInDto.Response.fromResult(result);
		return ApiResponse.success(response, "로그인을 성송했습니다.");
	}

	@PostMapping("/signout")
	public ApiResponse<Void> signOut(HttpServletRequest request) {
		HttpSession session = request.getSession(false); // 세션이 없으면 새로 만들지 않음
		if (session != null) {
			session.invalidate(); // 세션 완전 제거
		}
		SecurityContextHolder.clearContext();
		return ApiResponse.success("로그아웃 되었습니다.");
	}
}
