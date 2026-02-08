package com.process.clash.application.realtime.service;

import com.process.clash.application.realtime.event.RefetchEvent;
import com.process.clash.application.realtime.port.out.BroadcastRefetchPort;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RefetchEventListener {

    private final BroadcastRefetchPort broadcastRefetchPort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onRefetchEvent(RefetchEvent event) {
        if (event == null || event.notice() == null || event.userIds() == null) {
            return;
        }
        Collection<Long> userIds = event.userIds();
        if (userIds.isEmpty()) {
            return;
        }
        broadcastRefetchPort.broadcastRefetchToUsers(event.notice(), userIds);
    }
}
