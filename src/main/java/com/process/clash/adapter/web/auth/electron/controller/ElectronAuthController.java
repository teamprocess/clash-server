package com.process.clash.adapter.web.auth.electron.controller;

import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.adapter.web.auth.electron.service.ElectronAuthService;
import com.process.clash.adapter.web.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/electron")
@RequiredArgsConstructor
public class ElectronAuthController {

	private final ElectronAuthService electronAuthService;

	@PostMapping("/start")
	public ApiResponse<ElectronAuthDto.StartResponse> start() {
		return ApiResponse.success(electronAuthService.start());
	}

	@PostMapping("/login")
	public ApiResponse<Map<String, String>> login(@Valid @RequestBody ElectronAuthDto.LoginRequest req) {
		String redirectUrl = electronAuthService.loginAndRedirect(req);
		// fetch API는 custom scheme(clashapp://)으로 302 리다이렉트할 수 없음
		// 200 + JSON으로 deep link URL을 반환하여 클라이언트가 명시적으로 이동하도록 함
		return ApiResponse.success(Map.of("redirectUrl", redirectUrl));
    }

	@PostMapping("/exchange")
	public ApiResponse<ElectronAuthDto.ExchangeResponse> exchange(
			@Valid @RequestBody ElectronAuthDto.ExchangeRequest req,
			HttpServletRequest httpRequest
	) {
		ElectronAuthDto.ExchangeResponse response = electronAuthService.exchange(req, httpRequest);
		return ApiResponse.success(response, "로그인을 성공했습니다.");
	}
}
