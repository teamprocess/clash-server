package com.process.clash.adapter.persistence.announcement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AnnouncementJpaRepository extends JpaRepository<AnnouncementJpaEntity, Long> {

    @Query("""
            select a from AnnouncementJpaEntity a
            where a.startAt <= :now and a.endAt >= :now
            order by a.startAt desc
            """)
    List<AnnouncementJpaEntity> findAllActive(@Param("now") Instant now);
}
