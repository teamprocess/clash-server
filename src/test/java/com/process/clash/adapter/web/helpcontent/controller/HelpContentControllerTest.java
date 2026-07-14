package com.process.clash.adapter.web.helpcontent.controller;

import com.process.clash.application.helpcontent.data.GetHelpContentData;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class HelpContentControllerTest {

    @Mock
    private GetHelpContentUseCase getHelpContentUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HelpContentController(getHelpContentUseCase)).build();
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
}
