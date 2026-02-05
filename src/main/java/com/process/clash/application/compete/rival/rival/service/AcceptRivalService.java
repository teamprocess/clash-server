package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
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

@Service
@RequiredArgsConstructor
@Transactional
public class AcceptRivalService implements AcceptRivalUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final AcceptRivalPolicy acceptRivalPolicy;
    private final UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Override
    public void execute(ModifyRivalData.Command command) {

        Rival rival = acceptRivalPolicy.check(command.actor(), command.id());

        Rival updatedRival = rival.accept();

        rivalRepositoryPort.save(updatedRival);

        UserNotice userNoticeForReceiver = UserNotice
                .createDefault(
                        NoticeCategory.ACCEPT_RIVAL,
                        command.actor().id(),
                        rivalRepositoryPort.findOpponentIdByIdAndUserId(rival.id(), command.actor().id())
                );

        userNoticeRepositoryPort.save(userNoticeForReceiver);

        UserNotice userNoticeForSender = UserNotice
                .createDefault(
                        NoticeCategory.SHOW_ACCEPT_RIVAL,
                        command.actor().id(),
                        command.actor().id()
                );

        userNoticeRepositoryPort.save(userNoticeForSender);
    }
}
