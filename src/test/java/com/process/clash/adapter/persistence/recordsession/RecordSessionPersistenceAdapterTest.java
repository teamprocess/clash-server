package com.process.clash.adapter.persistence.recordsession;

import com.process.clash.adapter.persistence.recordtask.RecordTaskJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordSessionPersistenceAdapterTest {

    private static final ZoneId RECORD_ZONE_ID = ZoneId.of("UTC");

    @Mock
    private RecordSessionJpaRepository recordSessionJpaRepository;

    @Mock
    private RecordSessionJpaMapper recordSessionJpaMapper;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private RecordTaskJpaRepository recordTaskJpaRepository;

    private RecordSessionPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new RecordSessionPersistenceAdapter(
            recordSessionJpaRepository,
            recordSessionJpaMapper,
            userJpaRepository,
            recordTaskJpaRepository,
            RECORD_ZONE_ID
        );
    }

    @Test
    @DisplayName("단일 사용자 합산 조회 시 recordZoneId 기준 now를 전달한다")
    void getTotalStudyTimeInSeconds_passesRecordZoneNow() {
        LocalDateTime startOfDay = LocalDateTime.of(2026, 1, 1, 6, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        when(recordSessionJpaRepository.getTotalStudyTimeInSeconds(anyLong(), any(), any(), any()))
            .thenReturn(120L);

        Instant before = Instant.now();
        adapter.getTotalStudyTimeInSeconds(1L, startOfDay, endOfDay);
        Instant after = Instant.now();

        ArgumentCaptor<Instant> nowCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(recordSessionJpaRepository)
            .getTotalStudyTimeInSeconds(
                eq(1L),
                eq(startOfDay.atZone(RECORD_ZONE_ID).toInstant()),
                eq(endOfDay.atZone(RECORD_ZONE_ID).toInstant()),
                nowCaptor.capture()
            );

        Instant nowArg = nowCaptor.getValue();
        assertThat(nowArg).isAfterOrEqualTo(before);
        assertThat(nowArg).isBeforeOrEqualTo(after);
    }

    @Test
    @DisplayName("복수 사용자 합산 조회 시 recordZoneId 기준 now를 전달한다")
    void getTotalStudyTimeInSecondsByUserIds_passesRecordZoneNow() {
        LocalDateTime startOfDay = LocalDateTime.of(2026, 1, 1, 6, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        List<Long> userIds = List.of(1L, 2L);
        when(recordSessionJpaRepository.getTotalStudyTimeInSecondsByUserIds(any(), any(), any(), any()))
            .thenReturn(List.of());

        Instant before = Instant.now();
        adapter.getTotalStudyTimeInSecondsByUserIds(userIds, startOfDay, endOfDay);
        Instant after = Instant.now();

        ArgumentCaptor<Instant> nowCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(recordSessionJpaRepository)
            .getTotalStudyTimeInSecondsByUserIds(
                eq(userIds),
                eq(startOfDay.atZone(RECORD_ZONE_ID).toInstant()),
                eq(endOfDay.atZone(RECORD_ZONE_ID).toInstant()),
                nowCaptor.capture()
            );

        Instant nowArg = nowCaptor.getValue();
        assertThat(nowArg).isAfterOrEqualTo(before);
        assertThat(nowArg).isBeforeOrEqualTo(after);
    }

    @Test
    @DisplayName("복수 사용자 합산 조회 시 ID가 비어있으면 쿼리를 호출하지 않는다")
    void getTotalStudyTimeInSecondsByUserIds_skipsWhenEmpty() {
        LocalDateTime startOfDay = LocalDateTime.of(2026, 1, 1, 6, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        adapter.getTotalStudyTimeInSecondsByUserIds(List.of(), startOfDay, endOfDay);

        verify(recordSessionJpaRepository, never())
            .getTotalStudyTimeInSecondsByUserIds(any(), any(), any(), any());
    }
}
