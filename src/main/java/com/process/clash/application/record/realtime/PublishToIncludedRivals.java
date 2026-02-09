package com.process.clash.application.record.realtime;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.realtime.data.ChangeDomain;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.service.RefetchEventPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublishToIncludedRivals {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final RefetchEventPublisher refetchEventPublisher;

    public void publish(Actor actor, ChangeType type) {
        if (actor == null || actor.id() == null || type == null) {
            return;
        }

        List<Long> rivalUserIds = rivalRepositoryPort.findOpponentIdByUserId(actor.id());
        if (rivalUserIds == null || rivalUserIds.isEmpty()) {
            return;
        }

        refetchEventPublisher.publish(RefetchNotice.of(ChangeDomain.COMPETE, type), rivalUserIds);
    }
}
