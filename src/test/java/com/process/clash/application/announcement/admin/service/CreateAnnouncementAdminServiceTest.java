package com.process.clash.application.announcement.admin.service;

import com.process.clash.application.announcement.admin.data.CreateAnnouncementAdminData;
import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAnnouncementAdminServiceTest {

    @Mock
    private AnnouncementRepositoryPort announcementRepositoryPort;

    @Mock
    private CheckAdminPolicy checkAdminPolicy;

    private CreateAnnouncementAdminService service;

    @BeforeEach
    void setUp() {
        service = new CreateAnnouncementAdminService(announcementRepositoryPort, checkAdminPolicy);
    }

    @Test
    @DisplayName("어드민이 공지를 생성하면 저장된 공지를 반환한다")
    void execute_createAnnouncement_success() {
        Actor admin = new Actor(1L, Role.ADMIN);
        Instant startAt = Instant.parse("2026-03-29T00:00:00Z");
        Instant endAt = Instant.parse("2026-04-01T00:00:00Z");
        CreateAnnouncementAdminData.Command command = new CreateAnnouncementAdminData.Command(
                admin, "서버 점검 안내", "관리자", "점검 예정입니다.", startAt, endAt
        );

        Announcement saved = new Announcement(1L, Instant.now(), Instant.now(),
                "서버 점검 안내", "관리자", 1L, "점검 예정입니다.", startAt, endAt);
        when(announcementRepositoryPort.save(any())).thenReturn(saved);

        CreateAnnouncementAdminData.Result result = service.execute(command);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("서버 점검 안내");
        assertThat(result.author()).isEqualTo("관리자");
        verify(announcementRepositoryPort).save(any());
    }

    @Test
    @DisplayName("어드민 권한이 없으면 예외가 발생한다")
    void execute_notAdmin_throwsException() {
        Actor user = new Actor(1L, Role.USER);
        CreateAnnouncementAdminData.Command command = new CreateAnnouncementAdminData.Command(
                user, "제목", "작성자", "내용",
                Instant.now(), Instant.now().plusSeconds(3600)
        );

        doThrow(new RequiredAdminRoleException()).when(checkAdminPolicy).check(user);

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(RequiredAdminRoleException.class);

        verify(announcementRepositoryPort, never()).save(any());
    }
}
