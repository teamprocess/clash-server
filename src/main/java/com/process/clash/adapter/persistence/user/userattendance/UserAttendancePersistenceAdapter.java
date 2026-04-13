package com.process.clash.adapter.persistence.user.userattendance;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAttendancePersistenceAdapter implements UserAttendanceRepositoryPort {

    private final UserAttendanceJpaRepository userAttendanceJpaRepository;
    private final UserAttendanceJpaMapper userAttendanceJpaMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserAttendance save(UserAttendance userAttendance) {
        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userAttendance.userId());
        UserAttendanceJpaEntity savedEntity = userAttendanceJpaRepository.save(
                userAttendanceJpaMapper.toJpaEntity(userAttendance, userJpaEntity)
        );
        return userAttendanceJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserAttendance> findByUserIdAndIsAttended(Long userId, boolean isAttended) {
        return userAttendanceJpaRepository.findByUserIdAndIsAttended(userId, isAttended)
                .map(userAttendanceJpaMapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndIsAttended(Long userId, boolean isAttended) {
        return userAttendanceJpaRepository.existsByUserIdAndIsAttended(userId, isAttended);
    }
}