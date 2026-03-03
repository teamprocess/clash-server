package com.process.clash.adapter.persistence.recordsession;

import static org.assertj.core.api.Assertions.assertThat;

import com.process.clash.adapter.persistence.rival.rival.RivalJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.infrastructure.config.JpaAuditingConfig;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * RecordSessionJpaRepository.findStudyTimeRankingByUserIdAndPeriod 쿼리 검증 테스트.
 *
 * <p>H2 PostgreSQL 호환 모드를 사용하므로, 네이티브 쿼리의 EXTRACT(EPOCH FROM ...) 함수가
 * 정상적으로 동작하지 않을 경우 Testcontainers(PostgreSQL)로 전환해야 합니다.
 */
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:rankingtest;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;NON_KEYWORDS=VALUE"
})
@Import({JpaAuditingConfig.class, UserJpaMapper.class})
class RecordSessionJpaRepositoryRankingTest {

    @Autowired private TestEntityManager em;
    @Autowired private RecordSessionJpaRepository repository;
    @Autowired private UserJpaMapper userJpaMapper;

    private static final Instant PERIOD_START = Instant.parse("2026-02-01T00:00:00Z");
    private static final Instant PERIOD_END   = Instant.parse("2026-02-28T00:00:00Z");
    private static final Instant NOW          = Instant.parse("2026-03-01T00:00:00Z");

    private int counter = 0;

    private UserJpaEntity persistUser(String label) {
        counter++;
        String suffix = label + counter;
        User user = new User(
            null, Instant.now(), Instant.now(),
            "user_" + suffix, "user_" + suffix + "@test.com", "유저" + label,
            "pw", Role.USER, "", 0, 0, Major.NONE, UserStatus.PENDING, null
        );
        UserJpaEntity entity = userJpaMapper.toJpaEntity(user);
        em.persist(entity);
        return entity;
    }

    private void persistSession(UserJpaEntity user, Instant startedAt, Instant endedAt) {
        RecordSessionJpaEntity session = RecordSessionJpaEntity.create(
            user, null, RecordType.TASK, null, startedAt
        );
        session.changeEndedAt(endedAt);
        em.persist(session);
    }

    private void persistRival(UserJpaEntity first, UserJpaEntity second) {
        RivalJpaEntity rival = new RivalJpaEntity(
            null, first, second, RivalLinkingStatus.ACCEPTED, Instant.now(), Instant.now()
        );
        em.persist(rival);
    }

