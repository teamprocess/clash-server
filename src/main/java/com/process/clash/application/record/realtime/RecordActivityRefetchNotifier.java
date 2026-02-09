package com.process.clash.application.record.realtime;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.realtime.GroupRefetchNotifier;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordActivityRefetchNotifier implements RecordActivityNotifierPort {

    private final GroupRepositoryPort groupRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final GroupRefetchNotifier groupRefetchNotifier;
    private final CompeteRefetchNotifier competeRefetchNotifier;

    @Override
    public void notifyActivityStarted(Actor actor) {
        notify(
            actor,
            groupRefetchNotifier::notifyGroupActivityStarted,
            competeRefetchNotifier::notifyRivalActivityStarted
        );
    }

    @Override
    public void notifyActivityStopped(Actor actor) {
        notify(
            actor,
            groupRefetchNotifier::notifyGroupActivityStopped,
            competeRefetchNotifier::notifyRivalActivityStopped
        );
    }

    private void notify(
        Actor actor,
        Consumer<List<Long>> groupNotifier,
        Consumer<List<Long>> rivalNotifier
    ) {
        if (actor == null || actor.id() == null) {
            return;
        }

        List<Long> groupMemberUserIds = findGroupMemberUserIds(actor.id());
        if (!groupMemberUserIds.isEmpty()) {
            groupNotifier.accept(groupMemberUserIds);
        }

        List<Long> rivalUserIds = rivalRepositoryPort.findOpponentIdByUserId(actor.id());
        if (rivalUserIds != null && !rivalUserIds.isEmpty()) {
            rivalNotifier.accept(rivalUserIds);
        }
    }

    private List<Long> findGroupMemberUserIds(Long userId) {
        List<Long> includedGroupIds = groupRepositoryPort.findGroupIdsByMemberUserId(userId);
        if (includedGroupIds == null || includedGroupIds.isEmpty()) {
            return List.of();
        }
        List<Long> memberUserIds = groupRepositoryPort.findMemberUserIdsByGroupIds(includedGroupIds);
        return memberUserIds != null ? memberUserIds : List.of();
    }
}
