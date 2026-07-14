package com.process.clash.application.helpcontent.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.helpcontent.data.CreateHelpContentData;
import com.process.clash.application.helpcontent.exception.exception.conflict.HelpContentAlreadyExistsException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateHelpContentServiceTest {

    @Mock
    private HelpContentRepositoryPort helpContentRepositoryPort;

    private CreateHelpContentService service;

    @BeforeEach
    void setUp() {
        service = new CreateHelpContentService(helpContentRepositoryPort, new CheckAdminPolicy());
    }

    @Test
    @DisplayName("관리자가 새 도움말을 생성하면 버전 1로 저장된다")
    void createHelpContent_savesInitialVersion() {
        HelpContent saved = new HelpContent(
                "new-tooltip",
                "새 안내 문구",
                1,
                Instant.parse("2026-07-14T00:00:00Z"),
                Instant.parse("2026-07-14T00:00:00Z")
        );
        when(helpContentRepositoryPort.existsByKey("new-tooltip")).thenReturn(false);
        when(helpContentRepositoryPort.save(org.mockito.ArgumentMatchers.any())).thenReturn(saved);

        CreateHelpContentData.Result result = service.execute(new CreateHelpContentData.Command(
                new Actor(1L, Role.ADMIN),
                "new-tooltip",
                "새 안내 문구"
        ));

        ArgumentCaptor<HelpContent> captor = ArgumentCaptor.forClass(HelpContent.class);
        verify(helpContentRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().version()).isEqualTo(1);
        assertThat(captor.getValue().key()).isEqualTo("new-tooltip");
        assertThat(result.version()).isEqualTo(1);
    }

    @Test
    @DisplayName("이미 존재하는 도움말 키는 생성할 수 없다")
    void createHelpContent_whenKeyExists_throwsConflict() {
        when(helpContentRepositoryPort.existsByKey("cookie-tooltip")).thenReturn(true);

        assertThatThrownBy(() -> service.execute(new CreateHelpContentData.Command(
                new Actor(1L, Role.ADMIN),
                "cookie-tooltip",
                "새 안내 문구"
        )))
                .isInstanceOf(HelpContentAlreadyExistsException.class);

        verify(helpContentRepositoryPort, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
