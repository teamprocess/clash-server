package com.process.clash.adapter.web.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.auth.dto.SignUpDto;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.application.user.data.SignInData;
import com.process.clash.application.user.data.SignUpData;
import com.process.clash.application.user.port.in.SignInUseCase;
import com.process.clash.application.user.port.in.SignUpUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final SignUpUseCase signUpUseCase;
	private final SignInUseCase signInUseCase;

	@PostMapping("/signup")
	public ApiResponse<Void> signUp(@Valid @RequestBody SignUpDto.Request request) {
		SignUpData.Command command = new SignUpData.Command(request.username(), request.password(), request.name());
		signUpUseCase.execute(command);
		return ApiResponse.success("회원가입이 완료되었습니다.");
	}

	@PostMapping("/signin")
	public ApiResponse<SignInDto.Response> signIn(@Valid @RequestBody SignInDto.Request request) {
		SignInData.Command command = new SignInData.Command(request.username(), request.password());
		SignInData.Result result = signInUseCase.execute(command);
		SignInDto.Response response = SignInDto.Response.builder()
				.id(result.id())
				.username(result.username())
				.name(result.name())
				.build();
		return ApiResponse.success(response);
	}
}
