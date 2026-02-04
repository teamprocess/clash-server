package com.process.clash.adapter.persistence.roadmap.v2.question;

import com.process.clash.adapter.persistence.roadmap.v2.chapter.ChapterV2JpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.chapter.ChapterV2JpaRepository;
import com.process.clash.application.roadmap.v2.port.out.QuestionV2RepositoryPort;
import com.process.clash.domain.roadmap.v2.entity.QuestionV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionV2PersistenceAdapter implements QuestionV2RepositoryPort {

    private final QuestionV2JpaRepository questionV2JpaRepository;
    private final QuestionV2JpaMapper questionV2JpaMapper;
    private final ChapterV2JpaRepository chapterV2JpaRepository;

    @Override
    public QuestionV2 save(QuestionV2 question) {
        ChapterV2JpaEntity chapterEntity = chapterV2JpaRepository.getReferenceById(question.getChapterId());
        QuestionV2JpaEntity savedEntity = questionV2JpaRepository.save(questionV2JpaMapper.toJpaEntity(question, chapterEntity));
        return questionV2JpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<QuestionV2> findById(Long id) {
        return questionV2JpaRepository.findById(id).map(questionV2JpaMapper::toDomain);
    }

    @Override
    public List<QuestionV2> findAllByChapterId(Long chapterId) {
        return questionV2JpaRepository.findAllByChapterId(chapterId).stream().map(questionV2JpaMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        questionV2JpaRepository.deleteById(id);
    }
}
