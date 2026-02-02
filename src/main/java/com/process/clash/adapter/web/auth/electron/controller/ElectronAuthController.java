package com.process.clash.adapter.web.auth.electron.controller;

import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.adapter.web.auth.electron.service.ElectronAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/electron")
@RequiredArgsConstructor
public class ElectronAuthController {

	private final ElectronAuthService electronAuthService;

	@PostMapping("/start")
	public ElectronAuthDto.StartResponse start() {
		return electronAuthService.start();
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(@Valid @RequestBody ElectronAuthDto.LoginRequest req) {
		String location = electronAuthService.loginAndRedirect(req);
		return ResponseEntity.status(302)
				.header("Location", location)
				.build();
	}

	@PostMapping("/exchange")
	public ElectronAuthDto.ExchangeResponse exchange(
			@Valid @RequestBody ElectronAuthDto.ExchangeRequest req,
			HttpServletRequest httpRequest
	) {
		return electronAuthService.exchange(req, httpRequest);
	}
}
