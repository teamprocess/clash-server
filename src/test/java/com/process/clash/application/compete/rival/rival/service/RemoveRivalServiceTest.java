package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.policy.RemoveRivalPolicy;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveRivalServiceTest {

    @Mock
    private RemoveRivalPolicy removeRivalPolicy;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    private RemoveRivalService removeRivalService;

    @BeforeEach
    void setUp() {
        removeRivalService = new RemoveRivalService(removeRivalPolicy, rivalRepositoryPort);
    }

    @Test
    @DisplayName("라이벌 관계 당사자면 삭제할 수 있다")
    void execute_deletesWhenActorBelongsToRival() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.ACCEPTED, actor.id(), 2L);

        when(removeRivalPolicy.check(rivalId)).thenReturn(rival);

        removeRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(rivalRepositoryPort).deleteById(rivalId);
    }

    @Test
    @DisplayName("라이벌 관계에 속하지 않은 사용자는 삭제할 수 없다")
    void execute_throwsWhenActorDoesNotBelongToRival() {
        Actor actor = new Actor(3L);
        Long rivalId = 10L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.ACCEPTED, 1L, 2L);

        when(removeRivalPolicy.check(rivalId)).thenReturn(rival);

        assertThatThrownBy(() -> removeRivalService.execute(ModifyRivalData.Command.of(actor, rivalId)))
                .isInstanceOf(RivalNotFoundException.class);

        verify(rivalRepositoryPort, never()).deleteById(rivalId);
    }
}
