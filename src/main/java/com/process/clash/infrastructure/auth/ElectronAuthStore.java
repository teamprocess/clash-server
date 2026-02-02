package com.process.clash.infrastructure.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElectronAuthStore {

	private final StringRedisTemplate redis;
	private final ObjectMapper objectMapper;

	private static final Duration STATE_TTL = Duration.ofMinutes(5);
	private static final Duration CODE_TTL = Duration.ofSeconds(60);

	public void saveState(String state) {
		redis.opsForValue().set(stateKey(state), "1", STATE_TTL);
	}

	public boolean consumeState(String state) {
		// 원자적 연산으로 race condition 방지
		String key = stateKey(state);
		String value = redis.opsForValue().getAndDelete(key);
		return value != null;
	}

	public void saveOneTimeCode(String code, String state, Long userId) {
		try {
			OneTimeCodePayload payload = new OneTimeCodePayload(state, userId);
			String value = objectMapper.writeValueAsString(payload);
			redis.opsForValue().set(codeKey(code), value, CODE_TTL);
		} catch (JsonProcessingException e) {
			log.error("Failed to serialize OneTimeCodePayload", e);
			throw new IllegalStateException("Failed to save one-time code", e);
		}
	}

	public OneTimeCodePayload consumeOneTimeCode(String code) {
		// 원자적 연산으로 race condition 방지
		String key = codeKey(code);
		String value = redis.opsForValue().getAndDelete(key);
		if (value == null) return null;

		try {
			return objectMapper.readValue(value, OneTimeCodePayload.class);
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

	public record OneTimeCodePayload(String state, Long userId) {}
}
