package com.process.clash.adapter.scheduler;

import com.process.clash.application.record.service.RecordDayBoundaryService;
import com.process.clash.application.record.v2.service.RecordDayBoundaryV2Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RecordDayBoundarySchedulerTest {

    @Test
    @DisplayName("정기 스케줄러는 v1/v2 경계 롤오버를 모두 호출한다")
    void rolloverActiveSessions_callsV1AndV2Rollover() {
        RecordDayBoundaryService recordDayBoundaryService = mock(RecordDayBoundaryService.class);
        RecordDayBoundaryV2Service recordDayBoundaryV2Service = mock(RecordDayBoundaryV2Service.class);
        RecordDayBoundaryScheduler scheduler = new RecordDayBoundaryScheduler(
            recordDayBoundaryService,
            recordDayBoundaryV2Service
        );

        scheduler.rolloverActiveSessions();

        verify(recordDayBoundaryService, times(1)).rolloverActiveSessionsAtBoundary();
        verify(recordDayBoundaryV2Service, times(1)).rolloverActiveSessionsAtBoundary();
    }

    @Test
    @DisplayName("시작 시 롤오버도 v1/v2 경계 롤오버를 모두 호출한다")
    void rolloverActiveSessionsOnStartup_callsV1AndV2Rollover() {
        RecordDayBoundaryService recordDayBoundaryService = mock(RecordDayBoundaryService.class);
        RecordDayBoundaryV2Service recordDayBoundaryV2Service = mock(RecordDayBoundaryV2Service.class);
        RecordDayBoundaryScheduler scheduler = new RecordDayBoundaryScheduler(
            recordDayBoundaryService,
            recordDayBoundaryV2Service
        );

        scheduler.rolloverActiveSessionsOnStartup();

        verify(recordDayBoundaryService, times(1)).rolloverActiveSessionsAtBoundary();
        verify(recordDayBoundaryV2Service, times(1)).rolloverActiveSessionsAtBoundary();
    }
}
