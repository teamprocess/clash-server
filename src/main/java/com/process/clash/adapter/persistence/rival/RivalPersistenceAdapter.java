package com.process.clash.adapter.persistence.rival;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void saveAll(List<Rival> rivals) {
        Set<Long> allUserIds = rivals.stream()
                .flatMap(rival -> Stream.of(rival.myId(), rival.opponentId()))
                .collect(Collectors.toSet());

        Map<Long, UserJpaEntity> userMap = userJpaRepository.findAllById(allUserIds)
                .stream()
                .collect(Collectors.toMap(UserJpaEntity::getId, user -> user));

        List<RivalJpaEntity> entities = rivals.stream()
                .map(rival -> {
                    UserJpaEntity my = userMap.get(rival.myId());
                    UserJpaEntity opponent = userMap.get(rival.opponentId());
                    return rivalJpaMapper.toJpaEntity(rival, my, opponent);
                })
                .toList();

        rivalJpaRepository.saveAll(entities);
    }

    @Override
    public int countAllByMyId(Long myId) {

        return rivalJpaRepository.countAllByMyId(myId);
    }

    @Override
    public int countAllByOpponentId(Long opponentId) {

        return rivalJpaRepository.countAllByOpponentId(opponentId);
    }

    @Override
    public List<Rival> findAllByMyId(Long myId) {

        return rivalJpaRepository.findAllByMyId(myId)
                .stream()
                .map(rivalJpaMapper::toDomain)
                .toList();
    }
}
