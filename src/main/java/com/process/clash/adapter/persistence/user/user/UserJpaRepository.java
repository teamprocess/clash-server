package com.process.clash.adapter.persistence.user.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
	Optional<UserJpaEntity> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

    List<UserJpaEntity> findByIdIn(List<Long> ids);

	Optional<UserJpaEntity> findByEmail(String email);
}
