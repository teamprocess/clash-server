package com.process.clash.application.health.service;

import com.process.clash.application.health.data.GetHealthStatusData;
import com.process.clash.application.health.data.PingData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.SystemHealth;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthCheckServiceTest {

    @Mock
    private HealthEndpoint healthEndpoint;

    @InjectMocks
    private HealthCheckService healthCheckService;

    @Test
    void shouldReturnPong() {
        // when
        PingData.Result response = healthCheckService.ping();

        // then
        assertThat(response.message()).isEqualTo("PONG");
    }

    @Test
    void shouldReturnUpStatusWhenAllServicesAreHealthy() {
        // given
        Map<String, HealthComponent> components = new HashMap<>();
        components.put("db", createHealthComponent(Status.UP));
        components.put("redis", createHealthComponent(Status.UP));
        components.put("ping", createHealthComponent(Status.UP));

        SystemHealth systemHealth = mock(SystemHealth.class);
        when(systemHealth.getStatus()).thenReturn(Status.UP);
        when(systemHealth.getComponents()).thenReturn(components);
        when(healthEndpoint.health()).thenReturn(systemHealth);

        // when
        GetHealthStatusData.Result response = healthCheckService.getHealthStatus();

        // then
        assertThat(response.status()).isEqualTo("UP");
        assertThat(response.db()).isEqualTo("UP");
        assertThat(response.redis()).isEqualTo("UP");
        assertThat(response.app()).isEqualTo("UP");
    }

    @Test
    void shouldReturnDownStatusWhenDatabaseIsDown() {
        // given
        Map<String, HealthComponent> components = new HashMap<>();
        components.put("db", createHealthComponent(Status.DOWN));
        components.put("redis", createHealthComponent(Status.UP));
        components.put("ping", createHealthComponent(Status.UP));

        SystemHealth systemHealth = mock(SystemHealth.class);
        when(systemHealth.getStatus()).thenReturn(Status.DOWN);
        when(systemHealth.getComponents()).thenReturn(components);
        when(healthEndpoint.health()).thenReturn(systemHealth);

        // when
        GetHealthStatusData.Result response = healthCheckService.getHealthStatus();

        // then
        assertThat(response.status()).isEqualTo("DOWN");
        assertThat(response.db()).isEqualTo("DOWN");
        assertThat(response.redis()).isEqualTo("UP");
    }

    @Test
    void shouldReturnDownStatusWhenRedisIsDown() {
        // given
        Map<String, HealthComponent> components = new HashMap<>();
        components.put("db", createHealthComponent(Status.UP));
        components.put("redis", createHealthComponent(Status.DOWN));
        components.put("ping", createHealthComponent(Status.UP));

        SystemHealth systemHealth = mock(SystemHealth.class);
        when(systemHealth.getStatus()).thenReturn(Status.DOWN);
        when(systemHealth.getComponents()).thenReturn(components);
        when(healthEndpoint.health()).thenReturn(systemHealth);

        // when
        GetHealthStatusData.Result response = healthCheckService.getHealthStatus();

        // then
        assertThat(response.status()).isEqualTo("DOWN");
        assertThat(response.db()).isEqualTo("UP");
        assertThat(response.redis()).isEqualTo("DOWN");
    }

    @Test
    void shouldReturnUnknownForMissingComponents() {
        // given
        Map<String, HealthComponent> components = new HashMap<>();
        components.put("ping", createHealthComponent(Status.UP));
        // db와 redis가 없는 경우

        SystemHealth systemHealth = mock(SystemHealth.class);
        when(systemHealth.getStatus()).thenReturn(Status.UP);
        when(systemHealth.getComponents()).thenReturn(components);
        when(healthEndpoint.health()).thenReturn(systemHealth);

        // when
        GetHealthStatusData.Result response = healthCheckService.getHealthStatus();

        // then
        assertThat(response.status()).isEqualTo("UP");
        assertThat(response.db()).isEqualTo("UNKNOWN");
        assertThat(response.redis()).isEqualTo("UNKNOWN");
    }

    @Test
    void shouldHandleExceptionGracefully() {
        // given
        when(healthEndpoint.health()).thenThrow(new RuntimeException("Health check failed"));

        // when
        GetHealthStatusData.Result response = healthCheckService.getHealthStatus();

        // then
        assertThat(response.status()).isEqualTo("ERROR");
        assertThat(response.message()).contains("Health check failed");
    }

    @Test
    void shouldHandleNonSystemHealth() {
        // given
        HealthComponent healthComponent = mock(HealthComponent.class);
        when(healthEndpoint.health()).thenReturn(healthComponent);

        // when
        GetHealthStatusData.Result response = healthCheckService.getHealthStatus();

        // then
        assertThat(response.status()).isEqualTo("UNKNOWN");
    }

    private HealthComponent createHealthComponent(Status status) {
        HealthComponent component = mock(HealthComponent.class);
        when(component.getStatus()).thenReturn(status);
        return component;
    }
}
