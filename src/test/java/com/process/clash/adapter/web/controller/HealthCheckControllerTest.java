package com.process.clash.adapter.web.controller;

import com.process.clash.adapter.web.controller.dto.HealthResponse;
import com.process.clash.adapter.web.controller.dto.PingResponse;
import com.process.clash.application.health.data.GetHealthStatusData;
import com.process.clash.application.health.data.PingData;
import com.process.clash.application.health.port.in.HealthCheckUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthCheckControllerTest {

    @Mock
    private HealthCheckUseCase healthCheckUseCase;

    @InjectMocks
    private HealthCheckController controller;

    @Test
    void shouldReturnPongWhenPingEndpointIsCalled() {
        // given
        when(healthCheckUseCase.ping()).thenReturn(new PingData.Result("PONG"));

        // when
        ResponseEntity<PingResponse> response = controller.ping();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("PONG");
    }

    @Test
    void shouldReturnHealthStatusWhenAllServicesAreUp() {
        // given
        GetHealthStatusData.Result result = new GetHealthStatusData.Result("UP", "UP", "UP", "UP", null);
        when(healthCheckUseCase.getHealthStatus()).thenReturn(result);

        // when
        ResponseEntity<HealthResponse> response = controller.health();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("UP");
        assertThat(response.getBody().db()).isEqualTo("UP");
        assertThat(response.getBody().redis()).isEqualTo("UP");
        assertThat(response.getBody().app()).isEqualTo("UP");
    }

    @Test
    void shouldReturn503WhenAnyServiceIsDown() {
        // given
        GetHealthStatusData.Result result = new GetHealthStatusData.Result("DOWN", "DOWN", "UP", "UP", null);
        when(healthCheckUseCase.getHealthStatus()).thenReturn(result);

        // when
        ResponseEntity<HealthResponse> response = controller.health();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("DOWN");
        assertThat(response.getBody().db()).isEqualTo("DOWN");
        assertThat(response.getBody().redis()).isEqualTo("UP");
        assertThat(response.getBody().app()).isEqualTo("UP");
    }

    @Test
    void shouldHandleErrorResponse() {
        // given
        GetHealthStatusData.Result result = new GetHealthStatusData.Result("ERROR", null, null, null, "Health check failed");
        when(healthCheckUseCase.getHealthStatus()).thenReturn(result);

        // when
        ResponseEntity<HealthResponse> response = controller.health();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("ERROR");
        assertThat(response.getBody().message()).isEqualTo("Health check failed");
    }

    @Test
    void shouldHandleUnknownStatus() {
        // given
        GetHealthStatusData.Result result = new GetHealthStatusData.Result("UP", "UNKNOWN", "UNKNOWN", "UP", null);
        when(healthCheckUseCase.getHealthStatus()).thenReturn(result);

        // when
        ResponseEntity<HealthResponse> response = controller.health();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("UP");
        assertThat(response.getBody().db()).isEqualTo("UNKNOWN");
        assertThat(response.getBody().redis()).isEqualTo("UNKNOWN");
        assertThat(response.getBody().app()).isEqualTo("UP");
    }
}
