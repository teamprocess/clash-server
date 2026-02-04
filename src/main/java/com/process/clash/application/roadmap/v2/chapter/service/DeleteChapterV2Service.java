package com.process.clash.application.roadmap.v2.chapter.service;

import com.process.clash.application.roadmap.v2.chapter.data.DeleteChapterV2Data;
import com.process.clash.application.roadmap.v2.chapter.port.in.DeleteChapterV2UseCase;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteChapterV2Service implements DeleteChapterV2UseCase {

    private final ChapterV2RepositoryPort chapterV2RepositoryPort;

    @Override
    @Transactional
    public void execute(DeleteChapterV2Data.Command command) {
        chapterV2RepositoryPort.deleteById(command.chapterId());
    }
}
