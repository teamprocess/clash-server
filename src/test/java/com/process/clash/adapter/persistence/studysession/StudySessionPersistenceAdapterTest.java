package com.process.clash.adapter.persistence.studysession;

import com.process.clash.adapter.persistence.task.TaskJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudySessionPersistenceAdapterTest {

    private static final ZoneId RECORD_ZONE_ID = ZoneId.of("UTC");

    @Mock
    private StudySessionJpaRepository studySessionJpaRepository;

    @Mock
    private StudySessionJpaMapper studySessionJpaMapper;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private TaskJpaRepository taskJpaRepository;

    private StudySessionPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new StudySessionPersistenceAdapter(
            studySessionJpaRepository,
            studySessionJpaMapper,
            userJpaRepository,
            taskJpaRepository,
            RECORD_ZONE_ID
        );
    }

    @Test
    @DisplayName("단일 사용자 합산 조회 시 recordZoneId 기준 now를 전달한다")
    void getTotalStudyTimeInSeconds_passesRecordZoneNow() {
        LocalDateTime startOfDay = LocalDateTime.of(2026, 1, 1, 6, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        when(studySessionJpaRepository.getTotalStudyTimeInSeconds(anyLong(), any(), any(), any()))
            .thenReturn(120L);

        LocalDateTime before = LocalDateTime.now(RECORD_ZONE_ID);
        adapter.getTotalStudyTimeInSeconds(1L, startOfDay, endOfDay);
        LocalDateTime after = LocalDateTime.now(RECORD_ZONE_ID);

        ArgumentCaptor<LocalDateTime> nowCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(studySessionJpaRepository)
            .getTotalStudyTimeInSeconds(eq(1L), eq(startOfDay), eq(endOfDay), nowCaptor.capture());

        LocalDateTime nowArg = nowCaptor.getValue();
        assertThat(nowArg).isAfterOrEqualTo(before);
        assertThat(nowArg).isBeforeOrEqualTo(after);
    }

    @Test
    @DisplayName("복수 사용자 합산 조회 시 recordZoneId 기준 now를 전달한다")
    void getTotalStudyTimeInSecondsByUserIds_passesRecordZoneNow() {
        LocalDateTime startOfDay = LocalDateTime.of(2026, 1, 1, 6, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        List<Long> userIds = List.of(1L, 2L);
        when(studySessionJpaRepository.getTotalStudyTimeInSecondsByUserIds(any(), any(), any(), any()))
            .thenReturn(List.of());

        LocalDateTime before = LocalDateTime.now(RECORD_ZONE_ID);
        adapter.getTotalStudyTimeInSecondsByUserIds(userIds, startOfDay, endOfDay);
        LocalDateTime after = LocalDateTime.now(RECORD_ZONE_ID);

        ArgumentCaptor<LocalDateTime> nowCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(studySessionJpaRepository)
            .getTotalStudyTimeInSecondsByUserIds(eq(userIds), eq(startOfDay), eq(endOfDay), nowCaptor.capture());

        LocalDateTime nowArg = nowCaptor.getValue();
        assertThat(nowArg).isAfterOrEqualTo(before);
        assertThat(nowArg).isBeforeOrEqualTo(after);
    }

    @Test
    @DisplayName("복수 사용자 합산 조회 시 ID가 비어있으면 쿼리를 호출하지 않는다")
    void getTotalStudyTimeInSecondsByUserIds_skipsWhenEmpty() {
        LocalDateTime startOfDay = LocalDateTime.of(2026, 1, 1, 6, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        adapter.getTotalStudyTimeInSecondsByUserIds(List.of(), startOfDay, endOfDay);

        verify(studySessionJpaRepository, never())
            .getTotalStudyTimeInSecondsByUserIds(any(), any(), any(), any());
    }
}
