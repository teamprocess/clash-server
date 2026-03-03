package com.process.clash.adapter.persistence.recordsession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.process.clash.adapter.persistence.recordtask.RecordTaskJpaMapper;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RecordSessionJpaMapperTest {

    private final RecordSessionJpaMapper mapper = new RecordSessionJpaMapper(
        mock(RecordTaskJpaMapper.class),
        mock(UserJpaMapper.class)
    );

    @Test
    @DisplayName("종료 시각이 있는 세션을 매핑할 때 endedAt을 보존한다")
    void toJpaEntity_keepsEndedAtForClosedSession() {
        Instant startedAt = Instant.parse("2026-02-28T21:00:00Z");
        Instant endedAt = Instant.parse("2026-03-01T06:00:00Z");
        RecordSession closedSession = RecordSession.create(
            null,
            createUser(1L),
            null,
            RecordType.ACTIVITY,
            MonitoredApp.WEBSTORM,
            startedAt,
            endedAt
        );

        RecordSessionJpaEntity entity = mapper.toJpaEntity(
            closedSession,
            mock(UserJpaEntity.class),
            null
        );

        assertThat(entity.getStartedAt()).isEqualTo(startedAt);
        assertThat(entity.getEndedAt()).isEqualTo(endedAt);
    }

    @Test
    @DisplayName("종료되지 않은 세션을 매핑할 때 endedAt은 null이다")
    void toJpaEntity_keepsEndedAtNullForOpenSession() {
        Instant startedAt = Instant.parse("2026-02-28T21:00:00Z");
        RecordSession openSession = RecordSession.create(
            null,
            createUser(1L),
            null,
            RecordType.ACTIVITY,
            MonitoredApp.WEBSTORM,
            startedAt,
            null
        );

        RecordSessionJpaEntity entity = mapper.toJpaEntity(
            openSession,
            mock(UserJpaEntity.class),
            null
        );

        assertThat(entity.getStartedAt()).isEqualTo(startedAt);
        assertThat(entity.getEndedAt()).isNull();
    }

    private User createUser(Long id) {
        Instant now = Instant.parse("2026-02-28T00:00:00Z");
        return new User(
            id,
            now,
            now,
            "username",
            "user@example.com",
            "name",
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE,
            null
        );
    }
}
