package com.process.clash.adapter.persistence.record.v2.segment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.adapter.persistence.record.v2.session.RecordDevelopSessionV2JpaRepository;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort.AppActivityTotal;
import com.process.clash.domain.record.enums.MonitoredApp;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordDevelopSessionSegmentV2PersistenceAdapterTest {

    @Mock
    private RecordDevelopSessionSegmentV2JpaRepository recordDevelopSessionSegmentV2JpaRepository;

    @Mock
    private RecordDevelopSessionV2JpaRepository recordDevelopSessionV2JpaRepository;

    @Mock
    private RecordDevelopSessionSegmentV2JpaMapper recordDevelopSessionSegmentV2JpaMapper;

    private RecordDevelopSessionSegmentV2PersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new RecordDevelopSessionSegmentV2PersistenceAdapter(
            recordDevelopSessionSegmentV2JpaRepository,
            recordDevelopSessionV2JpaRepository,
            recordDevelopSessionSegmentV2JpaMapper
        );
    }

    @Test
    @DisplayName("집계 조회 시 유효하지 않은 app_id는 건너뛴다")
    void findAppActivityTotalsByUserIdAndRange_skipsInvalidAppIds() {
        Long userId = 1L;
        Instant startTime = Instant.parse("2026-02-01T00:00:00Z");
        Instant endTime = Instant.parse("2026-02-08T00:00:00Z");
        Instant now = Instant.parse("2026-02-08T00:00:00Z");

        when(recordDevelopSessionSegmentV2JpaRepository.findAppActivityTotalsByUserIdAndRange(
            userId,
            startTime,
            endTime,
            now
        )).thenReturn(List.of(
            projection("VSCODE", 120L),
            projection("UNKNOWN_APP", 300L),
            projection("", 400L),
            projection("INTELLIJ_IDEA", 600L)
        ));

        List<AppActivityTotal> result = adapter.findAppActivityTotalsByUserIdAndRange(
            userId,
            startTime,
            endTime,
            now
        );

        assertThat(result).containsExactly(
            new AppActivityTotal(MonitoredApp.VSCODE, 120L),
            new AppActivityTotal(MonitoredApp.INTELLIJ_IDEA, 600L)
        );
        verify(recordDevelopSessionSegmentV2JpaRepository).findAppActivityTotalsByUserIdAndRange(
            userId,
            startTime,
            endTime,
            now
        );
    }

    private RecordDevelopSessionSegmentV2JpaRepository.AppActivityTotalProjection projection(
        String appId,
        Long totalSeconds
    ) {
        return new RecordDevelopSessionSegmentV2JpaRepository.AppActivityTotalProjection() {
            @Override
            public String getAppId() {
                return appId;
            }

            @Override
            public Long getTotalSeconds() {
                return totalSeconds;
            }
        };
    }
}
