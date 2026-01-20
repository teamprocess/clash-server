package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.UpdateRecordSettingData;
import com.process.clash.application.record.port.in.UpdateRecordSettingUseCase;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.userpomodorosetting.port.out.UserPomodoroSettingRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRecordSettingService implements UpdateRecordSettingUseCase {

    private final UserPomodoroSettingRepositoryPort userPomodoroSettingRepositoryPort;

    @Override
    public UpdateRecordSettingData.Result execute(UpdateRecordSettingData.Command command) {
        UserPomodoroSetting userPomodoroSetting = userPomodoroSettingRepositoryPort.findByUserId(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        User updatedUser = user.withPomodoroSettings(
                command.pomodoroEnabled(),
                command.studyMinute(),
                command.breakMinute()
        );

        User savedUser = userRepositoryPort.save(updatedUser);

        return UpdateRecordSettingData.Result.create(
            savedUser.pomodoroEnabled(),
            savedUser.pomodoroStudyMinute(),
            savedUser.pomodoroBreakMinute()
        );
    }
}
