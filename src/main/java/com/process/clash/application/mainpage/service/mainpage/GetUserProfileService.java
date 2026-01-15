package com.process.clash.application.mainpage.service.mainpage;

import com.process.clash.application.mainpage.data.GetUserProfileData;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.mainpage.port.in.mainpage.GetUserProfileUseCase;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileService implements GetUserProfileUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public GetUserProfileData.Result execute(GetUserProfileData.Command command) {

        User user = userRepositoryPort.findById(command.actor().id())
                .orElseThrow(UserNotFoundException::new);

        return GetUserProfileData.Result.from(user);
    }
}
