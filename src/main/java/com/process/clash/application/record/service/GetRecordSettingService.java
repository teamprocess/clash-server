package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.GetRecordSettingData;
import com.process.clash.application.record.port.in.GetRecordSettingUseCase;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetRecordSettingService implements GetRecordSettingUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public GetRecordSettingData.Result execute(GetRecordSettingData.Command command) {
        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        return GetRecordSettingData.Result.create(
            user.pomodoroEnabled(),
            user.pomodoroStudyMinute(),
            user.pomodoroBreakMinute()
        );
    }
}
