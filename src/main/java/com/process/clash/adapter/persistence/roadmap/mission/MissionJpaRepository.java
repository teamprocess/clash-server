package com.process.clash.adapter.persistence.roadmap.mission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionJpaRepository extends JpaRepository<MissionJpaEntity, Long> {
	List<MissionJpaEntity> findAllByChapterId(Long chapterId);

	@Query("SELECT m FROM MissionJpaEntity m " +
		   "LEFT JOIN FETCH m.questions q " +
		   "WHERE m.id = :id")
	Optional<MissionJpaEntity> findByIdWithQuestionsAndChoices(@Param("id") Long id);
}
