package com.process.clash.adapter.persistence.user.userstudytime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserStudyTimeJpaRepository extends JpaRepository<UserStudyTimeJpaEntity, Long> {

    Optional<UserStudyTimeJpaEntity> findByUserIdAndDate(Long userId, LocalDate date);
}
