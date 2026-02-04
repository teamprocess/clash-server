package com.process.clash.application.roadmap.v2.chapter.service;

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

    @Override
    @Transactional
    public UpdateChapterV2Data.Result execute(UpdateChapterV2Data.Command command) {
        ChapterV2 chapter = chapterV2RepositoryPort.findById(command.chapterId())
                .orElseThrow(ChapterV2NotFoundException::new);

        ChapterV2 updatedChapter = new ChapterV2(
                chapter.getId(),
                chapter.getSectionId(),
                command.title(),
                command.description(),
                command.orderIndex(),
                chapter.getQuestions()
        );

        ChapterV2 savedChapter = chapterV2RepositoryPort.save(updatedChapter);
        return UpdateChapterV2Data.Result.from(savedChapter);
    }
}
