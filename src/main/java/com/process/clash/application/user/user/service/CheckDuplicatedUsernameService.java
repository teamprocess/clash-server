package com.process.clash.application.user.user.service;

import com.process.clash.application.user.user.data.CheckDuplicateUsernameData;
import com.process.clash.application.user.user.port.in.CheckDuplicatedUsernameUseCase;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckDuplicatedUsernameService implements CheckDuplicatedUsernameUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public boolean execute(CheckDuplicateUsernameData.Command command) {

        return userRepositoryPort.existsByUsername(command.username());
    }
}
