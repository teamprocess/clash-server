package com.process.clash.adapter.persistence.group;

import com.process.clash.adapter.persistence.rival.rival.RivalJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupPersistenceAdapter implements GroupRepositoryPort {

    private final GroupJpaMapper groupJpaMapper;
    private final GroupJpaRepository groupJpaRepository;
    private final UserJpaRepository userJpaRepository;
}
