package com.process.clash.application.record.v2.port.out;

import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RecordTaskV2RepositoryPort {

    RecordTaskV2 save(RecordTaskV2 task);

    Optional<RecordTaskV2> findById(Long id);

    Optional<RecordTaskV2> findByIdAndUserId(Long id, Long userId);

    Optional<RecordTaskV2> findByIdAndSubjectId(Long id, Long subjectId);

    Optional<RecordTaskV2> findByIdAndSubjectIdAndUserId(Long id, Long subjectId, Long userId);

    List<RecordTaskV2> findAllByUserIdAndRecordDateOrderBySubjectIdDescNullsFirst(Long userId, LocalDate recordDate);

    List<RecordTaskV2> findAllBySubjectIdsAndRecordDate(Collection<Long> subjectIds, LocalDate recordDate);

    void deleteById(Long id);
}
