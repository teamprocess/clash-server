package com.process.clash.adapter.persistence.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionJpaRepository extends JpaRepository<SessionJpaEntity, Long> {
}
