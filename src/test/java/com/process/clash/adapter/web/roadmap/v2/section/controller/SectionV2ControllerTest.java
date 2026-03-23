package com.process.clash.adapter.web.roadmap.v2.section.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2DetailsData;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2ListData;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2PreviewData;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2DetailsUseCase;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2ListUseCase;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2PreviewUseCase;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@ExtendWith(MockitoExtension.class)
class SectionV2ControllerTest {

    @Mock
    private GetSectionV2ListUseCase getSectionV2ListUseCase;

    @Mock
    private GetSectionV2PreviewUseCase getSectionV2PreviewUseCase;

    @Mock
    private GetSectionV2DetailsUseCase getSectionV2DetailsUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        SectionV2Controller controller = new SectionV2Controller(
                getSectionV2ListUseCase,
                getSectionV2PreviewUseCase,
                getSectionV2DetailsUseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(authenticatedActorResolver())
                .build();
    }

    @Test
    @DisplayName("GET /api/v2/sections/{sectionId}/details 는 completed 필드를 반환한다")
    void getSectionDetails_returnsCompletedField() throws Exception {
        when(getSectionV2DetailsUseCase.execute(any()))
                .thenReturn(new GetSectionV2DetailsData.Result(
                        1L,
                        "스프링 입문",
                        true,
                        3,
                        10L,
                        2,
                        List.of()
                ));

        mockMvc.perform(get("/api/v2/sections/1/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sectionId").value(1))
                .andExpect(jsonPath("$.data.completed").value(true))
                .andExpect(jsonPath("$.message").value("로드맵 상세 조회를 성공했습니다."));

        verify(getSectionV2DetailsUseCase).execute(any(GetSectionV2DetailsData.Command.class));
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
