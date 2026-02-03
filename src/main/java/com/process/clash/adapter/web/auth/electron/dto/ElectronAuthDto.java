package com.process.clash.adapter.web.auth.electron.dto;

public class ElectronAuthDto {

	public record StartResponse(String loginUrl, String state) {}

	public record LoginRequest(
			String username,
			String password,
			String recaptchaToken,
			String action,
			String state,
			String redirectUri
	) {}

	public record ExchangeRequest(String code, String state) {}

	public record ExchangeResponse(
			Long userId,
			String username,
			String role
	) {}
}
