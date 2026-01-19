package com.process.clash.adapter.persistence.user.userstudytime;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserStudyTimePersistenceAdapter implements UserStudyTimeRepositoryPort {

    private final UserStudyTimeJpaRepository userStudyTimeJpaRepository;
    private final UserStudyTimeJpaMapper userStudyTimeJpaMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserStudyTime save(UserStudyTime userStudyTime) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userStudyTime.userId());
        UserStudyTimeJpaEntity savedEntity = userStudyTimeJpaRepository.save(userStudyTimeJpaMapper.toJpaEntity(userStudyTime, userJpaEntity));
        return userStudyTimeJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserStudyTime> findByUserIdAndDate(Long userId, LocalDate date) {

        return userStudyTimeJpaRepository.findByUserIdAndDate(userId, date)
                .map(userStudyTimeJpaMapper::toDomain);
    }
}
