package com.process.clash.application.record.v2.port.out;

import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import java.util.List;
import java.util.Optional;

public interface RecordSubjectV2RepositoryPort {

    RecordSubjectV2 save(RecordSubjectV2 subject);

    Optional<RecordSubjectV2> findById(Long id);

    Optional<RecordSubjectV2> findByIdAndUserId(Long id, Long userId);

    List<RecordSubjectV2> findAllByUserId(Long userId);

    void deleteById(Long id);
}
