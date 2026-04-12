package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.data.FindAllRivalsData;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllRivalsServiceTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private FindAllRivalsService findAllRivalsService;

    @BeforeEach
    void setUp() {
        findAllRivalsService = new FindAllRivalsService(rivalRepositoryPort, userRepositoryPort);
    }

    @Test
    @DisplayName("PENDING 상태 라이벌이 ACCEPTED보다 먼저 반환된다")
    void execute_returnsPendingRivalsBeforeAccepted() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival acceptedRival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.ACCEPTED, myId, 2L);
        Rival pendingRival = new Rival(11L, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, myId, 3L);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(acceptedRival, pendingRival));
        when(userRepositoryPort.findAllByIds(anyList())).thenReturn(List.of(
                createUser(2L, "이몽룡"),
                createUser(3L, "성춘향")
        ));

        FindAllRivalsData.Result result = findAllRivalsService.execute(FindAllRivalsData.Command.from(actor));

        assertThat(result.rivals().get(0).rivalLinkingStatus()).isEqualTo(RivalLinkingStatus.PENDING);
        assertThat(result.rivals().get(1).rivalLinkingStatus()).isEqualTo(RivalLinkingStatus.ACCEPTED);
        assertThat(result.rivals().get(0).tier()).isEqualTo("UNRANKED");
    }

    @Test
    @DisplayName("PENDING 상태 라이벌이 REJECTED보다 먼저 반환된다")
    void execute_returnsPendingRivalsBeforeRejected() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival rejectedRival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.REJECTED, myId, 2L);
        Rival pendingRival = new Rival(11L, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, myId, 3L);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(rejectedRival, pendingRival));
        when(userRepositoryPort.findAllByIds(anyList())).thenReturn(List.of(
                createUser(2L, "이몽룡"),
                createUser(3L, "성춘향")
        ));

        FindAllRivalsData.Result result = findAllRivalsService.execute(FindAllRivalsData.Command.from(actor));

        assertThat(result.rivals().get(0).rivalLinkingStatus()).isEqualTo(RivalLinkingStatus.PENDING);
        assertThat(result.rivals().get(1).rivalLinkingStatus()).isEqualTo(RivalLinkingStatus.REJECTED);
    }

    @Test
    @DisplayName("PENDING 상태가 여러 개이면 모두 앞에 위치한다")
    void execute_returnsAllPendingRivalsFirst() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival acceptedRival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.ACCEPTED, myId, 2L);
        Rival pendingRival1 = new Rival(11L, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, myId, 3L);
        Rival pendingRival2 = new Rival(12L, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, myId, 4L);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(acceptedRival, pendingRival1, pendingRival2));
        when(userRepositoryPort.findAllByIds(anyList())).thenReturn(List.of(
                createUser(2L, "이몽룡"),
                createUser(3L, "성춘향"),
                createUser(4L, "홍길동")
        ));

        FindAllRivalsData.Result result = findAllRivalsService.execute(FindAllRivalsData.Command.from(actor));

        assertThat(result.rivals().subList(0, 2))
                .extracting(FindAllRivalsData.RivalInfo::rivalLinkingStatus)
                .containsOnly(RivalLinkingStatus.PENDING);
        assertThat(result.rivals().get(2).rivalLinkingStatus()).isEqualTo(RivalLinkingStatus.ACCEPTED);
    }

    @Test
    @DisplayName("rankTier가 NONE이면 expTier를 tier로 반환한다")
    void execute_returnsTierAsExpTier_whenRankTierIsNone() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival rival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.ACCEPTED, myId, 2L);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(rival));
        when(userRepositoryPort.findAllByIds(anyList())).thenReturn(List.of(
                createUser(2L, "이몽룡", RankTier.NONE, ExpTier.GOLD)
        ));

        FindAllRivalsData.Result result = findAllRivalsService.execute(FindAllRivalsData.Command.from(actor));

        assertThat(result.rivals().get(0).tier()).isEqualTo("GOLD");
    }

    @Test
    @DisplayName("rankTier가 NONE이 아니면 rankTier를 tier로 반환한다")
    void execute_returnsTierAsRankTier_whenRankTierIsNotNone() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival rival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.ACCEPTED, myId, 2L);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(rival));
        when(userRepositoryPort.findAllByIds(anyList())).thenReturn(List.of(
                createUser(2L, "이몽룡", RankTier.MASTER, ExpTier.DIAMOND)
        ));

        FindAllRivalsData.Result result = findAllRivalsService.execute(FindAllRivalsData.Command.from(actor));

        assertThat(result.rivals().get(0).tier()).isEqualTo("MASTER");
    }

    private User createUser(Long id, String name) {
        return createUser(id, name, RankTier.NONE, ExpTier.UNRANKED);
    }

    private User createUser(Long id, String name, RankTier rankTier, ExpTier expTier) {
        return new User(
                id, Instant.now(), Instant.now(),
                name, name + "@test.com", name,
                "password", Role.USER, "",
                0, 0, Major.NONE, UserStatus.ACTIVE,
                null, false, rankTier, expTier
        );
    }
}