package com.process.clash.adapter.persistence.user.usercookiehistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCookieHistoryJpaRepository extends JpaRepository<UserCookieHistoryJpaEntity, Long> {
}
