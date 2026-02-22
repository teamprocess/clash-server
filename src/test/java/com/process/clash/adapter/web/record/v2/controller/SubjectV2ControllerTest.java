package com.process.clash.adapter.web.record.v2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.CreateSubjectV2Data;
import com.process.clash.application.record.v2.data.GetAllSubjectsV2Data;
import com.process.clash.application.record.v2.port.in.CreateSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.in.CreateSubjectV2UseCase;
import com.process.clash.application.record.v2.port.in.DeleteSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.in.DeleteSubjectV2UseCase;
import com.process.clash.application.record.v2.port.in.GetAllSubjectsV2UseCase;
import com.process.clash.application.record.v2.port.in.UpdateSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.in.UpdateSubjectV2UseCase;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class SubjectV2ControllerTest {

    @Mock
    private GetAllSubjectsV2UseCase getAllSubjectsV2UseCase;

    @Mock
    private CreateSubjectV2UseCase createSubjectV2UseCase;

    @Mock
    private UpdateSubjectV2UseCase updateSubjectV2UseCase;

    @Mock
    private DeleteSubjectV2UseCase deleteSubjectV2UseCase;

    @Mock
    private CreateSubjectTaskV2UseCase createSubjectTaskV2UseCase;

    @Mock
    private UpdateSubjectTaskV2UseCase updateSubjectTaskV2UseCase;

    @Mock
    private DeleteSubjectTaskV2UseCase deleteSubjectTaskV2UseCase;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        SubjectV2Controller controller = new SubjectV2Controller(
            getAllSubjectsV2UseCase,
            createSubjectV2UseCase,
            updateSubjectV2UseCase,
            deleteSubjectV2UseCase,
            createSubjectTaskV2UseCase,
            updateSubjectTaskV2UseCase,
            deleteSubjectTaskV2UseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(authenticatedActorResolver())
            .build();
    }

    @Test
    @DisplayName("GET /api/v2/record/subjects 는 과목 그룹 목록을 반환한다")
    void getAllSubjects_returnsSuccessResponse() throws Exception {
        when(getAllSubjectsV2UseCase.execute(any()))
            .thenReturn(GetAllSubjectsV2Data.Result.from(List.of()));

        mockMvc.perform(get("/api/v2/record/subjects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("과목 그룹 목록을 조회했습니다."));

        verify(getAllSubjectsV2UseCase).execute(any(GetAllSubjectsV2Data.Command.class));
    }

    @Test
    @DisplayName("POST /api/v2/record/subjects 는 생성 요청을 커맨드로 전달한다")
    void createSubject_passesCommandToUseCase() throws Exception {
        mockMvc.perform(post("/api/v2/record/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.Map.of("name", "백엔드 프로젝트"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("새로운 과목 그룹을 생성했습니다."));

        ArgumentCaptor<CreateSubjectV2Data.Command> captor =
            ArgumentCaptor.forClass(CreateSubjectV2Data.Command.class);
        verify(createSubjectV2UseCase).execute(captor.capture());
        org.assertj.core.api.Assertions.assertThat(captor.getValue().name()).isEqualTo("백엔드 프로젝트");
        org.assertj.core.api.Assertions.assertThat(captor.getValue().actor().id()).isEqualTo(1L);
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
