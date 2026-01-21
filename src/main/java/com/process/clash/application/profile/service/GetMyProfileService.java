package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.GetMyProfileData;
import com.process.clash.application.profile.port.in.GetMyProfileUsecase;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyProfileService implements GetMyProfileUsecase {

    private final UserRepositoryPort userRepositoryPort;

    public GetMyProfileData.Result execute(GetMyProfileData.Command command) {
        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        return GetMyProfileData.Result.from(user);
    }
}
