package com.process.clash.adapter.web.helpcontent.controller;

import com.process.clash.adapter.web.common.GlobalExceptionHandler;
import com.process.clash.application.helpcontent.data.GetHelpContentData;
import com.process.clash.application.helpcontent.exception.exception.notfound.HelpContentNotFoundException;
import com.process.clash.application.helpcontent.port.in.GetHelpContentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class HelpContentControllerTest {

    @Mock
    private GetHelpContentUseCase getHelpContentUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HelpContentController(getHelpContentUseCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/help-contents/{key} 는 내용과 ETag를 반환한다")
    void getHelpContent_returnsContentAndEtag() throws Exception {
        when(getHelpContentUseCase.execute("cookie-tooltip"))
                .thenReturn(new GetHelpContentData.Result("cookie-tooltip", "쿠키 안내", 3));

        mockMvc.perform(get("/api/help-contents/cookie-tooltip"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ETAG, "\"3\""))
                .andExpect(content().contentTypeCompatibleWith("text/plain"))
                .andExpect(content().string("쿠키 안내"));
    }

    @Test
    @DisplayName("동일한 If-None-Match를 전달하면 304를 반환한다")
    void getHelpContent_whenEtagMatches_returnsNotModified() throws Exception {
        when(getHelpContentUseCase.execute("cookie-tooltip"))
                .thenReturn(new GetHelpContentData.Result("cookie-tooltip", "쿠키 안내", 3));

        mockMvc.perform(get("/api/help-contents/cookie-tooltip")
                        .header(HttpHeaders.IF_NONE_MATCH, "\"3\""))
                .andExpect(status().isNotModified())
                .andExpect(header().string(HttpHeaders.ETAG, "\"3\""))
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("존재하지 않는 도움말 조회는 JSON 오류 응답을 반환한다")
    void getHelpContent_whenNotFound_returnsJsonError() throws Exception {
        when(getHelpContentUseCase.execute("unknown-tooltip"))
                .thenThrow(new HelpContentNotFoundException());

        mockMvc.perform(get("/api/help-contents/unknown-tooltip"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("HELP_CONTENT_NOT_FOUND"));
    }
}
