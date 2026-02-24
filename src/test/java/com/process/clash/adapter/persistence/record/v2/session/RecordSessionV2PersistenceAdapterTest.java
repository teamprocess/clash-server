package com.process.clash.adapter.persistence.record.v2.session;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.adapter.persistence.record.v2.subject.RecordSubjectV2JpaRepository;
import com.process.clash.adapter.persistence.record.v2.task.RecordTaskV2JpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.infrastructure.config.RecordProperties;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordSessionV2PersistenceAdapterTest {

    private static final ZoneId RECORD_ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final int DAY_BOUNDARY_HOUR = 6;

    @Mock
    private RecordActiveSessionV2JpaRepository recordActiveSessionV2JpaRepository;
    @Mock
    private RecordTaskSessionV2JpaRepository recordTaskSessionV2JpaRepository;
    @Mock
    private RecordSubjectV2JpaRepository recordSubjectV2JpaRepository;
    @Mock
    private RecordTaskV2JpaRepository recordTaskV2JpaRepository;
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private RecordSessionV2JpaMapper recordSessionV2JpaMapper;

    private RecordSessionV2PersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new RecordSessionV2PersistenceAdapter(
            recordActiveSessionV2JpaRepository,
            recordTaskSessionV2JpaRepository,
            recordSubjectV2JpaRepository,
            recordTaskV2JpaRepository,
            userJpaRepository,
            recordSessionV2JpaMapper,
            new RecordProperties(RECORD_ZONE_ID.getId(), DAY_BOUNDARY_HOUR),
            RECORD_ZONE_ID
        );
    }

    @Test
    @DisplayName("일별 집계 조회 시 timezone/dayBoundary/now를 전달하고 projection을 row로 매핑한다")
    void findDailyStudyTimeByUserIds_passesTimezoneAndBoundaryAndMapsRows() {
        List<Long> userIds = List.of(1L, 2L);
        Instant startDate = Instant.parse("2026-02-10T00:00:00Z");
        Instant endDate = Instant.parse("2026-02-24T00:00:00Z");

        RecordActiveSessionV2JpaRepository.UserStudyTimePeriodProjectionV2 projection =
            org.mockito.Mockito.mock(RecordActiveSessionV2JpaRepository.UserStudyTimePeriodProjectionV2.class);
        when(projection.getUserId()).thenReturn(1L);
        when(projection.getRecordedDate()).thenReturn(Date.valueOf(LocalDate.of(2026, 2, 20)));
        when(projection.getPoint()).thenReturn(3600L);
        when(recordActiveSessionV2JpaRepository.findDailyStudyTimeByUserIds(
            any(),
            any(),
            any(),
            any(),
            any(),
            anyInt()
        )).thenReturn(List.of(projection));

        Instant before = Instant.now();
        List<Object[]> result = adapter.findDailyStudyTimeByUserIds(userIds, startDate, endDate);
        Instant after = Instant.now();

        ArgumentCaptor<Instant> nowCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(recordActiveSessionV2JpaRepository).findDailyStudyTimeByUserIds(
            eq(userIds),
            eq(startDate),
            eq(endDate),
            nowCaptor.capture(),
            eq(RECORD_ZONE_ID.getId()),
            eq(DAY_BOUNDARY_HOUR)
        );

        Instant nowArg = nowCaptor.getValue();
        assertThat(nowArg).isAfterOrEqualTo(before);
        assertThat(nowArg).isBeforeOrEqualTo(after);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsExactly(
            1L,
            Date.valueOf(LocalDate.of(2026, 2, 20)),
            3600L
        );
    }

    @Test
    @DisplayName("랭킹 조회 시 start/end 변환값과 now를 전달한다")
    void findStudyTimeRankingByUserIdAndPeriod_passesNow() {
        Long userId = 1L;
        LocalDateTime startDate = LocalDateTime.of(2026, 2, 24, 6, 0);
        LocalDateTime endDate = startDate.plusDays(1);

        when(recordActiveSessionV2JpaRepository.findStudyTimeRankingByUserIdAndPeriod(anyLong(), any(), any(), any()))
            .thenReturn(List.of());

        Instant before = Instant.now();
        adapter.findStudyTimeRankingByUserIdAndPeriod(userId, startDate, endDate);
        Instant after = Instant.now();

        ArgumentCaptor<Instant> nowCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(recordActiveSessionV2JpaRepository).findStudyTimeRankingByUserIdAndPeriod(
            eq(userId),
            eq(startDate.atZone(RECORD_ZONE_ID).toInstant()),
            eq(endDate.atZone(RECORD_ZONE_ID).toInstant()),
            nowCaptor.capture()
        );

        Instant nowArg = nowCaptor.getValue();
        assertThat(nowArg).isAfterOrEqualTo(before);
        assertThat(nowArg).isBeforeOrEqualTo(after);
    }
}
