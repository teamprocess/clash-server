package com.process.clash.application.user.user.service;

import com.process.clash.application.user.user.data.UpdateMyPrivacyData;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.in.UpdateMyPrivacyUseCase;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateMyPrivacyService implements UpdateMyPrivacyUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public UpdateMyPrivacyData.Result execute(UpdateMyPrivacyData.Command command) {
        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        User updatedUser = user.updatePrivacy(command.isPrivate());
        User savedUser = userRepositoryPort.save(updatedUser);

        return new UpdateMyPrivacyData.Result(savedUser.isPrivate());
    }
}
