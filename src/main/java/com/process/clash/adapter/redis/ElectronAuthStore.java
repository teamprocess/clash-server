package com.process.clash.adapter.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.application.auth.electron.port.out.ElectronAuthStorePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElectronAuthStore implements ElectronAuthStorePort {

	private final StringRedisTemplate redis;
	private final ObjectMapper objectMapper;

	private static final Duration STATE_TTL = Duration.ofMinutes(5);
	private static final Duration CODE_TTL = Duration.ofSeconds(60);
	private static final Duration SIGNUP_SESSION_TTL = Duration.ofMinutes(10);

	@Override
	public void saveState(String state) {
		redis.opsForValue().set(stateKey(state), "1", STATE_TTL);
	}

	/**
	 * State 존재 여부 검증 (소비하지 않음)
	 */
	@Override
	public boolean validateState(String state) {
		String key = stateKey(state);
		String value = redis.opsForValue().get(key);
		return value != null;
	}

	/**
	 * State 소비 (검증 + 삭제를 원자적으로 수행)
	 */
	@Override
	public boolean consumeState(String state) {
		// 원자적 연산으로 race condition 방지
		String key = stateKey(state);
		String value = redis.opsForValue().getAndDelete(key);
		return value != null;
	}

	@Override
	public void saveOneTimeCode(String code, String state, Long userId, boolean noRecaptcha) {
		try {
			ElectronAuthStorePort.OneTimeCodePayload payload = new ElectronAuthStorePort.OneTimeCodePayload(state, userId, noRecaptcha);
			String value = objectMapper.writeValueAsString(payload);
			redis.opsForValue().set(codeKey(code), value, CODE_TTL);
		} catch (JsonProcessingException e) {
			log.error("Failed to serialize OneTimeCodePayload", e);
			throw new IllegalStateException("Failed to save one-time code", e);
		}
	}

	@Override
	public ElectronAuthStorePort.OneTimeCodePayload consumeOneTimeCode(String code) {
		// 원자적 연산으로 race condition 방지
		String key = codeKey(code);
		String value = redis.opsForValue().getAndDelete(key);
		if (value == null) return null;

		try {
			return objectMapper.readValue(value, ElectronAuthStorePort.OneTimeCodePayload.class);
		} catch (JsonProcessingException e) {
			log.error("Failed to deserialize OneTimeCodePayload: {}", value, e);
			return null;
		}
	}

	private String stateKey(String state) {
		return "electron:auth:state:" + state;
	}

	private String codeKey(String code) {
		return "electron:auth:code:" + code;
	}

	private String signupSessionKey(String state) {
		return "electron:auth:signup:" + state;
	}

	// 회원가입 세션 저장 (state -> redirectUri)
	@Override
	public void saveSignupSession(String state, String redirectUri) {
		redis.opsForValue().set(signupSessionKey(state), redirectUri, SIGNUP_SESSION_TTL);
	}

	// 회원가입 세션 소비
	@Override
	public String consumeSignupSession(String state) {
		String key = signupSessionKey(state);
		return redis.opsForValue().getAndDelete(key);
	}
}
