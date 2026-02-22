package com.process.clash.adapter.persistence.record.v2.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDevelopSessionV2JpaRepository extends JpaRepository<RecordDevelopSessionV2JpaEntity, Long> {
}
