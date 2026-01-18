package com.process.clash.adapter.persistence.rival;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RivalPersistenceAdapter implements RivalRepositoryPort {

    private final RivalJpaMapper rivalJpaMapper;
    private final RivalJpaRepository rivalJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Rival save(Rival rival) {

        UserJpaEntity my = userJpaRepository.getReferenceById(rival.myId());
        UserJpaEntity opponent = userJpaRepository.getReferenceById(rival.opponentId());
        RivalJpaEntity savedEntity = rivalJpaRepository.save(rivalJpaMapper.toJpaEntity(rival, my, opponent));
        return rivalJpaMapper.toDomain(savedEntity);
    }

    @Override
    public int countAllByMy_Id(Long myId) {

        return rivalJpaRepository.countAllByMy_Id(myId);
    }

    @Override
    public int countAllByOpponent_Id(Long opponentId) {

        return rivalJpaRepository.countAllByOpponent_Id(opponentId);
    }

    @Override
    public List<Rival> findAllByMy_Id(Long myId) {

        return rivalJpaRepository.findAllByMy_Id(myId)
                .stream()
                .map(rivalJpaMapper::toDomain)
                .toList();
    }
}
