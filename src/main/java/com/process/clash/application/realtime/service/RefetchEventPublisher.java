package com.process.clash.application.realtime.service;

import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.event.RefetchEvent;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefetchEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(RefetchNotice notice, Collection<Long> userIds) {
        if (notice == null || userIds == null || userIds.isEmpty()) {
            return;
        }
        Set<Long> deduped = new LinkedHashSet<>();
        for (Long userId : userIds) {
            if (userId != null) {
                deduped.add(userId);
            }
        }
        if (deduped.isEmpty()) {
            return;
        }
        applicationEventPublisher.publishEvent(new RefetchEvent(notice, deduped));
    }
}
