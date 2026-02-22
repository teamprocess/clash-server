package com.process.clash.adapter.web.record.v2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.GetTodayRecordV2Data;
import com.process.clash.application.record.v2.port.in.GetCurrentRecordV2UseCase;
import com.process.clash.application.record.v2.port.in.GetMonitoredAppsV2UseCase;
import com.process.clash.application.record.v2.port.in.GetTodayRecordV2UseCase;
import com.process.clash.application.record.v2.port.in.StartRecordV2UseCase;
import com.process.clash.application.record.v2.port.in.StopRecordV2UseCase;
import com.process.clash.application.record.v2.port.in.SwitchDevelopAppV2UseCase;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@ExtendWith(MockitoExtension.class)
class RecordV2ControllerTest {

    @Mock
    private GetTodayRecordV2UseCase getTodayRecordV2UseCase;

    @Mock
    private StartRecordV2UseCase startRecordV2UseCase;

    @Mock
    private StopRecordV2UseCase stopRecordV2UseCase;

    @Mock
    private GetCurrentRecordV2UseCase getCurrentRecordV2UseCase;

    @Mock
    private GetMonitoredAppsV2UseCase getMonitoredAppsV2UseCase;

    @Mock
    private SwitchDevelopAppV2UseCase switchDevelopAppV2UseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RecordV2Controller controller = new RecordV2Controller(
            getTodayRecordV2UseCase,
            startRecordV2UseCase,
            stopRecordV2UseCase,
            getCurrentRecordV2UseCase,
            getMonitoredAppsV2UseCase,
            switchDevelopAppV2UseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(authenticatedActorResolver())
            .build();
    }

    @Test
    @DisplayName("GET /api/v2/record/daily 는 date 파라미터를 커맨드에 전달한다")
    void getDailyRecord_passesDateParameterToUseCase() throws Exception {
        when(getTodayRecordV2UseCase.execute(any()))
            .thenReturn(GetTodayRecordV2Data.Result.from("2026-02-20", 0L, null, List.of()));

        mockMvc.perform(get("/api/v2/record/daily").param("date", "2026-02-20"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("기록 현황을 조회했습니다."));

        ArgumentCaptor<GetTodayRecordV2Data.Command> captor =
            ArgumentCaptor.forClass(GetTodayRecordV2Data.Command.class);
        verify(getTodayRecordV2UseCase).execute(captor.capture());
        org.assertj.core.api.Assertions.assertThat(captor.getValue().date())
            .isEqualTo(LocalDate.of(2026, 2, 20));
    }

    @Test
    @DisplayName("GET /api/v2/record/daily 는 date 미입력 시 null을 전달한다")
    void getDailyRecord_passesNullDateWhenDateParamMissing() throws Exception {
        when(getTodayRecordV2UseCase.execute(any()))
            .thenReturn(GetTodayRecordV2Data.Result.from("2026-02-22", 0L, null, List.of()));

        mockMvc.perform(get("/api/v2/record/daily"))
            .andExpect(status().isOk());

        ArgumentCaptor<GetTodayRecordV2Data.Command> captor =
            ArgumentCaptor.forClass(GetTodayRecordV2Data.Command.class);
        verify(getTodayRecordV2UseCase).execute(captor.capture());
        org.assertj.core.api.Assertions.assertThat(captor.getValue().date()).isNull();
    }

    @Test
    @DisplayName("기존 /today 경로는 더 이상 존재하지 않는다")
    void oldTodayEndpoint_isNotFound() throws Exception {
        mockMvc.perform(get("/api/v2/record/today"))
            .andExpect(status().isNotFound());
    }

    private HandlerMethodArgumentResolver authenticatedActorResolver() {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(AuthenticatedActor.class);
            }

            @Override
            public Object resolveArgument(
                MethodParameter parameter,
                ModelAndViewContainer mavContainer,
                org.springframework.web.context.request.NativeWebRequest webRequest,
                WebDataBinderFactory binderFactory
            ) {
                return new Actor(1L);
            }
        };
    }
}
