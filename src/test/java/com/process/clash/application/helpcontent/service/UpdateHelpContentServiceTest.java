package com.process.clash.application.helpcontent.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.helpcontent.data.UpdateHelpContentData;
import com.process.clash.application.helpcontent.port.out.HelpContentRepositoryPort;
import com.process.clash.domain.helpcontent.entity.HelpContent;
import com.process.clash.domain.user.user.enums.Role;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateHelpContentServiceTest {

    @Mock
    private HelpContentRepositoryPort helpContentRepositoryPort;

    private UpdateHelpContentService service;

    @BeforeEach
    void setUp() {
        service = new UpdateHelpContentService(helpContentRepositoryPort, new CheckAdminPolicy());
    }

    @Test
    @DisplayName("관리자가 도움말을 수정하면 내용과 버전이 함께 갱신된다")
    void updateHelpContent_updatesContentAndIncrementsVersion() {
        HelpContent existing = new HelpContent(
                "cookie-tooltip",
                "기존 문구",
                3,
                Instant.parse("2026-07-14T00:00:00Z"),
                Instant.parse("2026-07-14T00:00:00Z")
        );
        HelpContent saved = new HelpContent(
                "cookie-tooltip",
                "변경 문구",
                4,
                existing.createdAt(),
                Instant.parse("2026-07-14T01:00:00Z")
        );
        when(helpContentRepositoryPort.findByKey("cookie-tooltip")).thenReturn(Optional.of(existing));
        when(helpContentRepositoryPort.save(org.mockito.ArgumentMatchers.any())).thenReturn(saved);

        UpdateHelpContentData.Result result = service.execute(new UpdateHelpContentData.Command(
                new Actor(1L, Role.ADMIN),
                "cookie-tooltip",
                "변경 문구"
        ));

        ArgumentCaptor<HelpContent> captor = ArgumentCaptor.forClass(HelpContent.class);
        verify(helpContentRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().content()).isEqualTo("변경 문구");
        assertThat(captor.getValue().version()).isEqualTo(4);
        assertThat(result.version()).isEqualTo(4);
        assertThat(result.content()).isEqualTo("변경 문구");
    }
}
