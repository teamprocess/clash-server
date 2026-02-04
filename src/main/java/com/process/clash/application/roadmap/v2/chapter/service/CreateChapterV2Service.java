package com.process.clash.application.roadmap.v2.chapter.service;

import com.process.clash.application.roadmap.v2.chapter.data.CreateChapterV2Data;
import com.process.clash.application.roadmap.v2.chapter.port.in.CreateChapterV2UseCase;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateChapterV2Service implements CreateChapterV2UseCase {

    private final ChapterV2RepositoryPort chapterV2RepositoryPort;

    @Override
    @Transactional
    public CreateChapterV2Data.Result execute(CreateChapterV2Data.Command command) {
        ChapterV2 chapter = command.toDomain();
        ChapterV2 savedChapter = chapterV2RepositoryPort.save(chapter);
        return CreateChapterV2Data.Result.from(savedChapter);
    }
}
