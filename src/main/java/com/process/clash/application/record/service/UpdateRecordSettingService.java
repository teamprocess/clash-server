package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.UpdateRecordSettingData;
import com.process.clash.application.record.port.in.UpdateRecordSettingUseCase;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRecordSettingService implements UpdateRecordSettingUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UpdateRecordSettingData.Result execute(UpdateRecordSettingData.Command command) {
        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        User updatedUser = new User(
            user.id(),
            user.createdAt(),
            LocalDateTime.now(),
            user.username(),
            user.name(),
            user.password(),
            user.role(),
            user.ableToAddRival(),
            user.profileImage(),
            command.pomodoroEnabled(),
            command.studyMinute(),
            command.breakMinute(),
            user.major()
        );

        User savedUser = userRepositoryPort.save(updatedUser);

        return UpdateRecordSettingData.Result.create(
            savedUser.pomodoroEnabled(),
            savedUser.pomodoroStudyMinute(),
            savedUser.pomodoroBreakMinute()
        );
    }
}
