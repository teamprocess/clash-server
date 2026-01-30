package com.process.clash.application.realtime.service;

import com.process.clash.application.realtime.event.RefetchEvent;
import com.process.clash.application.realtime.port.out.BroadcastRefetchPort;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RefetchEventListener {

    private final BroadcastRefetchPort broadcastRefetchPort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRefetchEvent(RefetchEvent event) {
        if (event == null || event.notice() == null || event.userIds() == null) {
            return;
        }
        Collection<Long> userIds = event.userIds();
        Set<Long> deduped = new LinkedHashSet<>(userIds);
        deduped.removeIf(id -> id == null);
        if (deduped.isEmpty()) {
            return;
        }
        broadcastRefetchPort.broadcastRefetchToUsers(event.notice(), deduped);
    }
}
