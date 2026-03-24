package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival;
import com.process.clash.application.compete.rival.rival.data.GetAllAbleRivalsData;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllAbleRivalsServiceTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private GetAllAbleRivalsService getAllAbleRivalsService;

    @BeforeEach
    void setUp() {
        getAllAbleRivalsService = new GetAllAbleRivalsService(rivalRepositoryPort, userRepositoryPort);
    }

    @Test
    @DisplayName("ACCEPTED 상태의 상대방은 조회 결과에서 제외된다")
    void execute_excludesAcceptedRivalUsers() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival acceptedRival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.ACCEPTED, myId, 2L);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(acceptedRival));

        getAllAbleRivalsService.execute(GetAllAbleRivalsData.Command.from(actor));

        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).findAbleRivalsWithUserInfo(captor.capture());
        assertThat(captor.getValue()).contains(2L);
    }

    @Test
    @DisplayName("PENDING 상태의 상대방은 조회 결과에서 제외된다")
    void execute_excludesPendingRivalUsers() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival pendingRival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, myId, 2L);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(pendingRival));

        getAllAbleRivalsService.execute(GetAllAbleRivalsData.Command.from(actor));

        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).findAbleRivalsWithUserInfo(captor.capture());
        assertThat(captor.getValue()).contains(2L);
    }

    @Test
    @DisplayName("상대방이 나에게 보낸 PENDING 신청도 조회 결과에서 제외된다")
    void execute_excludesPendingRivalUsers_whenOpponentApplied() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival pendingRival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, 2L, myId);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(pendingRival));

        getAllAbleRivalsService.execute(GetAllAbleRivalsData.Command.from(actor));

        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).findAbleRivalsWithUserInfo(captor.capture());
        assertThat(captor.getValue()).contains(2L);
    }

    @Test
    @DisplayName("REJECTED 상태의 상대방은 조회 결과에서 제외되지 않는다")
    void execute_doesNotExcludeRejectedRivalUsers() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival rejectedRival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.REJECTED, myId, 2L);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(rejectedRival));

        getAllAbleRivalsService.execute(GetAllAbleRivalsData.Command.from(actor));

        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).findAbleRivalsWithUserInfo(captor.capture());
        assertThat(captor.getValue()).doesNotContain(2L);
    }

    @Test
    @DisplayName("CANCELED 상태의 상대방은 조회 결과에서 제외되지 않는다")
    void execute_doesNotExcludeCanceledRivalUsers() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        Rival canceledRival = new Rival(10L, Instant.now(), Instant.now(), RivalLinkingStatus.CANCELED, myId, 2L);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of(canceledRival));

        getAllAbleRivalsService.execute(GetAllAbleRivalsData.Command.from(actor));

        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).findAbleRivalsWithUserInfo(captor.capture());
        assertThat(captor.getValue()).doesNotContain(2L);
    }

    @Test
    @DisplayName("자기 자신은 항상 조회 결과에서 제외된다")
    void execute_excludesMyself() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of());

        getAllAbleRivalsService.execute(GetAllAbleRivalsData.Command.from(actor));

        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).findAbleRivalsWithUserInfo(captor.capture());
        assertThat(captor.getValue()).contains(myId);
    }

    @Test
    @DisplayName("조회된 유저 목록을 그대로 반환한다")
    void execute_returnsAbleRivalUsers() {
        Long myId = 1L;
        Actor actor = new Actor(myId);

        AbleRivalInfoForRival user = new AbleRivalInfoForRival(3L, "user3", "홍길동", "github3", "img3");

        when(rivalRepositoryPort.findAllRivalsByUserId(myId)).thenReturn(List.of());
        when(userRepositoryPort.findAbleRivalsWithUserInfo(List.of(myId))).thenReturn(List.of(user));

        GetAllAbleRivalsData.Result result = getAllAbleRivalsService.execute(GetAllAbleRivalsData.Command.from(actor));

        assertThat(result.users()).hasSize(1);
        assertThat(result.users().get(0).id()).isEqualTo(3L);
    }
}