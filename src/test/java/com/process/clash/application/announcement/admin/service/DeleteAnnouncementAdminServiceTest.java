package com.process.clash.application.announcement.admin.service;

import com.process.clash.application.announcement.admin.data.DeleteAnnouncementAdminData;
import com.process.clash.application.announcement.exception.exception.notfound.AnnouncementNotFoundException;
import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import com.process.clash.application.announcement.realtime.AnnouncementRefetchNotifier;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.user.user.exception.exception.forbidden.RequiredAdminRoleException;
import com.process.clash.domain.announcement.entity.Announcement;
import com.process.clash.domain.user.user.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteAnnouncementAdminServiceTest {

    @Mock
    private AnnouncementRepositoryPort announcementRepositoryPort;

    @Mock
    private CheckAdminPolicy checkAdminPolicy;

    @Mock
    private AnnouncementRefetchNotifier announcementRefetchNotifier;

    private DeleteAnnouncementAdminService service;

    @BeforeEach
    void setUp() {
        service = new DeleteAnnouncementAdminService(announcementRepositoryPort, checkAdminPolicy, announcementRefetchNotifier);
    }

    @Test
    @DisplayName("존재하는 공지를 삭제하면 성공한다")
    void execute_deleteAnnouncement_success() {
        Actor admin = new Actor(1L, Role.ADMIN);
        DeleteAnnouncementAdminData.Command command = new DeleteAnnouncementAdminData.Command(admin, 1L);

        Announcement existing = new Announcement(1L, Instant.now(), Instant.now(),
                "서버 점검 안내", "관리자", 1L, "내용",
                Instant.now().minusSeconds(3600), Instant.now().plusSeconds(3600));
        when(announcementRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));

        service.execute(command);

        verify(announcementRepositoryPort).deleteById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 공지를 삭제하면 예외가 발생한다")
    void execute_announcementNotFound_throwsException() {
        Actor admin = new Actor(1L, Role.ADMIN);
        DeleteAnnouncementAdminData.Command command = new DeleteAnnouncementAdminData.Command(admin, 999L);

        when(announcementRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(AnnouncementNotFoundException.class);

        verify(announcementRepositoryPort, never()).deleteById(any());
    }

    @Test
    @DisplayName("어드민 권한이 없으면 예외가 발생한다")
    void execute_notAdmin_throwsException() {
        Actor user = new Actor(1L, Role.USER);
        DeleteAnnouncementAdminData.Command command = new DeleteAnnouncementAdminData.Command(user, 1L);

        doThrow(new RequiredAdminRoleException()).when(checkAdminPolicy).check(user);

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(RequiredAdminRoleException.class);

        verify(announcementRepositoryPort, never()).findById(any());
    }
}
