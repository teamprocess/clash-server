package com.process.clash.adapter.persistence.roadmap.v2.choice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceV2JpaRepository extends JpaRepository<ChoiceV2JpaEntity, Long> {

    List<ChoiceV2JpaEntity> findAllByQuestionId(Long questionId);
}
