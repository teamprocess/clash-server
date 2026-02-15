package com.process.clash.application.user.user.service;

import com.process.clash.application.user.user.data.UpdateMyProfileImageData;
import com.process.clash.application.user.user.exception.exception.badrequest.InvalidProfileImageUploadRequestException;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.in.UpdateMyProfileImageUseCase;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateMyProfileImageService implements UpdateMyProfileImageUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public UpdateMyProfileImageData.Result execute(UpdateMyProfileImageData.Command command) {
        if (command == null || command.actor() == null || command.actor().id() == null) {
            throw new InvalidProfileImageUploadRequestException();
        }
        if (command.profileImageUrl() == null || command.profileImageUrl().isBlank()) {
            throw new InvalidProfileImageUploadRequestException();
        }

        User user = userRepositoryPort.findById(command.actor().id())
                .orElseThrow(UserNotFoundException::new);

        User updatedUser = user.updateProfileImage(command.profileImageUrl().trim());
        User savedUser = userRepositoryPort.save(updatedUser);

        return new UpdateMyProfileImageData.Result(savedUser.profileImage());
    }
}
