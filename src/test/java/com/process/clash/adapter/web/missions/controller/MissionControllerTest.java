package com.process.clash.adapter.web.missions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.roadmap.missions.controller.MissionController;
import com.process.clash.adapter.web.roadmap.missions.dto.MissionSubmitDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.missions.data.SubmitMissionAnswerData;
import com.process.clash.application.roadmap.missions.port.in.SubmitMissionAnswerUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class MissionControllerTest {

    @Autowired
    private MissionController missionController;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SubmitMissionAnswerUseCase submitMissionAnswerUseCase;

    private void initMockMvc() {
        if (this.mockMvc == null) {
            this.mockMvc = MockMvcBuilders.standaloneSetup(missionController)
                    .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                        @Override
                        public boolean supportsParameter(MethodParameter parameter) {
                            return parameter.hasParameterAnnotation(AuthenticatedActor.class);
                        }

                        @Override
                        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                      org.springframework.web.context.request.NativeWebRequest webRequest,
                                                      WebDataBinderFactory binderFactory) throws Exception {
                            // Mock Actor
                            return new Actor(1L);
                        }
                    })
                    .build();
        }
    }

    @Test
    @WithMockUser
    void submitAnswer_shouldSucceed() throws Exception {
        initMockMvc();
        // Given
        Long missionId = 1L;
        Long questionId = 1L;
        Long submittedChoiceId = 1L;

        SubmitMissionAnswerData.Result result = new SubmitMissionAnswerData.Result(
                true, "정답입니다!", 1, 5, 1L
        );

        when(submitMissionAnswerUseCase.execute(any(SubmitMissionAnswerData.Command.class)))
                .thenReturn(result);

        MissionSubmitDto.Request request = new MissionSubmitDto.Request(submittedChoiceId);

        // When & Then
        mockMvc.perform(post("/api/missions/{missionId}/questions/{questionId}/submit", missionId, questionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}