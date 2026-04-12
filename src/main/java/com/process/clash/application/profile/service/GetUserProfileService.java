package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.GetUserProfileData;
import com.process.clash.application.profile.port.in.GetUserProfileUsecase;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.UserPresencePort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetUserProfileService implements GetUserProfileUsecase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserPresencePort userPresencePort;
    private final EquippedItemsAssembler equippedItemsAssembler;

    @Override
    public GetUserProfileData.Result execute(GetUserProfileData.Command command) {
        User user = userRepositoryPort.findById(command.targetUserId())
            .orElseThrow(UserNotFoundException::new);

        UserActivityStatus activityStatus = userPresencePort.getStatus(user.id());
        return GetUserProfileData.Result.from(
            user,
            activityStatus,
            equippedItemsAssembler.loadByUserId(user.id())
        );
    }
}
