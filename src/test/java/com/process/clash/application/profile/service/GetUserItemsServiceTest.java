package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.GetMyItemsData;
import com.process.clash.application.profile.data.GetUserItemsData;
import com.process.clash.application.profile.exception.exception.forbidden.ProfilePrivatedException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.common.enums.UserItemCategory;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserItemsServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserItemRepositoryPort userItemRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private GetUserItemsService getUserItemsService;

    @BeforeEach
    void setUp() {
        getUserItemsService = new GetUserItemsService(userRepositoryPort, userItemRepositoryPort, productRepositoryPort);
    }

    @Test
    @DisplayName("비공개 유저 아이템 조회 시 ProfilePrivatedException을 던진다")
    void execute_throwsProfilePrivatedException_whenUserIsPrivate() {
        Long targetUserId = 2L;
        User privateUser = createUser(targetUserId, true);
        GetUserItemsData.Command command = new GetUserItemsData.Command(targetUserId, UserItemCategory.ALL);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(privateUser));

        assertThatThrownBy(() -> getUserItemsService.execute(command))
            .isInstanceOf(ProfilePrivatedException.class);
    }

    @Test
    @DisplayName("공개 유저 아이템 조회 시 빈 목록을 반환한다")
    void execute_returnsEmptyList_whenUserHasNoItems() {
        Long targetUserId = 2L;
        User publicUser = createUser(targetUserId, false);
        GetUserItemsData.Command command = new GetUserItemsData.Command(targetUserId, UserItemCategory.ALL);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(publicUser));
        when(userItemRepositoryPort.findProductIdsByUserId(targetUserId)).thenReturn(List.of());

        GetMyItemsData.Result result = getUserItemsService.execute(command);

        assertThat(result.items()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회 시 UserNotFoundException을 던진다")
    void execute_throwsUserNotFoundException_whenUserNotFound() {
        Long targetUserId = 999L;
        GetUserItemsData.Command command = new GetUserItemsData.Command(targetUserId, UserItemCategory.ALL);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserItemsService.execute(command))
            .isInstanceOf(UserNotFoundException.class);
    }

    private User createUser(Long id, boolean isPrivate) {
        return new User(
            id, Instant.now(), Instant.now(),
            "username", "user@example.com", "name", "encoded-password",
            Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE,
            null, isPrivate, RankTier.NONE, ExpTier.UNRANKED
        );
    }
}
