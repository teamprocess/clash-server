package com.process.clash.adapter.web.missions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.missions.dto.MissionSubmitDto;
import com.process.clash.application.missions.port.in.SubmitMissionAnswerUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class MissionControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubmitMissionAnswerUseCase submitMissionAnswerUseCase;

    private void initMockMvc() {
        if (this.mockMvc == null) {
            this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        }
    }

    @Test
    void submitAnswer_shouldSucceed() throws Exception {
        initMockMvc();
        // Given
        Long missionId = 1L;
        Long questionId = 1L;
        Long submittedChoiceId = 1L;

        SubmitMissionAnswerUseCase.Result result = new SubmitMissionAnswerUseCase.Result(
                true, "정답입니다!", 1, 5, 1L
        );

        when(submitMissionAnswerUseCase.execute(any(SubmitMissionAnswerUseCase.Command.class)))
                .thenReturn(result);

        MissionSubmitDto.Request request = new MissionSubmitDto.Request(submittedChoiceId);

        // When & Then
        mockMvc.perform(post("/api/missions/{missionId}/questions/{questionId}/submit", missionId, questionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}