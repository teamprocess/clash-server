package com.process.clash.application.user.user.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.user.data.UpdateMyProfileImageData;
import com.process.clash.application.user.user.exception.exception.badrequest.InvalidProfileImageUploadRequestException;
import com.process.clash.application.user.user.port.out.ProfileImageUploadPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateMyProfileImageServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private ProfileImageUploadPort profileImageUploadPort;

    private UpdateMyProfileImageService updateMyProfileImageService;

    @BeforeEach
    void setUp() {
        updateMyProfileImageService = new UpdateMyProfileImageService(
                userRepositoryPort,
                profileImageUploadPort
        );
    }

    @Test
    @DisplayName("발급된 프로필 이미지 URL이면 저장한다")
    void execute_updatesProfileImageWhenUrlIsValid() {
        User user = createUser(1L);
        Actor actor = new Actor(user.id());
        String profileImageUrl = "https://bucket.s3.ap-northeast-2.amazonaws.com/users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png";
        UpdateMyProfileImageData.Command command = new UpdateMyProfileImageData.Command(actor, profileImageUrl);

        when(profileImageUploadPort.isValidProfileImageUrl(user.id(), profileImageUrl)).thenReturn(true);
        when(userRepositoryPort.findById(user.id())).thenReturn(Optional.of(user));
        when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateMyProfileImageData.Result result = updateMyProfileImageService.execute(command);

        assertThat(result.profileImageUrl()).isEqualTo(profileImageUrl);
        verify(profileImageUploadPort).isValidProfileImageUrl(user.id(), profileImageUrl);
    }

    @Test
    @DisplayName("발급되지 않은 프로필 이미지 URL이면 예외를 던진다")
    void execute_throwsExceptionWhenUrlIsInvalid() {
        Actor actor = new Actor(1L);
        String profileImageUrl = "https://example.com/image.png";
        UpdateMyProfileImageData.Command command = new UpdateMyProfileImageData.Command(actor, profileImageUrl);

        when(profileImageUploadPort.isValidProfileImageUrl(actor.id(), profileImageUrl)).thenReturn(false);

        assertThatThrownBy(() -> updateMyProfileImageService.execute(command))
                .isInstanceOf(InvalidProfileImageUploadRequestException.class);

        verify(userRepositoryPort, never()).findById(anyLong());
    }

    private User createUser(Long id) {
        return new User(
                id,
                Instant.now().minusSeconds(86_400),
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
                UserStatus.ACTIVE
        );
    }
}
