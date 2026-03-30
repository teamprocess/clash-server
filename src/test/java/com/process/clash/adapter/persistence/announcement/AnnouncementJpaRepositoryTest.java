package com.process.clash.adapter.persistence.announcement;

import com.process.clash.infrastructure.config.JpaAuditingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({JpaAuditingConfig.class, AnnouncementJpaMapper.class})
class AnnouncementJpaRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private AnnouncementJpaRepository announcementJpaRepository;

    @Test
    @DisplayName("현재 시각이 startAt과 endAt 사이인 공지만 반환한다")
    void findAllActive_returnsOnlyActiveAnnouncements() {
        Instant now = Instant.now();
        persistAnnouncement("활성 공지", now.minus(1, ChronoUnit.DAYS), now.plus(1, ChronoUnit.DAYS));

        List<AnnouncementJpaEntity> result = announcementJpaRepository.findAllActive(now);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("활성 공지");
    }

    @Test
    @DisplayName("만료된 공지는 반환하지 않는다")
    void findAllActive_doesNotReturnExpiredAnnouncements() {
        Instant now = Instant.now();
        persistAnnouncement("만료 공지", now.minus(2, ChronoUnit.DAYS), now.minus(1, ChronoUnit.DAYS));

        List<AnnouncementJpaEntity> result = announcementJpaRepository.findAllActive(now);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("아직 시작되지 않은 공지는 반환하지 않는다")
    void findAllActive_doesNotReturnFutureAnnouncements() {
        Instant now = Instant.now();
        persistAnnouncement("미래 공지", now.plus(1, ChronoUnit.DAYS), now.plus(2, ChronoUnit.DAYS));

        List<AnnouncementJpaEntity> result = announcementJpaRepository.findAllActive(now);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("활성 공지가 여러 개일 때 startAt 내림차순으로 정렬되어 반환된다")
    void findAllActive_returnsResultOrderedByStartAtDesc() {
        Instant now = Instant.now();
        persistAnnouncement("오래된 공지", now.minus(3, ChronoUnit.DAYS), now.plus(1, ChronoUnit.DAYS));
        persistAnnouncement("최신 공지", now.minus(1, ChronoUnit.DAYS), now.plus(2, ChronoUnit.DAYS));

        List<AnnouncementJpaEntity> result = announcementJpaRepository.findAllActive(now);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("최신 공지");
        assertThat(result.get(1).getTitle()).isEqualTo("오래된 공지");
    }

    // ─── helpers ─────────────────────────────────────────────────────────────

    private void persistAnnouncement(String title, Instant startAt, Instant endAt) {
        AnnouncementJpaEntity entity = new AnnouncementJpaEntity(
                null, null, null,
                title, "관리자", null, "테스트 공지 내용", startAt, endAt
        );
        em.persist(entity);
        em.flush();
    }
}
