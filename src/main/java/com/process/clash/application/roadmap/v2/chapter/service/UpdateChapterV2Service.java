package com.process.clash.application.roadmap.v2.chapter.service;

import com.process.clash.adapter.persistence.roadmap.v2.chapter.ChapterV2PersistenceAdapter;
import com.process.clash.application.roadmap.v2.chapter.data.UpdateChapterV2Data;
import com.process.clash.application.roadmap.v2.chapter.port.in.UpdateChapterV2UseCase;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.exception.exception.notfound.ChapterV2NotFoundException;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateChapterV2Service implements UpdateChapterV2UseCase {

    private final ChapterV2RepositoryPort chapterV2RepositoryPort;
    private final ChapterV2PersistenceAdapter chapterV2PersistenceAdapter;

    @Override
    @Transactional
    public UpdateChapterV2Data.Result execute(UpdateChapterV2Data.Command command) {
        // 챕터 존재 여부 확인
        chapterV2RepositoryPort.findById(command.chapterId())
                .orElseThrow(ChapterV2NotFoundException::new);

        // JPA 더티 체킹을 활용한 메타데이터만 업데이트 (성능 최적화)
        ChapterV2 updatedChapter = chapterV2PersistenceAdapter.updateMetadata(
                command.chapterId(),
                command.title(),
                command.description(),
                command.orderIndex(),
                command.studyMaterialUrl()
        );

        return UpdateChapterV2Data.Result.from(updatedChapter);
    }
}
