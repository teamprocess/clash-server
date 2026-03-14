package com.process.clash.application.ranking.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.ranking.data.GetChapterRankingData;
import com.process.clash.application.ranking.port.out.LoadChapterRankingPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetChapterRankingServiceTest {

    @Mock
    private LoadChapterRankingPort loadChapterRankingPort;

    private GetChapterRankingService getChapterRankingService;

    private static final Long MY_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        getChapterRankingService = new GetChapterRankingService(loadChapterRankingPort);
    }

    @Test
    @DisplayName("rankTier가 NONE이면 expTier를 tier로 반환한다")
    void execute_returnsTierAsExpTier_whenRankTierIsNone() {
        List<Object[]> rows = new ArrayList<>();
        rows.add(record(MY_USER_ID, 1, 10, "NONE", "GOLD"));
        when(loadChapterRankingPort.loadRankingsWithMyRank(MY_USER_ID)).thenReturn(rows);

        GetChapterRankingData.Result result = getChapterRankingService.execute(
                GetChapterRankingData.Command.from(new Actor(MY_USER_ID)));

        assertThat(result.myRank().tier()).isEqualTo("GOLD");
        assertThat(result.allRankers().get(0).tier()).isEqualTo("GOLD");
    }

    @Test
    @DisplayName("rankTier가 NONE이 아니면 rankTier를 tier로 반환한다")
    void execute_returnsTierAsRankTier_whenRankTierIsNotNone() {
        List<Object[]> rows = new ArrayList<>();
        rows.add(record(MY_USER_ID, 1, 10, "MASTER", "DIAMOND"));
        when(loadChapterRankingPort.loadRankingsWithMyRank(MY_USER_ID)).thenReturn(rows);

        GetChapterRankingData.Result result = getChapterRankingService.execute(
                GetChapterRankingData.Command.from(new Actor(MY_USER_ID)));

        assertThat(result.myRank().tier()).isEqualTo("MASTER");
        assertThat(result.allRankers().get(0).tier()).isEqualTo("MASTER");
    }

    @Test
    @DisplayName("현재 유저의 랭킹 정보가 myRank에 포함된다")
    void execute_includesCurrentUserInMyRank() {
        Long otherUserId = 2L;
        List<Object[]> rows = new ArrayList<>();
        rows.add(record(MY_USER_ID, 3, 20, "NONE", "SILVER"));
        rows.add(record(otherUserId, 1, 42, "NONE", "GOLD"));
        when(loadChapterRankingPort.loadRankingsWithMyRank(MY_USER_ID)).thenReturn(rows);

        GetChapterRankingData.Result result = getChapterRankingService.execute(
                GetChapterRankingData.Command.from(new Actor(MY_USER_ID)));

        assertThat(result.myRank().id()).isEqualTo(MY_USER_ID);
        assertThat(result.myRank().rank()).isEqualTo(3);
        assertThat(result.myRank().tier()).isEqualTo("SILVER");
    }

    @Test
    @DisplayName("상위 20명만 allRankers에 포함된다")
    void execute_includesOnlyTop20InAllRankers() {
        List<Object[]> records = new ArrayList<>();
        for (int rank = 1; rank <= 25; rank++) {
            records.add(record(100L + rank, rank, 50 - rank, "NONE", "BRONZE"));
        }
        // 현재 유저는 21위
        records.add(record(MY_USER_ID, 21, 5, "NONE", "UNRANKED"));

        when(loadChapterRankingPort.loadRankingsWithMyRank(MY_USER_ID)).thenReturn(records);

        GetChapterRankingData.Result result = getChapterRankingService.execute(
                GetChapterRankingData.Command.from(new Actor(MY_USER_ID)));

        assertThat(result.allRankers()).hasSize(20);
        assertThat(result.myRank().rank()).isEqualTo(21);
    }

    @Test
    @DisplayName("현재 유저가 21위 이상이면 myRank는 존재하지만 allRankers에는 포함되지 않는다")
    void execute_myRankNotInAllRankers_whenRankAbove20() {
        List<Object[]> records = new ArrayList<>();
        for (int rank = 1; rank <= 20; rank++) {
            records.add(record(100L + rank, rank, 50 - rank, "NONE", "BRONZE"));
        }
        records.add(record(MY_USER_ID, 21, 1, "NONE", "UNRANKED"));

        when(loadChapterRankingPort.loadRankingsWithMyRank(MY_USER_ID)).thenReturn(records);

        GetChapterRankingData.Result result = getChapterRankingService.execute(
                GetChapterRankingData.Command.from(new Actor(MY_USER_ID)));

        assertThat(result.myRank().id()).isEqualTo(MY_USER_ID);
        assertThat(result.allRankers()).hasSize(20);
        assertThat(result.allRankers())
                .extracting(GetChapterRankingData.RankersVo::id)
                .doesNotContain(MY_USER_ID);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Object[] record(Long userId, int rank, int completedCount,
                            String rankTier, String expTier) {
        return new Object[]{userId, "user" + userId, "https://img/" + userId,
                completedCount, rank, rankTier, expTier};
    }
}