package com.process.clash.adapter.web.helpcontent.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.common.GlobalExceptionHandler;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.helpcontent.port.in.CreateHelpContentUseCase;
import com.process.clash.application.helpcontent.port.in.UpdateHelpContentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import java.util.Map;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminHelpContentControllerTest {

    @Mock
    private CreateHelpContentUseCase createHelpContentUseCase;

    @Mock
    private UpdateHelpContentUseCase updateHelpContentUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new AdminHelpContentController(
                        createHelpContentUseCase,
                        updateHelpContentUseCase
                ))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(authenticatedActorResolver())
                .setValidator(validator)
                .build();
    }

    @Test
    @DisplayName("POST /api/admin/help-contents 는 100자를 초과한 키에 400을 반환한다")
    void createHelpContent_whenKeyExceeds100Characters_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/admin/help-contents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "key", "a".repeat(101),
                                "content", "도움말 내용"
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_ARGUMENT"))
                .andExpect(jsonPath("$.error.details.key").value("도움말 키는 100자 이하이어야 합니다."));

        verifyNoInteractions(createHelpContentUseCase, updateHelpContentUseCase);
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
                    NativeWebRequest webRequest,
                    WebDataBinderFactory binderFactory
            ) {
                return new Actor(1L);
            }
        };
    }
}
