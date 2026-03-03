package com.process.clash.application.record.realtime;

import com.process.clash.application.realtime.data.ChangeDomain;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.service.RefetchEventPublisher;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordRefetchNotifier {

    private final RefetchEventPublisher refetchEventPublisher;

    public void notifyRecordActivityStarted(Collection<Long> userIds) {
        publish(ChangeType.ACTIVITY_STARTED, userIds);
    }

    public void notifyRecordActivityStopped(Collection<Long> userIds) {
        publish(ChangeType.ACTIVITY_STOPPED, userIds);
    }

    public void notifyRecordSessionChanged(Collection<Long> userIds) {
        publish(ChangeType.DATA_CHANGED, userIds);
    }

    private void publish(ChangeType type, Collection<Long> userIds) {
        refetchEventPublisher.publish(RefetchNotice.of(ChangeDomain.RECORD, type), userIds);
    }
}
