package com.process.clash.adapter.persistence.rival.rival;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public void saveAndFlush(Rival rival) {

        UserJpaEntity firstUser = userJpaRepository.getReferenceById(rival.firstUserId());
        UserJpaEntity secondUser = userJpaRepository.getReferenceById(rival.secondUserId());
        rivalJpaRepository.saveAndFlush(rivalJpaMapper.toJpaEntity(rival, firstUser, secondUser));
    }

    @Override
    public Rival save(Rival rival) {

        UserJpaEntity firstUser = userJpaRepository.getReferenceById(rival.firstUserId());
        UserJpaEntity secondUser = userJpaRepository.getReferenceById(rival.secondUserId());
        RivalJpaEntity savedEntity = rivalJpaRepository.save(rivalJpaMapper.toJpaEntity(rival, firstUser, secondUser));
        return rivalJpaMapper.toDomain(savedEntity);
    }

    @Override
    public List<Rival> saveAll(List<Rival> rivals) {
        Set<Long> allUserIds = rivals.stream()
                .flatMap(rival -> Stream.of(rival.firstUserId(), rival.secondUserId()))
                .collect(Collectors.toSet());

        Map<Long, UserJpaEntity> userMap = userJpaRepository.findAllById(allUserIds)
                .stream()
                .collect(Collectors.toMap(UserJpaEntity::getId, user -> user));

        List<RivalJpaEntity> entities = rivals.stream()
                .map(rival -> {
                    UserJpaEntity my = userMap.get(rival.firstUserId());
                    UserJpaEntity opponent = userMap.get(rival.secondUserId());

                    if (my == null) {
                        throw new IllegalArgumentException(
                                "User not found with id: " + rival.firstUserId()
                        );
                    }
                    if (opponent == null) {
                        throw new IllegalArgumentException(
                                "User not found with id: " + rival.secondUserId()
                        );
                    }

                    return rivalJpaMapper.toJpaEntity(rival, my, opponent);
                })
                .toList();

        return rivalJpaRepository.saveAll(entities).stream()
                .map(rivalJpaMapper::toDomain)
                .toList();
    }

    @Override
    public int countAllByUserId(Long myId) {

        return rivalJpaRepository.countAllByUserId(myId);
    }

    @Override
    public List<Map<String, Object>> countAllByOpponentIdsGrouped(List<Long> opponentIds) {

        return rivalJpaRepository.countAllByUserIdsGrouped(opponentIds);
    }

    @Override
    public List<Rival> findAllByUserId(Long myId) {

        return rivalJpaRepository.findAllByUserId(myId)
                .stream()
                .map(rivalJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Long> findOpponentIdByUserId(Long myId) {

        return rivalJpaRepository.findOpponentIdsByUserId(myId);
    }

    @Override
    public Optional<Rival> findById(Long id) {

        return rivalJpaRepository.findById(id)
                .map(rivalJpaMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {

        rivalJpaRepository.deleteById(id);
    }

    @Override
    public Long findOpponentIdByIdAndUserId(Long id, Long userId) {

        return rivalJpaRepository.findOpponentIdByIdAndUserId(id, userId);
    }

    @Override
    public List<AbleRivalInfoForBattle> findAbleToBattleRivals(Long userId) {

        return rivalJpaRepository.findAbleToBattleRivals(userId);
    }

    @Override
    public Long findOpponentIdByIdAndUserIdInRejectCase(Long id, Long userId) {

        return rivalJpaRepository.findOpponentIdByIdAndUserIdInRejectCase(id, userId);
    }

    @Override
    public List<Rival> findByIdIn(Set<Long> ids) {
        return rivalJpaRepository.findByIdIn(ids).stream()
                .map(rivalJpaMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsPendingRivalRequestFrom(Long opponentId, Long myId) {
        return rivalJpaRepository.existsPendingRivalRequestFrom(opponentId, myId);
    }
}
