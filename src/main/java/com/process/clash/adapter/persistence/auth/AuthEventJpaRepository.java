package com.process.clash.adapter.persistence.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthEventJpaRepository extends JpaRepository<AuthEventJpaEntity, Long> {

}
