package com.process.clash.application.record.v2.port.out;

import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RecordTaskV2RepositoryPort {

    RecordTaskV2 save(RecordTaskV2 task);

    Optional<RecordTaskV2> findById(Long id);

    Optional<RecordTaskV2> findByIdAndSubjectId(Long id, Long subjectId);

    List<RecordTaskV2> findAllBySubjectIds(Collection<Long> subjectIds);

    void deleteById(Long id);
}
