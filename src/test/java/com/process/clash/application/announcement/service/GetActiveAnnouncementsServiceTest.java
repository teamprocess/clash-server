package com.process.clash.application.announcement.service;

import com.process.clash.application.announcement.data.GetActiveAnnouncementsData;
import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import com.process.clash.domain.announcement.entity.Announcement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetActiveAnnouncementsServiceTest {

    @Mock
    private AnnouncementRepositoryPort announcementRepositoryPort;

    private GetActiveAnnouncementsService getActiveAnnouncementsService;

    @BeforeEach
    void setUp() {
        getActiveAnnouncementsService = new GetActiveAnnouncementsService(announcementRepositoryPort);
    }

    @Test
    @DisplayName("공지가 있을 때 목록을 반환한다")
    void execute_returnsAnnouncementList() {
        Announcement announcement1 = new Announcement(
                1L, Instant.now(), Instant.now(),
                "서버 점검 안내", "관리자", 1L,
                "3월 30일 오전 2시~4시 서버 점검이 예정되어 있습니다.",
                Instant.now().minusSeconds(3600), Instant.now().plusSeconds(3600)
        );
        Announcement announcement2 = new Announcement(
                2L, Instant.now(), Instant.now(),
                "신규 기능 출시", "운영팀", 2L,
                "새로운 배틀 기능이 출시되었습니다.",
                Instant.now().minusSeconds(7200), Instant.now().plusSeconds(7200)
        );

        when(announcementRepositoryPort.findAllActive(any(Instant.class)))
                .thenReturn(List.of(announcement1, announcement2));

        GetActiveAnnouncementsData.Result result = getActiveAnnouncementsService.execute();

        assertThat(result.announcements()).hasSize(2);
        assertThat(result.announcements().get(0).id()).isEqualTo(1L);
        assertThat(result.announcements().get(0).title()).isEqualTo("서버 점검 안내");
        assertThat(result.announcements().get(0).author()).isEqualTo("관리자");
        assertThat(result.announcements().get(1).id()).isEqualTo(2L);
        assertThat(result.announcements().get(1).title()).isEqualTo("신규 기능 출시");
    }

    @Test
    @DisplayName("공지가 없을 때 빈 목록을 반환한다")
    void execute_returnsEmptyListWhenNoAnnouncements() {
        when(announcementRepositoryPort.findAllActive(any(Instant.class)))
                .thenReturn(List.of());

        GetActiveAnnouncementsData.Result result = getActiveAnnouncementsService.execute();

        assertThat(result.announcements()).isEmpty();
    }
}
