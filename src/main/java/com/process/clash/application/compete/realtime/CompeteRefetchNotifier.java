package com.process.clash.application.compete.realtime;

import com.process.clash.application.realtime.data.ChangeDomain;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.service.RefetchEventPublisher;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompeteRefetchNotifier {

    private final RefetchEventPublisher refetchEventPublisher;

    public void notifyRivalActivityStarted(Collection<Long> userIds) {
        publish(ChangeType.ACTIVITY_STARTED, userIds);
    }

    public void notifyRivalActivityStopped(Collection<Long> userIds) {
        publish(ChangeType.ACTIVITY_STOPPED, userIds);
    }

    private void publish(ChangeType type, Collection<Long> userIds) {
        refetchEventPublisher.publish(RefetchNotice.of(ChangeDomain.COMPETE, type), userIds);
    }
}
