package com.process.clash.adapter.persistence.major;

import com.process.clash.domain.common.enums.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorQuestionJpaRepository extends JpaRepository<MajorQuestionJpaEntity, Long> {

    List<MajorQuestionJpaEntity>findAllByMajor(Major major);

}
