package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.rival.policy.AcceptRivalPolicy;
import com.process.clash.application.compete.rival.rival.port.in.AcceptRivalUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AcceptRivalService implements AcceptRivalUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final AcceptRivalPolicy acceptRivalPolicy;
    private final UserNoticeRepositoryPort userNoticeRepositoryPort;
    private final CompeteRefetchNotifier competeRefetchNotifier;

    private static final int MAX_RIVAL_COUNT = 4;

    @Override
    public void execute(ModifyRivalData.Command command) {

        Rival rival = acceptRivalPolicy.check(command.actor(), command.id());
        Long opponentId = rival.firstUserId();

        rivalRepositoryPort.save(rival.accept());

        userNoticeRepositoryPort.deleteApplyRivalNoticeByRivalId(rival.id());

        userNoticeRepositoryPort.save(
                UserNotice.createDefault(NoticeCategory.ACCEPT_RIVAL, command.actor().id(), opponentId)
        );

        List<Long> affectedUsers = new ArrayList<>(List.of(opponentId, command.actor().id()));

        cancelAllPendingIfFull(command.actor().id(), affectedUsers);
        cancelAllPendingIfFull(opponentId, affectedUsers);

        competeRefetchNotifier.notifyUserNoticeChanged(affectedUsers);
        competeRefetchNotifier.notifyCompeteChanged(affectedUsers);
    }

    private void cancelAllPendingIfFull(Long userId, List<Long> affectedUsers) {

        if (rivalRepositoryPort.countAcceptedByUserId(userId) < MAX_RIVAL_COUNT) return;

        List<Rival> pendingRivals = rivalRepositoryPort.findAllPendingByUserId(userId);

        if (pendingRivals.isEmpty()) return;

        rivalRepositoryPort.saveAll(pendingRivals.stream().map(Rival::cancel).toList());

        List<Long> pendingRivalIds = new ArrayList<>();
        List<UserNotice> cancelNotices = new ArrayList<>();

        for (Rival pending : pendingRivals) {
            Long pendingOpponentId = pending.firstUserId().equals(userId)
                    ? pending.secondUserId()
                    : pending.firstUserId();

            affectedUsers.add(pendingOpponentId);
            cancelNotices.add(UserNotice.createDefault(NoticeCategory.CANCEL_RIVAL, userId, pendingOpponentId));
            pendingRivalIds.add(pending.id());
        }

        userNoticeRepositoryPort.deleteApplyRivalNoticeByRivalIds(pendingRivalIds);
        userNoticeRepositoryPort.saveAll(cancelNotices);
    }
}