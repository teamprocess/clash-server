package com.process.clash.application.record.service;

import com.process.clash.application.record.data.GetRecordSettingData;
import com.process.clash.application.record.port.in.GetRecordSettingUseCase;
import com.process.clash.application.user.userpomodorosetting.exception.exception.notfound.UserPomodoroSettingNotFoundException;
import com.process.clash.application.user.userpomodorosetting.port.out.UserPomodoroSettingRepositoryPort;
import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetRecordSettingService implements GetRecordSettingUseCase {

    private final UserPomodoroSettingRepositoryPort userPomodoroSettingRepositoryPort;

    @Override
    public GetRecordSettingData.Result execute(GetRecordSettingData.Command command) {
        UserPomodoroSetting userPomodoroSetting = userPomodoroSettingRepositoryPort.findByUserId(command.actor().id())
            .orElseThrow(UserPomodoroSettingNotFoundException::new);

        return GetRecordSettingData.Result.create(
                userPomodoroSetting.pomodoroEnabled(),
                userPomodoroSetting.pomodoroStudyMinute(),
                userPomodoroSetting.pomodoroBreakMinute()
        );
    }
}
