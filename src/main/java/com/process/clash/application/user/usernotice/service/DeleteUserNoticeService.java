package com.process.clash.application.user.usernotice.service;

import com.process.clash.application.user.usernotice.data.DeleteUserNoticeData;
import com.process.clash.application.user.usernotice.exception.exception.notfound.UserNoticeNotFoundException;
import com.process.clash.application.user.usernotice.port.in.DeleteUserNoticeUseCase;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteUserNoticeService implements DeleteUserNoticeUseCase {

    private final UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Override
    public void execute(DeleteUserNoticeData.Command command) {
        int updatedRows = userNoticeRepositoryPort.deleteByIdAndReceiverId(command.noticeId(), command.actor().id());
        if (updatedRows == 0) {
            throw new UserNoticeNotFoundException();
        }
    }
}