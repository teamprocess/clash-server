package com.process.clash.adapter.web.record.v2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.GetAllTasksV2Data;
import com.process.clash.application.record.v2.data.UpdateTaskCompletionV2Data;
import com.process.clash.application.record.v2.port.in.CreateSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.in.GetAllTasksV2UseCase;
import com.process.clash.application.record.v2.port.in.UpdateTaskCompletionV2UseCase;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@ExtendWith(MockitoExtension.class)
class TaskV2ControllerTest {

    @Mock
    private GetAllTasksV2UseCase getAllTasksV2UseCase;

    @Mock
    private CreateSubjectTaskV2UseCase createSubjectTaskV2UseCase;

    @Mock
    private UpdateTaskCompletionV2UseCase updateTaskCompletionV2UseCase;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        TaskV2Controller controller = new TaskV2Controller(
            getAllTasksV2UseCase,
            createSubjectTaskV2UseCase,
            updateTaskCompletionV2UseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(authenticatedActorResolver())
            .build();
    }

    @Test
    @DisplayName("GET /api/v2/record/tasks 는 세부 작업 목록을 반환한다")
    void getAllTasks_returnsSuccessResponse() throws Exception {
        when(getAllTasksV2UseCase.execute(any()))
            .thenReturn(GetAllTasksV2Data.Result.from(List.of()));

        mockMvc.perform(get("/api/v2/record/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("세부 작업 목록을 조회했습니다."));

        verify(getAllTasksV2UseCase).execute(any(GetAllTasksV2Data.Command.class));
    }

    @Test
    @DisplayName("POST /api/v2/record/tasks 는 subjectId 없이도 생성 요청을 전달한다")
    void createTask_withoutSubjectId() throws Exception {
        mockMvc.perform(post("/api/v2/record/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.Map.of("name", "리팩터링"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("새로운 세부 작업을 생성했습니다."));

        verify(createSubjectTaskV2UseCase).execute(any());
    }

    @Test
    @DisplayName("PATCH /api/v2/record/tasks/{taskId}/completion 은 완료 상태를 변경한다")
    void updateTaskCompletion_returnsUpdatedTask() throws Exception {
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", true, 0L, Instant.now(), Instant.now());
        when(updateTaskCompletionV2UseCase.execute(any()))
            .thenReturn(UpdateTaskCompletionV2Data.Result.from(task));

        mockMvc.perform(patch("/api/v2/record/tasks/11/completion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.Map.of("completed", true))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(11))
            .andExpect(jsonPath("$.data.completed").value(true));
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
