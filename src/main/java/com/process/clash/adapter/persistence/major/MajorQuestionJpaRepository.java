package com.process.clash.adapter.persistence.major;

import com.process.clash.domain.common.enums.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorQuestionJpaRepository extends JpaRepository<MajorQuestionJpaEntity, Long> {

}
