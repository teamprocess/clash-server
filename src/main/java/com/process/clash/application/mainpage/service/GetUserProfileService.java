package com.process.clash.application.mainpage.service;

import com.process.clash.application.mainpage.data.GetUserProfileData;
import com.process.clash.application.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.mainpage.port.in.GetUserProfileUseCase;
import com.process.clash.application.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileService implements GetUserProfileUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public GetUserProfileData.Result execute(GetUserProfileData.Command command) {
        User user = userRepositoryPort.findById(command.actor().userId())
                .orElseThrow(UserNotFoundException::new);

        return GetUserProfileData.Result.from(user);
    }
}
