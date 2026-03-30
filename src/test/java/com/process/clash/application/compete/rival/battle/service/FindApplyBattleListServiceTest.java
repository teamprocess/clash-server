package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.data.FindApplyBattleListData;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindApplyBattleListServiceTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private FindApplyBattleListService findApplyBattleListService;

    @BeforeEach
    void setUp() {
        findApplyBattleListService = new FindApplyBattleListService(
                battleRepositoryPort,
                rivalRepositoryPort,
                userRepositoryPort
        );
    }

    @Test
    @DisplayName("PENDING 배틀이 없으면 빈 목록을 반환한다")
    void execute_returnsEmptyList_whenNoPendingBattles() {
        Actor actor = new Actor(1L);
        when(battleRepositoryPort.findPendingBattlesByUserId(actor.id())).thenReturn(List.of());

        FindApplyBattleListData.Result result = findApplyBattleListService.execute(
                FindApplyBattleListData.Command.from(actor)
        );

        assertThat(result.battles()).isEmpty();
    }

    @Test
    @DisplayName("내가 신청한 PENDING 배틀이면 isMine이 true이다")
    void execute_returnsIsMineTrue_whenCurrentUserIsApplicant() {
        Long myId = 1L;
        Long opponentId = 2L;
        Long rivalId = 10L;
        Long battleId = 100L;
        Actor actor = new Actor(myId);

        Battle pendingBattle = pendingBattle(battleId, rivalId, myId, 7);
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, myId, opponentId);
        User opponent = createUser(opponentId, "이몽룡", "mongryong", "https://img.example.com/2.png");

        when(battleRepositoryPort.findPendingBattlesByUserId(myId)).thenReturn(List.of(pendingBattle));
        when(rivalRepositoryPort.findByIdIn(Set.of(rivalId))).thenReturn(List.of(rival));
        when(userRepositoryPort.findByIdIn(Set.of(opponentId))).thenReturn(List.of(opponent));

        FindApplyBattleListData.Result result = findApplyBattleListService.execute(
                FindApplyBattleListData.Command.from(actor)
        );

        assertThat(result.battles()).hasSize(1);
        assertThat(result.battles().get(0).isMine()).isTrue();
    }

    @Test
    @DisplayName("상대방이 신청한 PENDING 배틀이면 isMine이 false이다")
    void execute_returnsIsMineFalse_whenOpponentIsApplicant() {
        Long myId = 1L;
        Long opponentId = 2L;
        Long rivalId = 10L;
        Long battleId = 100L;
        Actor actor = new Actor(myId);

        Battle pendingBattle = pendingBattle(battleId, rivalId, opponentId, 7);
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, myId, opponentId);
        User opponent = createUser(opponentId, "이몽룡", "mongryong", "https://img.example.com/2.png");

        when(battleRepositoryPort.findPendingBattlesByUserId(myId)).thenReturn(List.of(pendingBattle));
        when(rivalRepositoryPort.findByIdIn(Set.of(rivalId))).thenReturn(List.of(rival));
        when(userRepositoryPort.findByIdIn(Set.of(opponentId))).thenReturn(List.of(opponent));

        FindApplyBattleListData.Result result = findApplyBattleListService.execute(
                FindApplyBattleListData.Command.from(actor)
        );

        assertThat(result.battles()).hasSize(1);
        assertThat(result.battles().get(0).isMine()).isFalse();
    }

    @Test
    @DisplayName("배틀 정보에 상대방 정보와 duration이 올바르게 담긴다")
    void execute_returnsBattleInfoWithCorrectEnemyAndDuration() {
        Long myId = 1L;
        Long opponentId = 2L;
        Long rivalId = 10L;
        Long battleId = 100L;
        int duration = 7;
        Actor actor = new Actor(myId);

        Battle pendingBattle = pendingBattle(battleId, rivalId, myId, duration);
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, myId, opponentId);
        User opponent = createUser(opponentId, "이몽룡", "mongryong", "https://img.example.com/2.png");

        when(battleRepositoryPort.findPendingBattlesByUserId(myId)).thenReturn(List.of(pendingBattle));
        when(rivalRepositoryPort.findByIdIn(Set.of(rivalId))).thenReturn(List.of(rival));
        when(userRepositoryPort.findByIdIn(Set.of(opponentId))).thenReturn(List.of(opponent));

        FindApplyBattleListData.Result result = findApplyBattleListService.execute(
                FindApplyBattleListData.Command.from(actor)
        );

        FindApplyBattleListData.BattleApplyInfo info = result.battles().get(0);
        assertThat(info.id()).isEqualTo(battleId);
        assertThat(info.duration()).isEqualTo(duration);
        assertThat(info.enemy().id()).isEqualTo(opponentId);
        assertThat(info.enemy().name()).isEqualTo("이몽룡");
        assertThat(info.enemy().profileImage()).isEqualTo("https://img.example.com/2.png");
    }

    @Test
    @DisplayName("PENDING 배틀이 여러 개이면 모두 반환한다")
    void execute_returnsAllPendingBattles_whenMultipleExist() {
        Long myId = 1L;
        Long opponent1Id = 2L;
        Long opponent2Id = 3L;
        Long rival1Id = 10L;
        Long rival2Id = 11L;
        Actor actor = new Actor(myId);

        Battle battle1 = pendingBattle(100L, rival1Id, myId, 7);
        Battle battle2 = pendingBattle(101L, rival2Id, opponent2Id, 5);

        Rival rival1 = new Rival(rival1Id, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, myId, opponent1Id);
        Rival rival2 = new Rival(rival2Id, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, myId, opponent2Id);

        User user1 = createUser(opponent1Id, "이몽룡", "mongryong", "https://img.example.com/2.png");
        User user2 = createUser(opponent2Id, "성춘향", "chunhyang", "https://img.example.com/3.png");

        when(battleRepositoryPort.findPendingBattlesByUserId(myId)).thenReturn(List.of(battle1, battle2));
        when(rivalRepositoryPort.findByIdIn(Set.of(rival1Id, rival2Id))).thenReturn(List.of(rival1, rival2));
        when(userRepositoryPort.findByIdIn(anySet())).thenReturn(List.of(user1, user2));

        FindApplyBattleListData.Result result = findApplyBattleListService.execute(
                FindApplyBattleListData.Command.from(actor)
        );

        assertThat(result.battles()).hasSize(2);
        assertThat(result.battles())
                .extracting(FindApplyBattleListData.BattleApplyInfo::id)
                .containsExactlyInAnyOrder(100L, 101L);
    }

    private Battle pendingBattle(Long battleId, Long rivalId, Long applicantId, int duration) {
        return new Battle(battleId, Instant.now(), Instant.now(),
                null, null, duration,
                BattleStatus.PENDING, null, rivalId, applicantId);
    }

    private User createUser(Long id, String name, String username, String profileImage) {
        return new User(
                id, Instant.now(), Instant.now(),
                username, username + "@test.com", name,
                "password", Role.USER, profileImage,
                0, 0, Major.NONE, UserStatus.ACTIVE,
                null, RankTier.NONE, ExpTier.UNRANKED
        );
    }
}
