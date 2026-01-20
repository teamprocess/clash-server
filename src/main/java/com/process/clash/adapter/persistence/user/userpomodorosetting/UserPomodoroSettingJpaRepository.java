package com.process.clash.adapter.persistence.user.userpomodorosetting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPomodoroSettingJpaRepository extends JpaRepository<UserPomodoroSettingJpaEntity, Long> {
}
