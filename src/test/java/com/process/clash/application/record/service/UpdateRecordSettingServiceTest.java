package com.process.clash.application.record.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.UpdateRecordSettingData;
import com.process.clash.application.user.userpomodorosetting.port.out.UserPomodoroSettingRepositoryPort;
import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRecordSettingServiceTest {

    @Mock
    private UserPomodoroSettingRepositoryPort userPomodoroSettingRepositoryPort;

    private UpdateRecordSettingService updateRecordSettingService;

    @BeforeEach
    void setUp() {
        updateRecordSettingService = new UpdateRecordSettingService(userPomodoroSettingRepositoryPort);
    }

    @Test
    @DisplayName("기존 Pomodoro Setting이 있으면 값만 업데이트한다")
    void execute_updatesExistingSetting() {
        Actor actor = new Actor(1L);
        UpdateRecordSettingData.Command command = new UpdateRecordSettingData.Command(actor, true, 50, 10);
        UserPomodoroSetting existing = new UserPomodoroSetting(
            100L,
            Instant.now().minusSeconds(86_400),
            Instant.now().minusSeconds(3_600),
            false,
            25,
            5,
            actor.id()
        );

        when(userPomodoroSettingRepositoryPort.findByUserId(actor.id())).thenReturn(Optional.of(existing));
        when(userPomodoroSettingRepositoryPort.save(any(UserPomodoroSetting.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateRecordSettingData.Result result = updateRecordSettingService.execute(command);

        ArgumentCaptor<UserPomodoroSetting> captor = ArgumentCaptor.forClass(UserPomodoroSetting.class);
        verify(userPomodoroSettingRepositoryPort).save(captor.capture());
        UserPomodoroSetting saved = captor.getValue();

        assertThat(saved.id()).isEqualTo(existing.id());
        assertThat(saved.userId()).isEqualTo(actor.id());
        assertThat(saved.pomodoroEnabled()).isTrue();
        assertThat(saved.pomodoroStudyMinute()).isEqualTo(50);
        assertThat(saved.pomodoroBreakMinute()).isEqualTo(10);
        assertThat(result.pomodoroEnabled()).isTrue();
        assertThat(result.studyMinute()).isEqualTo(50);
        assertThat(result.breakMinute()).isEqualTo(10);
    }

    @Test
    @DisplayName("Pomodoro Setting이 없으면 새로 생성해서 업데이트한다")
    void execute_createsSettingWhenMissing() {
        Actor actor = new Actor(2L);
        UpdateRecordSettingData.Command command = new UpdateRecordSettingData.Command(actor, true, 40, 8);

        when(userPomodoroSettingRepositoryPort.findByUserId(actor.id())).thenReturn(Optional.empty());
        when(userPomodoroSettingRepositoryPort.save(any(UserPomodoroSetting.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateRecordSettingData.Result result = updateRecordSettingService.execute(command);

        ArgumentCaptor<UserPomodoroSetting> captor = ArgumentCaptor.forClass(UserPomodoroSetting.class);
        verify(userPomodoroSettingRepositoryPort).save(captor.capture());
        UserPomodoroSetting saved = captor.getValue();

        assertThat(saved.id()).isNull();
        assertThat(saved.userId()).isEqualTo(actor.id());
        assertThat(saved.pomodoroEnabled()).isTrue();
        assertThat(saved.pomodoroStudyMinute()).isEqualTo(40);
        assertThat(saved.pomodoroBreakMinute()).isEqualTo(8);
        assertThat(result.pomodoroEnabled()).isTrue();
        assertThat(result.studyMinute()).isEqualTo(40);
        assertThat(result.breakMinute()).isEqualTo(8);
    }
}

