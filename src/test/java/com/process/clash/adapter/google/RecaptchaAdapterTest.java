package com.process.clash.adapter.google;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecaptchaAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private RecaptchaAdapter recaptchaAdapter;

    @BeforeEach
    void setUp() {
        recaptchaAdapter = new RecaptchaAdapter(restTemplate);
        ReflectionTestUtils.setField(recaptchaAdapter, "secretKey", "test-secret-key");
        ReflectionTestUtils.setField(recaptchaAdapter, "verifyUrl", "https://www.google.com/recaptcha/api/siteverify");
    }

    @Test
    @DisplayName("유효한 토큰과 충분한 점수로 검증 성공")
    void verifyToken_ValidTokenWithHighScore_ReturnsTrue() {
        // given
        String token = "valid-token";
        Map<String, Object> response = Map.of(
                "success", true,
                "score", 0.9
        );

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(response);

        // when
        boolean result = recaptchaAdapter.verifyToken(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유효한 토큰이지만 점수가 낮아 검증 실패")
    void verifyToken_ValidTokenWithLowScore_ReturnsFalse() {
        // given
        String token = "valid-token";
        Map<String, Object> response = Map.of(
                "success", true,
                "score", 0.3
        );

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(response);

        // when
        boolean result = recaptchaAdapter.verifyToken(token);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("점수가 정확히 0.5일 때 검증 성공")
    void verifyToken_ScoreExactlyMinimum_ReturnsTrue() {
        // given
        String token = "valid-token";
        Map<String, Object> response = Map.of(
                "success", true,
                "score", 0.5
        );

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(response);

        // when
        boolean result = recaptchaAdapter.verifyToken(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰이 null일 때 검증 실패")
    void verifyToken_NullToken_ReturnsFalse() {
        // when
        boolean result = recaptchaAdapter.verifyToken(null);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("토큰이 빈 문자열일 때 검증 실패")
    void verifyToken_BlankToken_ReturnsFalse() {
        // when
        boolean result = recaptchaAdapter.verifyToken("   ");

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Google API 응답이 success=false일 때 검증 실패")
    void verifyToken_ApiReturnsFailure_ReturnsFalse() {
        // given
        String token = "invalid-token";
        Map<String, Object> response = Map.of(
                "success", false,
                "error-codes", new String[]{"invalid-input-response"}
        );

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(response);

        // when
        boolean result = recaptchaAdapter.verifyToken(token);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Google API 응답이 null일 때 검증 실패")
    void verifyToken_ApiReturnsNull_ReturnsFalse() {
        // given
        String token = "valid-token";

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(null);

        // when
        boolean result = recaptchaAdapter.verifyToken(token);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Google API 호출 중 예외 발생 시 검증 실패")
    void verifyToken_RestClientException_ReturnsFalse() {
        // given
        String token = "valid-token";

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenThrow(new RestClientException("Connection failed"));

        // when
        boolean result = recaptchaAdapter.verifyToken(token);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("응답에 score가 없을 때 검증 실패")
    void verifyToken_MissingScore_ReturnsFalse() {
        // given
        String token = "valid-token";
        Map<String, Object> response = Map.of(
                "success", true
        );

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(response);

        // when
        boolean result = recaptchaAdapter.verifyToken(token);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("score가 Integer 타입일 때도 정상 처리")
    void verifyToken_ScoreAsInteger_ReturnsTrue() {
        // given
        String token = "valid-token";
        Map<String, Object> response = Map.of(
                "success", true,
                "score", 1  // Integer instead of Double
        );

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(response);

        // when
        boolean result = recaptchaAdapter.verifyToken(token);

        // then
        assertThat(result).isTrue();
    }
}
