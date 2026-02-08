package com.process.clash.application.record.realtime;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.realtime.data.ChangeDomain;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.service.RefetchEventPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublishToIncludedGroups {

    private final GroupRepositoryPort groupRepositoryPort;
    private final RefetchEventPublisher refetchEventPublisher;

    public void publish(Actor actor, ChangeType type) {
        if (actor == null || actor.id() == null || type == null) {
            return;
        }

        List<Long> includedGroupIds = groupRepositoryPort.findGroupIdsByMemberUserId(actor.id());
        if (includedGroupIds == null || includedGroupIds.isEmpty()) {
            return;
        }

        List<Long> memberUserIds = groupRepositoryPort.findMemberUserIdsByGroupIds(includedGroupIds);
        if (memberUserIds == null || memberUserIds.isEmpty()) {
            return;
        }

        refetchEventPublisher.publish(RefetchNotice.of(ChangeDomain.GROUP, type), memberUserIds);
    }
}
