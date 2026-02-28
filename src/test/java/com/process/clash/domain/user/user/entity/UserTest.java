package com.process.clash.domain.user.user.entity;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("deletedAt이 null이면 탈퇴하지 않은 유저다")
    void isDeleted_returnsFalse_whenDeletedAtIsNull() {
        User user = activeUser();

        assertThat(user.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("deletedAt이 설정되어 있으면 탈퇴한 유저다")
    void isDeleted_returnsTrue_whenDeletedAtIsSet() {
        User user = deletedUser();

        assertThat(user.isDeleted()).isTrue();
    }

    private User activeUser() {
        return new User(
                1L,
                Instant.now(),
                Instant.now(),
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

    private User deletedUser() {
        return new User(
                1L,
                Instant.now(),
                Instant.now(),
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
                Instant.now()
        );
    }
}
