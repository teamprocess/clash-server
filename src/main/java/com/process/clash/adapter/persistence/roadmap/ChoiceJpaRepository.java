package com.process.clash.adapter.persistence.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChoiceJpaRepository extends JpaRepository<ChoiceJpaEntity, Long> {
	List<ChoiceJpaEntity> findAllByQuestionId(Long questionId);
}
