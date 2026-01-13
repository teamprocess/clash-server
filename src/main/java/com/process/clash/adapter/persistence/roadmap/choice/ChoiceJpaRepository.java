package com.process.clash.adapter.persistence.roadmap.choice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceJpaRepository extends JpaRepository<ChoiceJpaEntity, Long> {
	List<ChoiceJpaEntity> findAllByQuestionId(Long questionId);
}
