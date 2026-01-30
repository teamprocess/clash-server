package com.process.clash.application.group.realtime;

import com.process.clash.application.realtime.data.ChangeDomain;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.service.RefetchEventPublisher;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupRefetchNotifier {

    private final RefetchEventPublisher refetchEventPublisher;

    public void notifyGroupsChanged(Collection<Long> userIds) {
        publish(ChangeType.DATA_CHANGED, userIds);
    }

    public void notifyMyGroupsChanged(Collection<Long> userIds) {
        publish(ChangeType.DATA_CHANGED, userIds);
    }

    public void notifyGroupDetailChanged(Collection<Long> userIds) {
        publish(ChangeType.UPDATED, userIds);
    }

    public void notifyGroupActivityChanged(Collection<Long> userIds) {
        publish(ChangeType.STATUS_CHANGED, userIds);
    }

    public void notifyGroupActivityStarted(Collection<Long> userIds) {
        publish(ChangeType.ACTIVITY_STARTED, userIds);
    }

    public void notifyGroupActivityStopped(Collection<Long> userIds) {
        publish(ChangeType.ACTIVITY_STOPPED, userIds);
    }

    private void publish(ChangeType type, Collection<Long> userIds) {
        refetchEventPublisher.publish(RefetchNotice.of(ChangeDomain.GROUP, type), userIds);
    }
}
