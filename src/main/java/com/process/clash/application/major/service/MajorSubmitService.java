package com.process.clash.application.major.service;

import com.process.clash.application.major.data.MajorSubmitData;
import com.process.clash.application.major.port.in.MajorTestSubmitUseCase;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MajorSubmitService implements MajorTestSubmitUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public void execute(MajorSubmitData.Command command) {

        User user = userRepositoryPort.findById(command.actor().id())
                .orElseThrow(UserNotFoundException::new);

        user.submitMajor(command.major());

        userRepositoryPort.save(user);

    }
}
