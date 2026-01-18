package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;
import com.process.clash.application.compete.rival.port.in.GetAllAbleRivalsUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllAbleRivalsService implements GetAllAbleRivalsUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command) {

        List<Rival> rivals = rivalRepositoryPort.findAllByMy_Id(command.actor().id());

        List<Long> opponentIds = rivals.stream()
                .map(Rival::opponentId)
                .toList();

        List<User> users = userRepositoryPort.findByIdNotIn(opponentIds);

        return ;
    }
}
