package com.process.clash.application.user.userattendance.realtime;

import com.process.clash.application.realtime.data.ChangeDomain;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.service.RefetchEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class AttendanceRefetchNotifier {

    private final RefetchEventPublisher refetchEventPublisher;

    /**
     * 전날 출석을 놓친 유저들에게 주간 출석 데이터가 변경됐음을 소켓으로 알립니다.
     */
    public void notifyAttendanceMissed(Collection<Long> userIds) {
        refetchEventPublisher.publish(
                RefetchNotice.of(ChangeDomain.ATTENDANCE, ChangeType.DATA_CHANGED),
                userIds
        );
    }
}