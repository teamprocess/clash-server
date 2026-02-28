package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.in.CancelRivalUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CancelRivalService implements CancelRivalUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserNoticeRepositoryPort userNoticeRepositoryPort;
    private final CompeteRefetchNotifier competeRefetchNotifier;

    @Override
    public void execute(ModifyRivalData.Command command) {

        Rival rival = rivalRepositoryPort.findById(command.id())
                .orElseThrow(RivalNotFoundException::new);

        Rival updatedRival = rival.cancel();

        Rival savedRival = rivalRepositoryPort.save(updatedRival);

        Long opponentId = rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(savedRival.id(), command.actor().id());

        UserNotice userNoticeForReceiver = UserNotice
                .createDefault(
                        NoticeCategory.CANCEL_RIVAL,
                        command.actor().id(),
                        opponentId
                );

        userNoticeRepositoryPort.save(userNoticeForReceiver);

        List<Long> userIdsToNotify = List.of(opponentId, command.actor().id());
        competeRefetchNotifier.notifyUserNoticeChanged(userIdsToNotify);
        competeRefetchNotifier.notifyCompeteChanged(userIdsToNotify);
    }
}
