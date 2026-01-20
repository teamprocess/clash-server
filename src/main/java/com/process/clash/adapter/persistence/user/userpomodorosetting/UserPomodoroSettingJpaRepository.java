package com.process.clash.adapter.persistence.user.userpomodorosetting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPomodoroSettingJpaRepository extends JpaRepository<UserPomodoroSettingJpaEntity, Long> {

    Optional<UserPomodoroSettingJpaEntity> findByUserId(Long userId);
}
