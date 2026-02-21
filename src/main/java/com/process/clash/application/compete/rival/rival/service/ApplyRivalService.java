package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.rival.policy.ApplyRivalPolicy;
import com.process.clash.application.compete.rival.rival.port.in.ApplyRivalUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyRivalService implements ApplyRivalUseCase {

    private final ApplyRivalPolicy applyRivalPolicy;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserNoticeRepositoryPort userNoticeRepositoryPort;
    private final CompeteRefetchNotifier competeRefetchNotifier;

    @Override
    public void execute(ApplyRivalData.Command command) {

        applyRivalPolicy.check(command);

        List<Rival> rivals = command.ids().stream()
                .map(opponentId -> Rival.createDefault(command.actor().id(), opponentId.id()))
                .toList();

        rivalRepositoryPort.saveAll(rivals);

        List<UserNotice> userNotices = rivals.stream()
                .map(rival -> UserNotice.createDefault(
                        NoticeCategory.APPLY_RIVAL,
                        rival.firstUserId(),
                        rival.secondUserId()
                ))
                .toList();

        userNoticeRepositoryPort.saveAll(userNotices);

        List<Long> opponentIds = rivals.stream()
                .map(Rival::secondUserId)
                .toList();
        competeRefetchNotifier.notifyUserNoticeChanged(opponentIds);
    }
}