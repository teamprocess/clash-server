package com.process.clash.adapter.web.group.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.GetAllGroupsData;
import com.process.clash.application.group.data.GetGroupActivityData;
import com.process.clash.application.group.data.GetGroupDetailData;
import com.process.clash.application.group.data.GetMyGroupsData;
import com.process.clash.application.group.port.in.CreateGroupUseCase;
import com.process.clash.application.group.port.in.DeleteGroupUseCase;
import com.process.clash.application.group.port.in.GetAllGroupsUseCase;
import com.process.clash.application.group.port.in.GetGroupActivityUseCase;
import com.process.clash.application.group.port.in.GetGroupDetailUseCase;
import com.process.clash.application.group.port.in.GetMyGroupsUseCase;
import com.process.clash.application.group.port.in.JoinGroupUseCase;
import com.process.clash.application.group.port.in.QuitGroupUseCase;
import com.process.clash.application.group.port.in.UpdateGroupUseCase;
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
class GroupControllerTest {

    @Mock
    private GetAllGroupsUseCase getAllGroupsUseCase;

    @Mock
    private GetMyGroupsUseCase getMyGroupsUseCase;

    @Mock
    private CreateGroupUseCase createGroupUseCase;

    @Mock
    private UpdateGroupUseCase updateGroupUseCase;

    @Mock
    private DeleteGroupUseCase deleteGroupUseCase;

    @Mock
    private JoinGroupUseCase joinGroupUseCase;

    @Mock
    private QuitGroupUseCase quitGroupUseCase;

    @Mock
    private GetGroupActivityUseCase getGroupActivityUseCase;

    @Mock
    private GetGroupDetailUseCase getGroupDetailUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        GroupController controller = new GroupController(
            getAllGroupsUseCase,
            getMyGroupsUseCase,
            createGroupUseCase,
            updateGroupUseCase,
            deleteGroupUseCase,
            joinGroupUseCase,
            quitGroupUseCase,
            getGroupActivityUseCase,
            getGroupDetailUseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(authenticatedActorResolver())
            .build();
    }

    @Test
    @DisplayName("GET /api/groups/{groupId}/activity 는 date 쿼리를 커맨드로 전달한다")
    void getGroupActivity_passesDateToCommand() throws Exception {
        when(getGroupActivityUseCase.execute(any()))
            .thenReturn(new GetGroupActivityData.Result(List.of()));

        mockMvc.perform(get("/api/groups/10/activity").param("date", "2026-03-10"))
            .andExpect(status().isOk());

        ArgumentCaptor<GetGroupActivityData.Command> captor =
            ArgumentCaptor.forClass(GetGroupActivityData.Command.class);
        verify(getGroupActivityUseCase).execute(captor.capture());
        org.assertj.core.api.Assertions.assertThat(captor.getValue().date())
            .isEqualTo(LocalDate.of(2026, 3, 10));
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