    @Test
    @DisplayName("라이벌이 여러 명이어도 요청자의 학습시간이 중복 집계되지 않는다")
    void studyTime_notMultipliedByRivalCount() {
        UserJpaEntity userA = persistUser("A");
        UserJpaEntity userB = persistUser("B");
        UserJpaEntity userC = persistUser("C");

        // userA ↔ userB, userA ↔ userC (두 개의 라이벌 관계)
        persistRival(userA, userB);
        persistRival(userA, userC);

        // userA: 1시간 세션 1개
        persistSession(userA,
            Instant.parse("2026-02-10T09:00:00Z"),
            Instant.parse("2026-02-10T10:00:00Z")
        );

        em.flush();
        em.clear();

        List<UserRanking> rankings = repository.findStudyTimeRankingByUserIdAndPeriod(
            userA.getId(), PERIOD_START, PERIOD_END, NOW
        );

        UserRanking rankA = rankings.stream()
            .filter(r -> r.userId().equals(userA.getId()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("userA가 랭킹에 없습니다"));

        // 라이벌이 2명이어도 학습시간은 1시간(3600초)이어야 한다 (×2 되면 안 됨)
        assertThat(rankA.point()).isEqualTo(3600L);
    }

    @Test
    @DisplayName("라이벌인 사용자는 isRival=true를 반환한다")
    void isRival_trueForRivalUser() {
        UserJpaEntity requester = persistUser("Req");
        UserJpaEntity rival     = persistUser("Rival");

        persistRival(requester, rival);

        persistSession(rival,
            Instant.parse("2026-02-10T09:00:00Z"),
            Instant.parse("2026-02-10T10:00:00Z")
        );

        em.flush();
        em.clear();

        List<UserRanking> rankings = repository.findStudyTimeRankingByUserIdAndPeriod(
            requester.getId(), PERIOD_START, PERIOD_END, NOW
        );

        UserRanking rivalRanking = rankings.stream()
            .filter(r -> r.userId().equals(rival.getId()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("라이벌 유저가 랭킹에 없습니다"));

        assertThat(rivalRanking.isRival()).isTrue();
    }

    @Test
    @DisplayName("라이벌이 아닌 사용자는 isRival=false를 반환한다")
    void isRival_falseForNonRivalUser() {
        UserJpaEntity requester  = persistUser("Req");
        UserJpaEntity normalUser = persistUser("Normal");

        persistSession(normalUser,
            Instant.parse("2026-02-10T09:00:00Z"),
            Instant.parse("2026-02-10T10:00:00Z")
        );

        em.flush();
        em.clear();

        List<UserRanking> rankings = repository.findStudyTimeRankingByUserIdAndPeriod(
            requester.getId(), PERIOD_START, PERIOD_END, NOW
        );

        UserRanking normalRanking = rankings.stream()
            .filter(r -> r.userId().equals(normalUser.getId()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("일반 유저가 랭킹에 없습니다"));

        assertThat(normalRanking.isRival()).isFalse();
    }

    @Test
    @DisplayName("요청자 본인은 라이벌이 있어도 isRival=false를 반환한다")
    void isRival_falseForRequesterThemselves() {
        UserJpaEntity requester = persistUser("Self");
        UserJpaEntity rival     = persistUser("Rival");

        persistRival(requester, rival);

        // requester 본인의 세션
        persistSession(requester,
            Instant.parse("2026-02-10T09:00:00Z"),
            Instant.parse("2026-02-10T10:00:00Z")
        );

        em.flush();
        em.clear();

        List<UserRanking> rankings = repository.findStudyTimeRankingByUserIdAndPeriod(
            requester.getId(), PERIOD_START, PERIOD_END, NOW
        );

        UserRanking selfRanking = rankings.stream()
            .filter(r -> r.userId().equals(requester.getId()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("요청자가 랭킹에 없습니다"));

        assertThat(selfRanking.isRival()).isFalse();
    }

    @Test
    @DisplayName("기간 외 세션은 집계에서 제외된다")
    void sessionsOutsidePeriodAreExcluded() {
        UserJpaEntity user = persistUser("OutOfRange");

        // 기간(2월) 이전 세션
        persistSession(user,
            Instant.parse("2026-01-01T09:00:00Z"),
            Instant.parse("2026-01-01T10:00:00Z")
        );

        em.flush();
        em.clear();

        List<UserRanking> rankings = repository.findStudyTimeRankingByUserIdAndPeriod(
            user.getId(), PERIOD_START, PERIOD_END, NOW
        );

        assertThat(rankings).noneMatch(r -> r.userId().equals(user.getId()));
    }

    @Test
    @DisplayName("학습시간이 많을수록 랭킹 상위에 위치한다")
    void rankings_orderedByStudyTimeDescending() {
        UserJpaEntity highUser = persistUser("High");
        UserJpaEntity lowUser  = persistUser("Low");

        // highUser: 2시간
        persistSession(highUser,
            Instant.parse("2026-02-10T08:00:00Z"),
            Instant.parse("2026-02-10T10:00:00Z")
        );
        // lowUser: 30분
        persistSession(lowUser,
            Instant.parse("2026-02-10T09:00:00Z"),
            Instant.parse("2026-02-10T09:30:00Z")
        );

        em.flush();
        em.clear();

        List<UserRanking> rankings = repository.findStudyTimeRankingByUserIdAndPeriod(
            highUser.getId(), PERIOD_START, PERIOD_END, NOW
        );

        List<Long> orderedIds = rankings.stream().map(UserRanking::userId).toList();
        assertThat(orderedIds.indexOf(highUser.getId()))
            .isLessThan(orderedIds.indexOf(lowUser.getId()));
    }
}
