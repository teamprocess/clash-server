package com.process.clash.adapter.persistence.user.userstudytime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStudyTimeJpaRepository extends JpaRepository<UserStudyTimeJpaEntity, Long> {
}
