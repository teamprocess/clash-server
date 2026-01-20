package com.process.clash.adapter.persistence.user.userexphistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExpHistoryJpaRepository extends JpaRepository<UserExpHistoryJpaEntity, Long> {
}
