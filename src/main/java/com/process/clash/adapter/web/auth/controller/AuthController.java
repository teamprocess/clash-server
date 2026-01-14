package com.process.clash.adapter.web.auth.controller;

import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.auth.dto.SignUpDto;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.application.user.user.data.SignInData;
import com.process.clash.application.user.user.data.SignUpData;
import com.process.clash.application.user.user.port.in.SignInUseCase;
import com.process.clash.application.user.user.port.in.SignUpUseCase;
import com.process.clash.application.user.user.port.out.AuthEventRepositoryPort;
import com.process.clash.infrastructure.principle.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final SignUpUseCase signUpUseCase;
	private final SignInUseCase signInUseCase;
	private final RememberMeServices rememberMeServices;
	private final SecurityContextRepository securityContextRepository;
	private final AuthEventRepositoryPort authEventRepositoryPort;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/sign-up")
	public ApiResponse<Void> signUp(@Valid @RequestBody SignUpDto.Request request) {
		SignUpData.Command command = SignUpData.Command.fromRequest(request);
		signUpUseCase.execute(command);
		return ApiResponse.success("회원가입이 완료되었습니다.");
	}

	@PostMapping("/sign-in")
	public ApiResponse<SignInDto.Response> signIn(
			@Valid @RequestBody SignInDto.Request request,
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse
	) {
		SignInData.Command command = new SignInData.Command(request.username(), request.password());
		SignInData.Result result = signInUseCase.execute(command);

		HttpServletRequest wrapper = new HttpServletRequestWrapper(httpRequest) {
			@Override
			public String getParameter(String name) {
				if ("remember-me".equals(name)) return "true"; // 파라미터 이름 확인!
				return super.getParameter(name);
			}
		};

		AuthUser authUser = new AuthUser(result.id(), result.username(), result.encodedPassword(), result.role());
		// 인증 토큰을 생성
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				authUser,
				result.encodedPassword(),
				authUser.getAuthorities()
		);

		// 빈 보안 컨텍스트 가져오기
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		// 컨텍스트에 토큰 저장
		context.setAuthentication(token); // 토큰 먼저 박기
		SecurityContextHolder.setContext(context); // 홀더에 올리기
		securityContextRepository.saveContext(context, httpRequest, httpResponse); // 완성된 컨텍스트를 세션에 저장

		// record login event (ip, device)
		String ip = httpRequest.getRemoteAddr();
		String device = httpRequest.getHeader("User-Agent");
		authEventRepositoryPort.recordLogin(result.username(), ip, device);

		if (request.rememberMe()) {
			rememberMeServices.loginSuccess(wrapper, httpResponse, token);
		}

		SignInDto.Response response = SignInDto.Response.fromResult(result);
		return ApiResponse.success(response, "로그인을 성공했습니다.");
	}

	@PostMapping("/sign-out")
	public ApiResponse<Void> signOut(HttpServletRequest request) {
		HttpSession session = request.getSession(false); // 세션이 없으면 새로 만들지 않음

		// try to determine username before invalidation
		String username = null;
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
				username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
			} else if (principal instanceof String) {
				username = (String) principal;
			} else if (SecurityContextHolder.getContext().getAuthentication().getName() != null) {
				username = SecurityContextHolder.getContext().getAuthentication().getName();
			}
		}

		String ip = request.getRemoteAddr();
		String device = request.getHeader("User-Agent");

		if (session != null) {
			session.invalidate(); // 세션 완전 제거
		}
		SecurityContextHolder.clearContext();

		if (username != null) {
			authEventRepositoryPort.recordLogout(username, ip, device);
		}

		return ApiResponse.success("로그아웃 되었습니다.");
	}
}
