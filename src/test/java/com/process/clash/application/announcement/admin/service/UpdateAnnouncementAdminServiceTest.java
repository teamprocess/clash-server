package com.process.clash.application.announcement.admin.service;

import com.process.clash.application.announcement.admin.data.UpdateAnnouncementAdminData;
import com.process.clash.application.announcement.exception.exception.notfound.AnnouncementNotFoundException;
import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import com.process.clash.application.announcement.realtime.AnnouncementRefetchNotifier;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.user.user.exception.exception.forbidden.RequiredAdminRoleException;
import com.process.clash.domain.announcement.entity.Announcement;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.application.common.actor.Actor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAnnouncementAdminServiceTest {

    @Mock
    private AnnouncementRepositoryPort announcementRepositoryPort;

    @Mock
    private CheckAdminPolicy checkAdminPolicy;

    @Mock
    private AnnouncementRefetchNotifier announcementRefetchNotifier;

    private UpdateAnnouncementAdminService service;

    @BeforeEach
    void setUp() {
        service = new UpdateAnnouncementAdminService(announcementRepositoryPort, checkAdminPolicy, announcementRefetchNotifier);
    }

    @Test
    @DisplayName("존재하는 공지를 수정하면 수정된 결과를 반환한다")
    void execute_updateAnnouncement_success() {
        Actor admin = new Actor(1L, Role.ADMIN);
        Instant startAt = Instant.parse("2026-03-29T00:00:00Z");
        Instant endAt = Instant.parse("2026-04-02T00:00:00Z");
        UpdateAnnouncementAdminData.Command command = new UpdateAnnouncementAdminData.Command(
                admin, 1L, "수정된 제목", "관리자", "수정된 내용", startAt, endAt
        );

        Announcement existing = new Announcement(1L, Instant.now(), Instant.now(),
                "원래 제목", "관리자", 1L, "원래 내용",
                Instant.parse("2026-03-28T00:00:00Z"), Instant.parse("2026-04-01T00:00:00Z"));
        Announcement updated = new Announcement(1L, existing.createdAt(), Instant.now(),
                "수정된 제목", "관리자", 1L, "수정된 내용", startAt, endAt);

        when(announcementRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(announcementRepositoryPort.save(any())).thenReturn(updated);

        UpdateAnnouncementAdminData.Result result = service.execute(command);

        assertThat(result.title()).isEqualTo("수정된 제목");
        assertThat(result.content()).isEqualTo("수정된 내용");
        assertThat(result.endedAt()).isEqualTo(endAt);
        verify(announcementRepositoryPort).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 공지를 수정하면 예외가 발생한다")
    void execute_announcementNotFound_throwsException() {
        Actor admin = new Actor(1L, Role.ADMIN);
        UpdateAnnouncementAdminData.Command command = new UpdateAnnouncementAdminData.Command(
                admin, 999L, "제목", "관리자", "내용",
                Instant.now(), Instant.now().plusSeconds(3600)
        );

        when(announcementRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(AnnouncementNotFoundException.class);

        verify(announcementRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("어드민 권한이 없으면 예외가 발생한다")
    void execute_notAdmin_throwsException() {
        Actor user = new Actor(1L, Role.USER);
        UpdateAnnouncementAdminData.Command command = new UpdateAnnouncementAdminData.Command(
                user, 1L, "제목", "관리자", "내용",
                Instant.now(), Instant.now().plusSeconds(3600)
        );

        doThrow(new RequiredAdminRoleException()).when(checkAdminPolicy).check(user);

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(RequiredAdminRoleException.class);

        verify(announcementRepositoryPort, never()).findById(any());
    }
}
