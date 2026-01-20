package com.process.clash.application.record.service;

import com.process.clash.application.record.data.UpdateRecordSettingData;
import com.process.clash.application.record.port.in.UpdateRecordSettingUseCase;
import com.process.clash.application.user.userpomodorosetting.exception.exception.notfound.UserPomodoroSettingNotFoundException;
import com.process.clash.application.user.userpomodorosetting.port.out.UserPomodoroSettingRepositoryPort;
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
            .orElseThrow(UserPomodoroSettingNotFoundException::new);

        UserPomodoroSetting updatedUserPomodoroSetting = userPomodoroSetting.updatePomodoroSetting(
                command.pomodoroEnabled(),
                command.studyMinute(),
                command.breakMinute()
        );

        UserPomodoroSetting savedUserPomodoroSetting = userPomodoroSettingRepositoryPort.save(updatedUserPomodoroSetting);

        return UpdateRecordSettingData.Result.create(
                savedUserPomodoroSetting.pomodoroEnabled(),
                savedUserPomodoroSetting.pomodoroStudyMinute(),
                savedUserPomodoroSetting.pomodoroBreakMinute()
        );
    }
}
