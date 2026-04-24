package com.process.clash.adapter.web.roadmap.v2.question.controller;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.common.GlobalExceptionHandler;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.question.port.in.CreateQuestionV2UseCase;
import com.process.clash.application.roadmap.v2.question.port.in.DeleteQuestionV2UseCase;
import com.process.clash.application.roadmap.v2.question.port.in.UpdateQuestionV2UseCase;
import java.util.Map;
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
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@ExtendWith(MockitoExtension.class)
class AdminQuestionV2ControllerTest {

    @Mock
    private CreateQuestionV2UseCase createQuestionV2UseCase;

    @Mock
    private UpdateQuestionV2UseCase updateQuestionV2UseCase;

    @Mock
    private DeleteQuestionV2UseCase deleteQuestionV2UseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AdminQuestionV2Controller controller = new AdminQuestionV2Controller(
                createQuestionV2UseCase,
                updateQuestionV2UseCase,
                deleteQuestionV2UseCase
        );

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(authenticatedActorResolver())
                .setValidator(validator)
                .build();
    }

    @Test
    @DisplayName("POST /api/v2/admin/questions 는 content가 2000자를 초과하면 400을 반환한다")
    void createQuestion_whenContentExceeds2000Characters_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v2/admin/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "chapterId", 1L,
                                "content", "a".repeat(2001),
                                "explanation", "해설",
                                "orderIndex", 0,
                                "difficulty", 1
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_ARGUMENT"))
                .andExpect(jsonPath("$.error.details.content").value("content는 최대 2000자까지 입력할 수 있습니다."));

        verifyNoInteractions(createQuestionV2UseCase);
    }

    @Test
    @DisplayName("PATCH /api/v2/admin/questions/{questionId} 는 content가 2000자를 초과하면 400을 반환한다")
    void updateQuestion_whenContentExceeds2000Characters_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/api/v2/admin/questions/{questionId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "content", "a".repeat(2001),
                                "explanation", "해설",
                                "orderIndex", 0,
                                "difficulty", 1
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_ARGUMENT"))
                .andExpect(jsonPath("$.error.details.content").value("content는 최대 2000자까지 입력할 수 있습니다."));

        verifyNoInteractions(updateQuestionV2UseCase);
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
