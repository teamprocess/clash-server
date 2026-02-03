package com.process.clash.adapter.web.auth.electron.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ElectronAuthDto {

	public record StartResponse(String loginUrl, String state) {}

	public record LoginRequest(
			@NotBlank(message = "유저네임은 필수 입력값입니다.")
			@Size(min = 3, max = 20, message = "유저네임은 3~20자여야 합니다.")
			String username,

			@NotBlank(message = "비밀번호는 필수 입력값입니다.")
			@Size(min = 8, max = 100, message = "비밀번호는 8~100자여야 합니다.")
			String password,

			String action,

			@NotBlank(message = "State는 필수 입력값입니다.")
			String state,

			@NotBlank(message = "Redirect URI는 필수 입력값입니다.")
			String redirectUri
	) {}

	public record ExchangeRequest(
			@NotBlank(message = "Code는 필수 입력값입니다.")
			String code,

			@NotBlank(message = "State는 필수 입력값입니다.")
			String state
	) {}

	public record ExchangeResponse(
			Long userId,
			String username,
			String role
	) {}

	// 회원가입 관련 DTO
	public record StartSignupResponse(String signupUrl, String state) {}

	public record SignupRequest(
			@NotBlank(message = "유저네임은 필수 입력값입니다.")
			@Size(min = 3, max = 20, message = "유저네임은 3~20자여야 합니다.")
			@Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "유저네임은 영문, 숫자, _, -만 사용 가능합니다.")
			String username,

			@NotBlank(message = "이메일은 필수 입력값입니다.")
			@Email(message = "이메일 형식이 올바르지 않습니다.")
			String email,

			@NotBlank(message = "비밀번호는 필수 입력값입니다.")
			@Size(min = 8, max = 100, message = "비밀번호는 8~100자여야 합니다.")
			String password,

			@NotBlank(message = "State는 필수 입력값입니다.")
			String state,

			@NotBlank(message = "Redirect URI는 필수 입력값입니다.")
			String redirectUri
	) {}

	public record VerifyEmailRequest(
			@NotBlank(message = "인증 코드는 필수 입력값입니다.")
			@Size(min = 6, max = 6, message = "인증 코드는 6자리여야 합니다.")
			String verificationCode,

			@NotBlank(message = "State는 필수 입력값입니다.")
			String state,

			@NotBlank(message = "Redirect URI는 필수 입력값입니다.")
			String redirectUri
	) {}
}
